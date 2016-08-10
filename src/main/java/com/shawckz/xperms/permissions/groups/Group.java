package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.perms.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 360 on 9/21/2015.
 */
@Getter
@Setter
@AllArgsConstructor
public class Group {

    private final XPerms instance;
    private final PermServer permServer;
    private String name;
    private String prefix;
    private String suffix;
    private Permissions groupPermissions;
    private SubGroups subGroups;

    public Permissions getPermissions() {
        return subGroups.getAllPermissions();
    }

}
