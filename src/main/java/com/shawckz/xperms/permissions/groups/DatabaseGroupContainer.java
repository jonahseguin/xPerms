package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.database.mongo.AutoMongo;
import com.shawckz.xperms.database.mongo.annotations.CollectionName;
import com.shawckz.xperms.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.xperms.database.mongo.annotations.MongoColumn;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.serial.GroupSerializer;
import com.shawckz.xperms.permissions.serial.PermServerSerializer;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 360 on 9/21/2015.
 */
@CollectionName(name = "xpermgroups")
@Getter
@Setter
public class DatabaseGroupContainer extends AutoMongo {

    @MongoColumn(name = "groupName", identifier = true)
    private String groupName;

    @MongoColumn(name = "server", identifier = true)
    @DatabaseSerializer(serializer = PermServerSerializer.class)
    private PermServer permServer;

    @MongoColumn(name = "groups")
    @DatabaseSerializer(serializer = GroupSerializer.class)
    private Group group;

    public DatabaseGroupContainer(XPerms instance) {
        super(instance);
    }

    public DatabaseGroupContainer(XPerms instance, PermServer permServer, Group group) {
        super(instance);
        this.permServer = permServer;
        this.groupName = group.getName();
        this.group = group;
    }

}
