package com.shawckz.xperms.profile;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shawckz.xperms.XPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongodb.morphia.query.Query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ProfileCache implements Listener {

    public static final long CACHE_EXPIRY_MINUTES = 30;

    private final XPerms instance;
    private final ConcurrentMap<String, CachedProfile> cache = new ConcurrentHashMap<>();
    private final Set<FailedCachedProfile> failedCaches = new HashSet<>();
    private final BiMap<String, String> usernameCache = HashBiMap.create(); // <UniqueID, Username>
    private final RedisProfileCache redis;

    public ProfileCache(XPerms plugin) {
        this.instance = plugin;
        this.redis = new RedisProfileCache(plugin);
        instance.getServer().getPluginManager().registerEvents(this, instance);

        // Cache cleanup task
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupCache();
                XPerms.log("Cleaned the local cache");
            }
        }.runTaskTimerAsynchronously(instance, (20 * 60 * 15), (20 * 60 * 15)); //Every 15 minutes

        if (instance.getXConfig().isCacheFailureHandling()) {
            // Failed caches task
            new BukkitRunnable() {
                @Override
                public void run() {
                    Iterator<FailedCachedProfile> it = failedCaches.iterator();
                    while (it.hasNext()) {
                        FailedCachedProfile failedCachedProfile = it.next();
                        if (failedCachedProfile.isOnline()) {
                            XProfile profile = handleCache(failedCachedProfile.getUsername(), failedCachedProfile.getUniqueId());
                            Player p = Bukkit.getPlayerExact(failedCachedProfile.getUsername());
                            if (profile != null) {
                                failedCaches.remove(failedCachedProfile);
                                if (p != null) {
                                    p.sendMessage(ChatColor.GRAY + "[xPerms]" +
                                            ChatColor.GREEN + " Your profile was loaded.");
                                }
                            } else {
                                if (p != null) {
                                    p.sendMessage(ChatColor.GRAY + "[xPerms]" +
                                            ChatColor.GREEN + " Your profile failed to load.");
                                }
                            }
                        } else {
                            failedCaches.remove(failedCachedProfile);
                        }
                    }
                }
            }.runTaskTimerAsynchronously(instance,
                    (20 * instance.getXConfig().getCacheFailIntervalSeconds()), (20 * instance.getXConfig().getCacheFailIntervalSeconds())); //Every 5 minutes
        }
    }

    /* ===============================
    === LISTENERS
    =============================== */

    @EventHandler
    public void onPreLoginCache(AsyncPlayerPreLoginEvent e) {
        final String username = e.getName();
        final String uniqueId = e.getUniqueId().toString();

        if (!instance.getXConfig().isAsyncCaching()) {
            // Only cache from here if async caching is disabled, otherwise this will be handled in PlayerJoinEvent
            XProfile profile = handleCache(username, uniqueId);
            if (profile == null) {
                if (instance.getXConfig().isCacheFailureHandling()) {
                    // Failed to load
                    failedCaches.add(new FailedCachedProfile(username, uniqueId));
                } else {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                            ChatColor.RED + "[xPerms] Your profile failed to load.  Please re-log and try again.  " +
                                    "If this issue persists, contact an administrator.");
                }
            }
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (instance.getXConfig().isAsyncCaching()) {
            //Async caching enabled
            p.sendMessage(ChatColor.GRAY + "[xPerms] Loading your profile...");
            new BukkitRunnable() {
                @Override
                public void run() {
                    XProfile profile = handleCache(p.getName(), p.getUniqueId().toString());
                    if (profile != null) {
                        // Success, initialize
                        initializeProfile(p, profile);
                    } else {
                        handleCacheFailure(p);
                    }
                }
            }.runTaskAsynchronously(instance);
        } else {
            //Async caching disabled
            if (hasProfileCachedLocally(p.getUniqueId().toString())) {
                //Success, initialize
                initializeProfile(p, getLocalProfile(p.getUniqueId().toString()));
            } else {
                handleCacheFailure(p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (instance.getXConfig().isCacheRemoveOnQuit()) {
            final Player p = e.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (hasProfileCachedLocally(p.getUniqueId().toString())) {
                        XProfile profile = removeFromLocalCache(getLocalProfile(p.getUniqueId().toString()));
                        if (profile != null) {
                            saveProfileRedis(profile);
                            saveProfileMongo(profile);
                            // Save in Redis and Mongo, but not locally because we just removed them from the local cache
                            // Which is why I didn't use the profile.saveProfile method
                        }
                    }
                }
            }.runTaskAsynchronously(instance);
        }
    }

    private void handleCacheFailure(Player p) {
        if (instance.getXConfig().isCacheFailureHandling()) {
            // Failed to load, add to failedCache queue and notify
            p.sendMessage(ChatColor.GRAY + "[xPerms]" +
                    ChatColor.GREEN + " Your profile failed to load.");
            p.sendMessage(ChatColor.RED + "We will continue to attempt to load your profile.");
            p.sendMessage(ChatColor.RED + "If this problem persists, try re-logging and contacting an administrator.");
            failedCaches.add(new FailedCachedProfile(p.getName(), p.getUniqueId().toString()));
        } else {
            // Kick them
            instance.getServer().getScheduler().runTask(instance, () ->
                    p.kickPlayer(ChatColor.RED + "[xPerms] Your profile failed to load.  Please re-log and try again.  " +
                            "If this issue persists, contact an administrator."));
        }
    }

    /* ===============================
    === GETTER METHODS
    =============================== */

    public XProfile lookupProfile(String username) {
        Player t = Bukkit.getPlayer(username);
        if (t != null) {
            username = t.getName();
        }
        return getProfileByUsername(username);
    }

    public XProfile getProfileByUsername(String username) {
        String uniqueId = convertToUniqueId(username);
        if (uniqueId != null) {
            // In local username -> uniqueId conversion cache
            return getProfile(uniqueId);
        } else {
            // Not in local username -> uniqueId conversion cache, query mongo

            Query<XProfile> q = instance.getDatabaseManager().getDataStore().createQuery(XProfile.class);
            q.criteria("name").equalIgnoreCase(username);
            Stream<XProfile> stream = q.asList().stream();
            Optional<XProfile> xp = stream.findFirst();

            if (xp.isPresent()) {
                XProfile profile = xp.get();
                return cacheLocally(profile.getUniqueId(), profile);
            } else {
                return null;
            }
        }
    }

    public XProfile getProfile(Player player) {
        return getProfile(player.getUniqueId().toString());
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

    public XProfile getLocalProfile(String uniqueId) {
        return accessProfile(cache.get(uniqueId.toLowerCase()));
    }

    public XProfile getLocalProfile(Player player) {
        return getLocalProfile(player.getUniqueId().toString());
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

    /* ===============================
    === CACHING INTERNALS
    =============================== */


    public XProfile handleCache(String name, String uniqueId) {
        usernameCache.put(uniqueId.toLowerCase(), name);
        try {
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
        } catch (Exception ex) {
            // Failed
            XPerms.log("Failed to cache for name: " + name + ", uniqueId: " + uniqueId);
            return null;
        }
    }

    private XProfile accessProfile(CachedProfile profile) {
        profile.setExpiry(System.currentTimeMillis());
        return profile.getProfile();
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
        cache.put(uniqueId.toLowerCase(), cachedProfile);
        return profile;
    }

    /* ===============================
    === SAVING METHODS
    =============================== */

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

    public XProfile removeFromLocalCache(XProfile profile) {
        if (hasProfileCachedLocally(profile.getUniqueId())) {
            cache.remove(profile.getUniqueId().toLowerCase());
        }
        return profile;
    }

    /* ===============================
    === CHECKER METHODS
    =============================== */

    public boolean hasProfileCachedLocally(String uniqueId) {
        return cache.containsKey(uniqueId.toLowerCase());
    }

    public boolean hasProfileCachedRedis(String uniqueId) {
        return redis.inCache(uniqueId);
    }

    /* ===============================
    === UTILITY METHODS
    =============================== */

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
