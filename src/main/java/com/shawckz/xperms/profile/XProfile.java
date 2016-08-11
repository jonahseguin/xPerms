package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.groups.profile.WrapperProfileGroupSet;
import com.shawckz.xperms.permissions.perms.Permission;
import com.shawckz.xperms.profile.internal.CachePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

@Getter
@Setter
@Entity("xperms_profiles")
public class XProfile extends CachePlayer {

    @Id
    private ObjectId id;

    private String uniqueId;

    private String name;

    @Reference
    private WrapperProfileGroupSet groups;

    /* Local Variables */
    @Transient
    private final XPerms instance;
    @Transient
    private Player player;
    @Transient
    private PermissionAttachment permissionAttachment;

    public XProfile(XPerms instance) {
        super(instance);
        this.instance = instance;
        //Keep empty constructor so that AutoMongo can instantiate
        this.groups = new WrapperProfileGroupSet(instance);
    }

    public XProfile(XPerms instance, String uniqueId, String name) {
        super(instance);
        this.instance = instance;
        this.uniqueId = uniqueId;
        this.name = name;
        this.groups = new WrapperProfileGroupSet(instance);
    }

    /**
     * Re-create the permissions attachment, and re-attach all permissions
     * Should be called after applying modifying groups or permissions
     * Loads global and then local permissions, so that local permissions
     * override global permissions.
     */
    public void refreshPermissions() {
        if (permissionAttachment != null) {
            permissionAttachment.remove();
            permissionAttachment = null;
        }
        permissionAttachment = new PermissionAttachment(instance, player);
        //Load permissions from GLOBAL
        for (String groupName : groups.getGroupSet(instance.getGlobalPermServer()).getGroups()) {
            Group g = instance.getGroupManager().getGroup(instance.getGlobalPermServer(), groupName);
            for (Permission permission : g.getGroupPermissions().getPermissions().values()) {
                permissionAttachment.setPermission(permission.getPermission(), permission.isValue());
            }
        }
        //Load permissions from LOCAL, load after loading GLOBAL so that local overrides global
        for (String groupName : groups.getGroupSet(instance.getPermServer()).getGroups()) {
            Group g = instance.getGroupManager().getGroup(instance.getPermServer(), groupName);
            for (Permission permission : g.getGroupPermissions().getPermissions().values()) {
                permissionAttachment.setPermission(permission.getPermission(), permission.isValue());
            }
        }
    }

}
