package com.shawckz.xperms.profile;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shawckz.xperms.XPerms;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongodb.morphia.query.Query;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ProfileCache implements Listener {

    public static final long CACHE_EXPIRY_MINUTES = 60;

    private final XPerms instance;
    private final ConcurrentMap<String, CachedProfile> cache = new ConcurrentHashMap<>();
    private final BiMap<String, String> usernameCache = HashBiMap.create(); // <UniqueID, Username>
    private final RedisProfileCache redis;

    public ProfileCache(XPerms plugin) {
        this.instance = plugin;
        this.redis = new RedisProfileCache(plugin);
        instance.getServer().getPluginManager().registerEvents(this, instance);

        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupCache();
                XPerms.log("Cleaned the local cache");
            }
        }.runTaskTimerAsynchronously(XPerms.getInstance(), (20 * 60 * 15), (20 * 60 * 15)); //Every 15 minutes
    }

    public XProfile getProfileByName(String username) {
        String uniqueId = convertToUniqueId(username);
        if (uniqueId != null) {
            //In username -> uniqueId conversion cache
            return getProfile(uniqueId);
        } else {
            //Not in username -> uniqueId conversion cache, query mongo
            //TODO
            return null; //TODO TODO TODO TODO
        }
    }

    public XProfile getProfile(String uniqueId) {
        if (hasProfileCachedLocally(uniqueId)) {
            return getLocalProfile(uniqueId);
        } else if (hasProfileCachedRedis(uniqueId)) {
            return cacheLocally(uniqueId, getRedisProfile(uniqueId));
        } else {
            XProfile profile = getMongoProfile(uniqueId);
            if (profile != null) {
                return profile;
            } else {
                return null;
            }
        }
    }

    public XProfile handleCache(String name, String uniqueId) {
        usernameCache.put(uniqueId, name);
        if (hasProfileCachedLocally(uniqueId)) {
            return getLocalProfile(uniqueId);
        } else if (hasProfileCachedRedis(uniqueId)) {
            // Get from Redis, add to local cache
            XProfile profile = getRedisProfile(uniqueId);
            if (!name.equalsIgnoreCase(profile.getName())) {
                // Update username if it has changed
                profile.setName(name);
                saveProfileRedis(profile);
                saveProfileMongo(profile);
            }
            return cacheLocally(uniqueId, profile);
        } else {
            // Not cached, check database or create
            XProfile profile = getMongoProfile(uniqueId);
            if (profile != null) {
                if (!name.equalsIgnoreCase(profile.getName())) {
                    // Update username if it has changed
                    profile.setName(name);
                    saveProfileMongo(profile);
                }
                saveProfileRedis(profile);
                return cacheLocally(uniqueId, profile);
            } else {
                // Not in database, create
                profile = createProfile(name, uniqueId);
                saveProfileEverywhere(profile);
                return profile;
            }
        }
    }

    public void saveProfileEverywhere(XProfile profile) {
        cacheLocally(profile.getUniqueId(), profile);
        saveProfileRedis(profile);
        saveProfileMongo(profile);
    }

    public void saveProfileRedis(XProfile profile) {
        BasicDBObject dbObject = (BasicDBObject) instance.getDatabaseManager().getMorphia().toDBObject(profile);
        redis.cache(profile.getUniqueId(), dbObject.toJson());
    }

    public void saveProfileMongo(XProfile profile) {
        instance.getDatabaseManager().getDataStore().save(profile);
    }

    private XProfile accessProfile(CachedProfile profile) {
        profile.setExpiry(System.currentTimeMillis());
        return profile.getProfile();
    }

    public String convertToName(String uniqueId) {
        if (usernameCache.containsKey(uniqueId)) {
            return usernameCache.get(uniqueId);
        }
        return null;
    }

    public String convertToUniqueId(String name) {
        BiMap<String, String> inverse = usernameCache.inverse();
        if (inverse.containsKey(name)) {
            return inverse.get(name);
        }
        return null;
    }

    public XProfile createProfile(String name, String uuid) {
        return new XProfile(instance, name, uuid);
    }

    public void initializeProfile(Player player, XProfile profile) {
        profile.setPlayer(player);
        profile.refreshPermissions();
    }

    public XProfile cacheLocally(String uniqueId, XProfile profile) {
        CachedProfile cachedProfile = new CachedProfile(profile, System.currentTimeMillis(), getNewCacheExpiry());
        cache.put(uniqueId, cachedProfile);
        return profile;
    }

    public XProfile getLocalProfile(String uniqueId) {
        return accessProfile(cache.get(uniqueId.toLowerCase()));
    }

    public boolean hasProfileCachedLocally(String uniqueId) {
        return cache.containsKey(uniqueId.toLowerCase());
    }

    public boolean hasProfileCachedRedis(String uniqueId) {
        return redis.inCache(uniqueId);
    }

    public XProfile getRedisProfile(String uniqueId) {
        String json = redis.getFromCache(uniqueId);
        DBObject dbObject = BasicDBObject.parse(json);
        return instance.getDatabaseManager().getMorphia().fromDBObject(instance.getDatabaseManager().getDataStore(),
                XProfile.class, dbObject);
    }

    public XProfile getMongoProfile(String uniqueId) {
        Query<XProfile> q = instance.getDatabaseManager().getDataStore().createQuery(XProfile.class);
        q.criteria("uniqueId").equalIgnoreCase(uniqueId);
        Stream<XProfile> stream = q.asList().stream();
        Optional<XProfile> xp = stream.findFirst();
        return xp.isPresent() ? xp.get() : null;
    }

    private long getNewCacheExpiry() {
        return System.currentTimeMillis() + (1000 * 60 * CACHE_EXPIRY_MINUTES);
    }

    /**
     * Remove expired CachedProfiles
     */
    public void cleanupCache() {
        Iterator<String> it = cache.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            CachedProfile profile = cache.get(key);
            if (System.currentTimeMillis() >= profile.getExpiry() && (profile.getProfile().getPlayer() == null || !profile.getProfile().getPlayer().isOnline())) {
                cache.remove(key);
            }
        }
    }


}
