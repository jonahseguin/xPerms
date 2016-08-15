package com.shawckz.xperms.permissions;

import com.shawckz.xperms.XPerms;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PermServerCache {

    private final Map<String, PermServer> servers = new HashMap<>();
    private final PermServer permServer;
    private final PermServer globalPermServer;

    public PermServerCache(PermServer permServer, PermServer globalPermServer) {
        this.permServer = permServer;
        this.globalPermServer = globalPermServer;

        loadPermServers();
        savePermServer(permServer);
        savePermServer(globalPermServer);
    }

    public PermServer getPermServer(String name) {
        return servers.get(name.toLowerCase());
    }

    public boolean hasPermServer(String name) {
        return servers.containsKey(name.toLowerCase());
    }

    public void loadPermServers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                XPerms.getInstance().getDatabaseManager().getDataStore().createQuery(PermServer.class)
                        .asList()
                        .forEach(permServer -> {
                            servers.put(permServer.getName().toLowerCase(), permServer);
                        });
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

    public void savePermServer(final PermServer server) {
        if (server != null) {
            servers.put(server.getName().toLowerCase(), server);
            new BukkitRunnable() {
                @Override
                public void run() {
                    XPerms.getInstance().getDatabaseManager().getDataStore().save(server);
                }
            }.runTaskAsynchronously(XPerms.getInstance());
        }
    }

    public Map<String, PermServer> getServers() {
        return servers;
    }

    public PermServer getThisPermServer() {
        return permServer;
    }

    public PermServer getGlobalPermServer() {
        return globalPermServer;
    }
}
