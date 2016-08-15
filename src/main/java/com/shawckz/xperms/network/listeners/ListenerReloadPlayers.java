package com.shawckz.xperms.network.listeners;

import com.mongodb.BasicDBObject;
import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.network.NetworkListener;
import com.shawckz.xperms.network.XNetworkManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by jonahseguin on 2016-08-13.
 */
public class ListenerReloadPlayers extends NetworkListener {

    public ListenerReloadPlayers() {
        super(NetworkEvent.RELOAD_PLAYERS);
    }

    @Override
    public void onMessage(String channel, String message) {
        BasicDBObject o = BasicDBObject.parse(message);
        String sourceServer = o.getString("source");
        if (!sourceServer.equalsIgnoreCase(XPerms.getInstance().getPermServer().getName())) {
            if (channel.equalsIgnoreCase(getNetworkEvent().getEvent())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        XPerms.getInstance().getGroupManager().loadGroups();
                        XNetworkManager.staffMessage("Reloaded all online players.");
                        XPerms.getInstance().getCache().reloadCache();
                    }
                }.runTaskAsynchronously(XPerms.getInstance());
            }
        }
    }
}
