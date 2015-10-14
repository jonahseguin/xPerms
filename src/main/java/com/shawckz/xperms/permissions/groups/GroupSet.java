package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 360 on 9/21/2015.
 */
@Getter
public class GroupSet {

    private final XPerms instance;
    private final Map<PermServer, XGroupSet> groups;

    public GroupSet(XPerms instance) {
        this.instance = instance;
        this.groups = new HashMap<>();
    }

    public GroupSet(XPerms instance, Map<PermServer, XGroupSet> groups){
        this.instance = instance;
        this.groups = groups;
    }

    public Group getGroup(PermServer server, String name){
        if(groups.containsKey(server)){
            if(groups.get(server).getGroups().containsKey(name)) {
                return groups.get(server).getGroups().get(name);
            }
        }
        return null;
    }

    public Group getLocalGroup(String name){
        return getGroup(instance.getPermServer(),name);
    }

    public XGroupSet getGroupSet(PermServer permServer){
        if(!groups.containsKey(permServer)){
            groups.put(permServer, new XGroupSet(permServer));
        }
        return groups.get(permServer);
    }

    public void saveGroup(PermServer permServer, Group group){
        getGroupSet(permServer).saveGroup(group);
    }

}
