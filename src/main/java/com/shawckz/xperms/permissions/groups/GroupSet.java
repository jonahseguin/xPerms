package com.shawckz.xperms.permissions.groups;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 9/21/2015.
 */
@Data
public class GroupSet {

    private final Map<String, Group> groups = new HashMap<>();

    public boolean hasGroup(String groupName) {
        return groups.containsKey(groupName);
    }

    public void saveGroup(Group group) {
        groups.put(group.getName(), group);
    }

    public void removeGroup(Group group) {
        groups.remove(group.getName());
    }

    public Group getGroup(String groupName) {
        if (hasGroup(groupName)) {
            return groups.get(groupName);
        }
        return null;
    }

}
