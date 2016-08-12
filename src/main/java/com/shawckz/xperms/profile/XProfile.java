package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.groups.profile.ProfileGroupCache;
import com.shawckz.xperms.permissions.perms.Permission;
import com.shawckz.xperms.permissions.perms.Permissions;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.mongodb.morphia.annotations.*;

@Getter
@Setter
@Entity("xperms_profiles")
public class XProfile {

    @Id
    private ObjectId id;

    private String uniqueId;

    private String name;

    @Reference
    private ProfileGroupCache groups;

    @Embedded
    private Permissions permissions = new Permissions();

    /* Local Variables */
    @Transient
    private final XPerms instance;
    @Transient
    private Player player;
    @Transient
    private PermissionAttachment permissionAttachment;

    public XProfile(XPerms instance) {
        this.instance = instance;
        //Keep empty constructor so that AutoMongo can instantiate
        this.groups = new ProfileGroupCache(instance);
    }

    public XProfile(XPerms instance, String uniqueId, String name) {
        this.instance = instance;
        this.uniqueId = uniqueId;
        this.name = name;
        this.groups = new ProfileGroupCache(instance);
    }

    /**
     * Re-create the permissions attachment, and re-attach all permissions
     * Should be called after applying modifying groups or permissions
     * Loads global and then local permissions, so that local permissions
     * override global permissions.
     * Per-player permissions override any and all conflicting permissions
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
        //Load per-player permissions, which overrides any conflicting GLOBAL and LOCAL permissions
        permissions.getPermissions()
                .values()
                .forEach(
                        permission -> permissionAttachment.setPermission(permission.getPermission(), permission.isValue())
                );
    }

    public void saveProfile() {
        instance.getDatabaseManager().getDataStore().save(this);
    }

}
