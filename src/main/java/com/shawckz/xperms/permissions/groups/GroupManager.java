package com.shawckz.xperms.permissions.groups;


import com.mongodb.BasicDBObject;
import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.database.mongo.AutoMongo;
import com.shawckz.xperms.permissions.PermServer;

import java.util.List;

/**
 * Created by 360 on 9/21/2015.
 */
public class GroupManager {

    private final XPerms instance;
    private final GroupSet groupSet;

    public GroupManager(XPerms instance) {
        this.instance = instance;
        this.groupSet = new GroupSet(instance);
    }

    public void loadGroups() {
        int loadedGroups = 0;
        List<AutoMongo> cursor = DatabaseGroupContainer.select(instance, new BasicDBObject(), DatabaseGroupContainer.class);
        for (AutoMongo result : cursor) {
            if (result instanceof DatabaseGroupContainer) {
                DatabaseGroupContainer container = (DatabaseGroupContainer) result;
                registerGroup(container.getPermServer(), container.getGroup());
                loadedGroups++;
            }
        }
        XPerms.log("Loaded " + loadedGroups + " groups into the cache.");
    }

    public void saveGroups() {
        int savedGroups = 0;
        for (PermServer permServer : groupSet.getGroups().keySet()) {
            XGroupSet xGroupSet = groupSet.getGroupSet(permServer);
            for (Group group : xGroupSet.getGroups().values()) {
                DatabaseGroupContainer container = new DatabaseGroupContainer(instance, permServer, group);
                container.update();
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

    public XGroupSet getGroupSet(PermServer server) {
        return groupSet.getGroupSet(server);
    }

}
