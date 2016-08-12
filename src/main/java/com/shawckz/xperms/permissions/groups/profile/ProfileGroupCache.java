package com.shawckz.xperms.permissions.groups.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import lombok.Getter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 360 on 9/21/2015.
 */
@Getter
@Entity("xperms_profile_groupsets")
public class ProfileGroupCache {

    @Transient
    private final XPerms instance;

    @Embedded
    private Map<PermServer, ProfileGroupSet> groups;

    public ProfileGroupCache() {
        this.instance = XPerms.getInstance();
    }

    public ProfileGroupCache(XPerms instance) {
        this.instance = instance;
        this.groups = new HashMap<>();
    }

    public ProfileGroupCache(XPerms instance, Map<PermServer, ProfileGroupSet> groups) {
        this.instance = instance;
        this.groups = groups;
    }

    public Group getGroup(PermServer server, String name) {
        return getGroupSet(server).getGroup(name);
    }

    public Group getLocalGroup(String name) {
        return getGroup(instance.getPermServer(), name);
    }

    public ProfileGroupSet getGroupSet(PermServer permServer) {
        if (!groups.containsKey(permServer)) {
            groups.put(permServer, new ProfileGroupSet(instance, permServer));
        }
        return groups.get(permServer);
    }

    public void saveGroup(PermServer permServer, Group group) {
        getGroupSet(permServer).saveGroup(group);
    }
}
