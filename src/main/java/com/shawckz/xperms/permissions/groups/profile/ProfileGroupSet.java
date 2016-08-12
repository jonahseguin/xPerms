package com.shawckz.xperms.permissions.groups.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import lombok.Getter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 360 on 9/21/2015.
 */
@Getter
@Embedded
public class ProfileGroupSet {

    @Transient
    private XPerms instance;
    @Reference
    private PermServer permServer;
    private Set<String> groups;

    public ProfileGroupSet() {
    }

    public ProfileGroupSet(XPerms instance, PermServer server) {
        this.instance = instance;
        this.permServer = server;
        this.groups = new HashSet<>();
    }

    public ProfileGroupSet(XPerms instance, PermServer server, Set<String> groups) {
        this.instance = instance;
        this.permServer = server;
        this.groups = groups;
    }

    public boolean hasGroup(Group group) {
        return hasGroup(group.getName());
    }

    public boolean hasGroup(String groupName) {
        return groups.contains(groupName);
    }

    public void saveGroup(Group group) {
        groups.add(group.getName());
    }

    public void removeGroup(Group group) {
        groups.remove(group.getName());
    }

    public Group getGroup(String groupName) {
        if (groups.contains(groupName)) {
            return instance.getGroupManager().getGroup(permServer, groupName);
        }
        return null;
    }

}
