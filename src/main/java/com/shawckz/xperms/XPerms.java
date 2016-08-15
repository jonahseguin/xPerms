package com.shawckz.xperms;

import com.shawckz.xperms.command.GCommandHandler;
import com.shawckz.xperms.commands.*;
import com.shawckz.xperms.config.XConfig;
import com.shawckz.xperms.database.DatabaseManager;
import com.shawckz.xperms.network.XNetworkManager;
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
    private XNetworkManager networkManager;

    @Override
    public void onEnable() {
        XPerms.instance = this;
        this.config = new XConfig(this);
        this.config.load();
        this.config.save();
        this.serverCache = new PermServerCache(new PermServer(this.config.getServer()), new PermServer(GLOBAL_SERVER));
        this.databaseManager = new DatabaseManager(this);
        this.groupManager = new GroupManager(this);
        this.groupManager.loadGroups();
        this.cache = new ProfileCache(this);
        this.networkManager = new XNetworkManager(this);

        GCommandHandler cmd = new GCommandHandler(this);
        cmd.registerCommands(new CmdXPerms());
        cmd.registerCommands(new CmdAddGroup());
        cmd.registerCommands(new CmdAddPermission());
        cmd.registerCommands(new CmdRemoveGroup());
        cmd.registerCommands(new CmdCreateGroup());
        cmd.registerCommands(new CmdDeleteGroup());
        cmd.registerCommands(new CmdCreateServer());
        cmd.registerCommands(new CmdRemovePermission());
        cmd.registerCommands(new CmdListServers());
        cmd.registerCommands(new CmdListGroups());
        cmd.registerCommands(new CmdShowGroups());
        cmd.registerCommands(new CmdAddSubgroup());
        cmd.registerCommands(new CmdRemoveSubgroup());
        cmd.registerCommands(new CmdGroupInfo());
        cmd.registerCommands(new CmdSetSuffix());
        cmd.registerCommands(new CmdTestPermission());
    }

    @Override
    public void onDisable() {
        this.groupManager.saveGroups();
        this.databaseManager.shutdown();
        XPerms.instance = null;
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

    public XNetworkManager getNetworkManager() {
        return networkManager;
    }
}
