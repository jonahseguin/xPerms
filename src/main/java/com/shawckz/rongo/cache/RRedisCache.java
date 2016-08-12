package com.shawckz.rongo.cache;

import com.shawckz.rongo.Cacheable;
import redis.clients.jedis.Jedis;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public abstract class RRedisCache<K extends Cacheable> implements IRongoCache<String, K> {

    private final Jedis jedis;
    private final String cacheKey;

    public RRedisCache(Jedis jedis, String cacheKey) {
        this.jedis = jedis;
        this.cacheKey = cacheKey;
    }

    @Override
    public boolean inCache(String search) {
        return jedis.exists(cacheKey + search.toLowerCase());
    }

    @Override
    public void cache(K cacheable) {
        jedis.set(cacheKey + cacheable.getIdentifier().toLowerCase(), cacheable.toJSON());
    }

    @Override
    public void uncache(K cacheable) {
        jedis.del(cacheKey + cacheable.getIdentifier().toLowerCase());
    }

    @Override
    public K get(String search) {
        if (inCache(search)) {
            String json = jedis.get(cacheKey + search.toLowerCase());
            return fromJSON(json);
        }
        return null;
    }

    public abstract K fromJSON(String json);

}
