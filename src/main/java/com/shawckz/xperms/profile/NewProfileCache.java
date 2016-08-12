package com.shawckz.xperms.profile;

import com.shawckz.rongo.bukkit.BukkitRongoCache;
import com.shawckz.xperms.XPerms;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import redis.clients.jedis.Jedis;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public class NewProfileCache extends BukkitRongoCache<XProfile> {

    private final XPerms instance;

    public NewProfileCache(XPerms instance, Jedis jedis, String redisKey, Datastore datastore) {
        super(instance, jedis, redisKey, datastore);
        this.instance = instance;
    }

    @Override
    public XProfile createCacheable(String username, String uniqueId) {
        return new XProfile(instance, uniqueId, username);
    }

    @Override
    public void initialize(Player player, XProfile cacheable) {

    }

    @Override
    public XProfile fromJSON(String json) {
        return null;
    }

    @Override
    public Class<XProfile> getTypeClass() {
        return XProfile.class;
    }

    @Override
    public String getMongoQueryKey() {
        return null;
    }
}
