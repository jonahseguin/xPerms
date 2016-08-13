package com.shawckz.xperms.permissions.groups;


import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.profile.XProfile;

/**
 * Created by 360 on 9/21/2015.
 */
public class GroupManager {

    private final XPerms instance;
    private final GroupCache groupSet;

    public GroupManager(XPerms instance) {
        this.instance = instance;
        this.groupSet = new GroupCache(instance);
    }

    public void loadGroups() {
        instance.getDatabaseManager().getDataStore()
                .createQuery(Group.class)
                .asList()
                .forEach(group -> registerGroup(group.getPermServer(), group));
        XPerms.log("Loaded groups into the cache.");
    }

    public void saveGroups() {
        int savedGroups = 0;
        for (PermServer permServer : groupSet.getGroups().keySet()) {
            GroupSet xGroupSet = groupSet.getGroupSet(permServer);
            for (Group group : xGroupSet.getGroups().values()) {
                instance.getDatabaseManager().getDataStore().save(group);
                savedGroups++;
            }
        }
        XPerms.log("Saved " + savedGroups + " groups to the database.");
    }

    public void registerGroup(PermServer permServer, Group group) {
        groupSet.saveGroup(permServer, group);
    }

    public boolean hasRegisteredGroup(PermServer server, Group group) {
        return hasRegisteredGroup(server, group.getName());
    }

    public boolean hasRegisteredGroup(PermServer server, String groupName) {
        return getGroupSet(server).hasGroup(groupName);
    }

    public Group getGroup(PermServer server, String groupName) {
        return getGroupSet(server).getGroup(groupName);
    }

    public GroupSet getGroupSet(PermServer server) {
        return groupSet.getGroupSet(server);
    }

    public void refreshPlayerPermissions() {
        instance.getServer().getOnlinePlayers().forEach(player -> {
            XProfile profile = instance.getCache().getLocalProfile(player);
            if (profile != null) {
                profile.refreshPermissions();
            }
        });
    }

}
