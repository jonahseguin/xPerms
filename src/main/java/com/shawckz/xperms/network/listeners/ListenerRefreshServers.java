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
public class ListenerRefreshServers extends NetworkListener {

    public ListenerRefreshServers() {
        super(NetworkEvent.REFRESH_SERVERS);
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
                        XPerms.getInstance().getServerCache().getServers().clear();
                        XPerms.getInstance().getServerCache().loadPermServers();
                        XNetworkManager.staffMessage("Loaded permission servers from database.");
                    }
                }.runTaskAsynchronously(XPerms.getInstance());
            }
        }
    }
}
