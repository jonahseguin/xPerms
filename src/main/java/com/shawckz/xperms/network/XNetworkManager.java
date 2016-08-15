package com.shawckz.xperms.network;

import com.mongodb.BasicDBObject;
import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.network.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import redis.clients.jedis.Jedis;

/**
 * Created by jonahseguin on 2016-08-13.
 */
public class XNetworkManager {

    private final XPerms instance;
    private Jedis jedis;

    public XNetworkManager(XPerms instance) {
        this.instance = instance;
        if (instance.getDatabaseManager().isRedisEnabled()) {
            this.jedis = instance.getDatabaseManager().getJedisResource();
            // TODO: Return resource..

            registerListener(new ListenerRefreshGroups());
            registerListener(new ListenerRefreshServers());
            registerListener(new ListenerReloadPlayers());
            registerListener(new ListenerReloadPlayer());
            registerListener(new ListenerBroadcast());
        }
    }

    public void registerListener(NetworkListener listener) {
        if (instance.getDatabaseManager().isRedisEnabled()) {
            jedis.subscribe(listener, listener.getNetworkEvent().getEvent());
        }
    }

    public void publish(NetworkEvent event, String message) {
        if (instance.getDatabaseManager().isRedisEnabled()) {
            jedis.publish(event.getEvent(), message);
        }
    }

    public void callEvent(NetworkEvent event) {
        publish(event, new BasicDBObject("source", XPerms.getInstance().getPermServer().getName()).toJson());
    }

    public static void staffMessage(String msg) {
        final String message = ChatColor.translateAlternateColorCodes('&', "&7[xPerms] &e" + msg);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
        XPerms.log("[Staff] " + message);
    }

}
