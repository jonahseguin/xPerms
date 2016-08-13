package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import redis.clients.jedis.Jedis;

/**
 * Created by jonahseguin on 2016-08-12.
 */
public class RedisProfileCache {

    public static final String REDIS_KEY = "xPerms.profile.";

    private final XPerms instance;
    private final Jedis jedis;

    public RedisProfileCache(XPerms instance) {
        this.instance = instance;
        this.jedis = instance.getDatabaseManager().getJedisResource();
        // TODO: Return the resource on shutdown
    }

    public boolean inCache(String uniqueId) {
        return jedis.exists(getKey(uniqueId));
    }

    public void cache(String uniqueId, String json) {
        jedis.set(getKey(uniqueId), json);
    }

    public String getFromCache(String uniqueId) {
        return jedis.get(getKey(uniqueId));
    }

    public void uncache(String uniqueId) {
        jedis.del(getKey(uniqueId));
    }

    private String getKey(String uniqueId) {
        return REDIS_KEY + uniqueId.toLowerCase();
    }

}
