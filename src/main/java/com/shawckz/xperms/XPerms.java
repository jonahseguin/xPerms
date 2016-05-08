package com.shawckz.xperms;

import com.shawckz.xperms.config.XConfig;
import com.shawckz.xperms.database.DatabaseManager;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.groups.GroupManager;
import com.shawckz.xperms.permissions.groups.SubGroups;
import com.shawckz.xperms.permissions.perms.Permissions;
import com.shawckz.xperms.profile.XCache;
import com.shawckz.xperms.profile.XProfile;

import org.bukkit.plugin.java.JavaPlugin;

public class XPerms extends JavaPlugin {

    private static XPerms instance;
    private XConfig config;
    private DatabaseManager databaseManager;
    private GroupManager groupManager;
    private XCache cache;
    private PermServer permServer;
    private PermServer globalPermServer = new PermServer("GLOBAL");

    @Override
    public void onEnable(){
        XPerms.instance = this;
        this.config = new XConfig(this);
        this.config.load();
        this.config.save();
        this.permServer = new PermServer(this.config.getServer());
        this.databaseManager = new DatabaseManager(this);
        this.groupManager = new GroupManager(this);
        this.groupManager.loadGroups();
        this.cache = new XCache(this);

        XProfile profile = XPerms.getInstance().getCache().getProfile("Shawckz");

        Group group = XPerms.getInstance().getGroupManager().getGroup(XPerms.getInstance().getPermServer(), "SomeGroup");
        if(group == null) {
            group =  new Group(XPerms.getInstance(),
                    XPerms.getInstance().getPermServer(),
                    "SomeGroup", //Group name
                    "&e[Prefix]", //Group prefix
                    "&9[Suffix]", //Group suffix
                    new Permissions(), //Or pass in a hashmap of permissions
                    new SubGroups(XPerms.getInstance()) //Or copy subgroups from an existing group...
            );

            XPerms.getInstance().getGroupManager().registerGroup(XPerms.getInstance().getPermServer(), group);
        }

        profile.getGroup().saveGroup(XPerms.getInstance().getPermServer(), group); //Add the group to the player's local groups

        profile.refreshPermissions();//Load permissions from group

    }

    @Override
    public void onDisable(){
        this.groupManager.saveGroups();
        this.databaseManager.shutdown();
        instance = null;
    }

    public static void log(String msg){
        getInstance().getLogger().info(msg);
    }

    public static XPerms getInstance() {
        return instance;
    }

    public XCache getCache() {
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
        return permServer;
    }

    public PermServer getGlobalPermServer() {
        return globalPermServer;
    }
}
