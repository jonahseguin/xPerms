package com.shawckz.xperms;

import com.shawckz.xperms.command.GCommandHandler;
import com.shawckz.xperms.commands.CmdAddGroup;
import com.shawckz.xperms.commands.CmdXPerms;
import com.shawckz.xperms.config.XConfig;
import com.shawckz.xperms.database.DatabaseManager;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.PermServerCache;
import com.shawckz.xperms.permissions.groups.GroupManager;
import com.shawckz.xperms.profile.ProfileCache;
import org.bukkit.plugin.java.JavaPlugin;

public class XPerms extends JavaPlugin {

    public static final String GLOBAL_SERVER = "GLOBAL";

    private static XPerms instance;
    private XConfig config;
    private DatabaseManager databaseManager;
    private GroupManager groupManager;
    private ProfileCache cache;
    private PermServerCache serverCache;

    @Override
    public void onEnable() {
        XPerms.instance = this;
        this.config = new XConfig(this);
        this.config.load();
        this.config.save();
        serverCache = new PermServerCache(new PermServer(this.config.getServer()), new PermServer(GLOBAL_SERVER));
        this.databaseManager = new DatabaseManager(this);
        this.groupManager = new GroupManager(this);
        this.groupManager.loadGroups();
        this.cache = new ProfileCache(this);

        GCommandHandler cmd = new GCommandHandler(this);
        cmd.registerCommands(new CmdXPerms());
        cmd.registerCommands(new CmdAddGroup());
    }

    @Override
    public void onDisable() {
        this.groupManager.saveGroups();
        this.databaseManager.shutdown();
        instance = null;
    }

    public static void log(String msg) {
        getInstance().getLogger().info(msg);
    }

    public static XPerms getInstance() {
        return instance;
    }

    public ProfileCache getCache() {
        return cache;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public XConfig getXConfig() {
        return config;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PermServer getPermServer() {
        return serverCache.getThisPermServer();
    }

    public PermServer getGlobalPermServer() {
        return serverCache.getGlobalPermServer();
    }

    public PermServerCache getServerCache() {
        return serverCache;
    }
}
