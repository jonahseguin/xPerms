package com.shawckz.xperms.network.listeners;

import com.mongodb.BasicDBObject;
import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.network.NetworkListener;
import com.shawckz.xperms.profile.XProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by jonahseguin on 2016-08-13.
 */
public class ListenerReloadPlayer extends NetworkListener {

    public ListenerReloadPlayer() {
        super(NetworkEvent.RELOAD_PLAYER);
    }

    @Override
    public void onMessage(String channel, String message) {
        BasicDBObject o = BasicDBObject.parse(message);
        String sourceServer = o.getString("source");
        String targetUniqueId = o.getString("player");
        if (!sourceServer.equalsIgnoreCase(XPerms.getInstance().getPermServer().getName())) {
            if (channel.equalsIgnoreCase(getNetworkEvent().getEvent())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player target = Bukkit.getPlayer(UUID.fromString(targetUniqueId));
                        if (target != null) {
                            XProfile profile = XPerms.getInstance().getCache().getLocalProfile(target.getUniqueId().toString());
                            XPerms.getInstance().getCache().reloadPlayer(target, profile);
                        }
                    }
                }.runTaskAsynchronously(XPerms.getInstance());
            }
        }
    }
}
