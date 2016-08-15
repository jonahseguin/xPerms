package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.perms.Permissions;
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
@NoArgsConstructor
@Entity("xperms_groups")
public class Group {

    @Id
    private ObjectId id;

    @Transient
    private XPerms instance;
    private String name;
    private String prefix = "";
    private String suffix = "";
    @Embedded
    private Permissions groupPermissions;
    @Embedded
    private SubGroups subGroups;

    public Group(XPerms instance, String name) {
        this.instance = instance;
        this.name = name;
        this.groupPermissions = new Permissions();
        this.subGroups = new SubGroups();
        subGroups.setSuperGroup(this);
    }

    public Permissions getPermissions() {
        return subGroups.getAllPermissions();
    }

    @PostLoad
    public void onPostLoad() {
        this.instance = XPerms.getInstance();
    }

}
