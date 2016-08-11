package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.perms.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 * Created by 360 on 9/21/2015.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity("xperms_groups")
public class Group {

    @Id
    private ObjectId id;

    @Transient
    private XPerms instance;
    @Reference
    private PermServer permServer;
    private String name;
    private String prefix;
    private String suffix;
    @Embedded
    private Permissions groupPermissions;
    @Embedded
    private SubGroups subGroups;

    public Permissions getPermissions() {
        return subGroups.getAllPermissions();
    }

}
