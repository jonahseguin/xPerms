package com.shawckz.xperms.permissions.groups.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 360 on 9/21/2015.
 */
@Getter
public class XProfileGroupSet {

    private final XPerms instance;
    private final PermServer permServer;
    private final Set<String> groups;

    public XProfileGroupSet(XPerms instance, PermServer server) {
        this.instance = instance;
        this.permServer = server;
        this.groups = new HashSet<>();
    }

    public XProfileGroupSet(XPerms instance, PermServer server, Set<String> groups) {
        this.instance = instance;
        this.permServer = server;
        this.groups = groups;
    }

    public boolean hasGroup(String groupName){
        return groups.contains(groupName);
    }

    public void saveGroup(Group group){
        groups.add(group.getName());
    }

    public void removeGroup(Group group){
        groups.remove(group.getName());
    }

    public Group getGroup(String groupName){
        if(groups.contains(groupName)){
            return instance.getGroupManager().getGroup(permServer, groupName);
        }
        return null;
    }

}
