package com.shawckz.rongo.bukkit;

import com.shawckz.rongo.Cacheable;
import com.shawckz.rongo.RongoCache;
import com.shawckz.rongo.cache.IRongoCache;
import com.shawckz.rongo.cache.RLocalCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mongodb.morphia.Datastore;
import redis.clients.jedis.Jedis;

public abstract class BukkitRongoCache<T extends Cacheable> extends RongoCache<T> implements Listener {

    private final JavaPlugin instance;

    public BukkitRongoCache(JavaPlugin instance, Jedis jedis, String redisKey, Datastore datastore) {
        super(jedis, redisKey, datastore);
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    public abstract T createCacheable(String username, String uniqueId);

    public abstract void initialize(Player player, T cacheable);

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            final String username = e.getName();
            final String uniqueId = e.getUniqueId().toString();

            if (inAnyCache(username)) {
                IRongoCache<String, T> cache = getLowestContainingCache(username);
                if (!(cache instanceof RLocalCache)) {
                    cacheLocally(cache.get(username));
                }
            } else {
                cacheEverywhere(createCacheable(username, uniqueId));
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (getLocalCache().inCache(e.getPlayer().getName())) {
            initialize(e.getPlayer(), get(e.getPlayer().getName()));
        } else {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&c&lYour profile [" + instance.getDescription().getName() + "] was not loaded, please re-log and try again."));
        }
    }

}
