package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.mongodb.morphia.query.Query;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ProfileCache implements Listener {

    private final XPerms instance;
    private final ConcurrentMap<String, XProfile> cache = new ConcurrentHashMap<>();

    public ProfileCache(XPerms plugin) {
        this.instance = plugin;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    public XProfile createProfile(String name, String uuid) {
        return new XProfile(instance, name, uuid);
    }

    public void initializeProfile(Player player, XProfile profile) {
        profile.setPlayer(player);
        profile.refreshPermissions();
    }

    public XProfile getLocalProfile(String name) {
        return cache.get(name.toLowerCase());
    }

    public boolean hasProfileCached(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    public XProfile cacheProfile(XProfile profile) {
        cache.put(profile.getName().toLowerCase(), profile);
        return profile;
    }

    public XProfile lookupProfile(String name) {
        Player targetPlayer = Bukkit.getPlayer(name);
        if (targetPlayer != null) {
            name = targetPlayer.getName();
        }
        return getProfile(name);
    }

    public XProfile loadProfileSync(String name) {
        Query<XProfile> q = instance.getDatabaseManager().getDataStore().createQuery(XProfile.class);
        q.criteria("name").equalIgnoreCase(name);
        Stream<XProfile> stream = q.asList().stream();
        Optional<XProfile> xp = stream.findFirst();
        return xp.isPresent() ? xp.get() : null;
    }

    public XProfile loadProfileAndCacheSync(String name) {
        XProfile profile = loadProfileSync(name);
        if (profile != null) {
            return profile;
        } else {
            return null;
        }
    }

    public XProfile loadOrCreateProfileSync(String name, String uniqueId) {
        if (hasProfileCached(name)) {
            return getLocalProfile(name);
        } else {
            XProfile profile = loadProfileSync(name);
            if (profile != null) {
                return profile;
            } else {
                return createProfile(name, uniqueId);
            }
        }
    }

    public XProfile getProfile(String name) {
        if (hasProfileCached(name)) {
            return getLocalProfile(name);
        } else {
            return loadProfileAndCacheSync(name);
        }
    }

    public XProfile getProfile(Player player) {
        return getProfile(player.getName());
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            final String username = e.getName();
            final String uniqueId = e.getUniqueId().toString();

            cacheProfile(loadOrCreateProfileSync(username, uniqueId));
        }
    }

}
