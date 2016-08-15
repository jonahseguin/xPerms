package com.shawckz.xperms.permissions.groups;


import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.profile.XProfile;

/**
 * Created by 360 on 9/21/2015.
 */
public class GroupManager {

    private final XPerms instance;
    private final GroupSet groupSet;

    public GroupManager(XPerms instance) {
        this.instance = instance;
        this.groupSet = new GroupSet();
    }

    public void loadGroups() {
        // Clear current groups
        groupSet.getGroups().clear();
        // Load new groups
        instance.getDatabaseManager().getDataStore()
                .createQuery(Group.class)
                .asList()
                .forEach(this::registerGroup);
        XPerms.log("Loaded groups into the cache.");
    }

    public void saveGroups() {
        int savedGroups = 0;
        for (Group group : groupSet.getGroups().values()) {
            instance.getDatabaseManager().getDataStore().save(group);
            savedGroups++;
        }
        XPerms.log("Saved " + savedGroups + " groups to the database.");
    }

    public void registerGroup(Group group) {
        groupSet.saveGroup(group);
    }

    public void unregisterGroup(Group group) {
        groupSet.removeGroup(group);
    }

    public boolean hasRegisteredGroup(Group group) {
        return hasRegisteredGroup(group.getName());
    }

    public boolean hasRegisteredGroup(String groupName) {
        return getGroupSet().hasGroup(groupName);
    }

    public Group getGroup(String groupName) {
        return getGroupSet().getGroup(groupName);
    }

    public GroupSet getGroupSet() {
        return groupSet;
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
