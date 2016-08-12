package com.shawckz.rongo;

import com.shawckz.rongo.cache.IRongoCache;
import com.shawckz.rongo.cache.RLocalCache;
import com.shawckz.rongo.cache.RMongoCache;
import com.shawckz.rongo.cache.RRedisCache;
import org.mongodb.morphia.Datastore;
import redis.clients.jedis.Jedis;

public abstract class RongoCache<T extends Cacheable> {

    // All implement IRongoCache<String, T>
    private final RLocalCache<T> local;
    private final RRedisCache<T> redis;
    private final RMongoCache<T> mongo;

    public RongoCache(Jedis jedis, String redisKey, Datastore datastore) {
        this.local = new RLocalCache<>();
        this.redis = new RRedisCache<T>(jedis, redisKey) {
            @Override
            public T fromJSON(String json) {
                return RongoCache.this.fromJSON(json);
            }
        };
        this.mongo = new RMongoCache<T>(datastore) {
            @Override
            public Class<T> getTypeClass() {
                return RongoCache.this.getTypeClass();
            }

            @Override
            public String getQueryKey() {
                return getMongoQueryKey();
            }
        };
    }

    public T get(String key) {
        IRongoCache<String, T> cache = getLowestContainingCache(key);
        if (cache != null) {
            if (cache instanceof RLocalCache) {
                return cache.get(key);
            } else {
                return getAndCache(key, cache);
            }
        } else {
            return null;
        }
    }

    public boolean inAnyCache(String key) {
        return local.inCache(key) || redis.inCache(key) || mongo.inCache(key);
    }

    public IRongoCache<String, T> getLowestContainingCache(String key) {
        if (local.inCache(key)) {
            return local;
        } else if (redis.inCache(key)) {
            return redis;
        } else if (mongo.inCache(key)) {
            return mongo;
        } else {
            return null;
        }
    }

    public void cacheLocally(T cacheable) {
        local.cache(cacheable);
    }

    public void cacheEverywhere(T cacheable) {
        local.cache(cacheable);
        redis.cache(cacheable);
        mongo.cache(cacheable);
    }

    public RLocalCache<T> getLocalCache() {
        return local;
    }

    public RRedisCache<T> getRedisCache() {
        return redis;
    }

    public RMongoCache<T> getMongoCache() {
        return mongo;
    }

    private T getAndCache(String key, IRongoCache<String, T> cache) {
        T val = cache.get(key);
        local.cache(val);
        return val;
    }

    public abstract T fromJSON(String json);

    public abstract Class<T> getTypeClass();

    public abstract String getMongoQueryKey();

}
