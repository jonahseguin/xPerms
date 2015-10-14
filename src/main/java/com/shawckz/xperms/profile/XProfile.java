package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.database.mongo.annotations.CollectionName;
import com.shawckz.xperms.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.xperms.database.mongo.annotations.MongoColumn;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.groups.profile.ProfileGroupSet;
import com.shawckz.xperms.permissions.perms.Permission;
import com.shawckz.xperms.permissions.serial.ProfileGroupSerializer;
import com.shawckz.xperms.profile.internal.CachePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

@CollectionName(name = "xpermplayers")
@Getter
@Setter
public class XProfile extends CachePlayer {

    //I would make these private, but due to how AutoMongo populates values I cannot
    @MongoColumn(name = "uniqueId", identifier = true)
    private String uniqueId;

    @MongoColumn(name = "username")
    private String name;

    @MongoColumn(name = "profileGroup")
    @DatabaseSerializer(serializer = ProfileGroupSerializer.class)
    private ProfileGroupSet group;

    private final XPerms instance;
    private Player player;
    private PermissionAttachment permissionAttachment;

    public XProfile(XPerms instance) {
        super(instance);
        this.instance = instance;
        //Keep empty constructor so that AutoMongo can instantiate
        this.group = new ProfileGroupSet(instance);
    }

    public XProfile(XPerms instance, String uniqueId, String name) {
        super(instance);
        this.instance = instance;
        this.uniqueId = uniqueId;
        this.name = name;
        this.group = new ProfileGroupSet(instance);
    }

    public void refreshPermissions(){
        if(permissionAttachment != null){
            permissionAttachment.remove();
            permissionAttachment = null;
        }
        permissionAttachment = new PermissionAttachment(instance, player);
        //Load permissions from GLOBAL
        for(String groupName : group.getGroupSet(instance.getGlobalPermServer()).getGroups()){
            Group g = instance.getGroupManager().getGroup(instance.getGlobalPermServer(), groupName);
            for(Permission permission : g.getGroupPermissions().getPermissions().values()){
                permissionAttachment.setPermission(permission.getPermission(), permission.isValue());
            }
        }
        //Load permissions from LOCAL
        for(String groupName : group.getGroupSet(instance.getPermServer()).getGroups()){
            Group g = instance.getGroupManager().getGroup(instance.getPermServer(), groupName);
            for(Permission permission : g.getGroupPermissions().getPermissions().values()){
                permissionAttachment.setPermission(permission.getPermission(), permission.isValue());
            }
        }
    }

}
