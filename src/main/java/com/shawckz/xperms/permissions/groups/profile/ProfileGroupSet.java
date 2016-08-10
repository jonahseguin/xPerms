package com.shawckz.xperms.permissions.groups.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 360 on 9/21/2015.
 */
@Getter
public class ProfileGroupSet {

    private final XPerms instance;
    private final Map<PermServer, XProfileGroupSet> groups;

    public ProfileGroupSet(XPerms instance) {
        this.instance = instance;
        this.groups = new HashMap<>();
    }

    public ProfileGroupSet(XPerms instance, Map<PermServer, XProfileGroupSet> groups) {
        this.instance = instance;
        this.groups = groups;
    }

    public Group getGroup(PermServer server, String name) {
        return getGroupSet(server).getGroup(name);
    }

    public Group getLocalGroup(String name) {
        return getGroup(instance.getPermServer(), name);
    }

    public XProfileGroupSet getGroupSet(PermServer permServer) {
        if (!groups.containsKey(permServer)) {
            groups.put(permServer, new XProfileGroupSet(instance, permServer));
        }
        return groups.get(permServer);
    }

    public void saveGroup(PermServer permServer, Group group) {
        getGroupSet(permServer).saveGroup(group);
    }
}
