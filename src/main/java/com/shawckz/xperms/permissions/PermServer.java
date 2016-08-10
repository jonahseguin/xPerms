package com.shawckz.xperms.permissions;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.database.mongo.AutoMongo;
import com.shawckz.xperms.database.mongo.annotations.CollectionName;
import com.shawckz.xperms.database.mongo.annotations.MongoColumn;

/**
 * Created by 360 on 9/21/2015.
 */
@CollectionName(name = "xperms_servers")
public class PermServer extends AutoMongo {

    @MongoColumn(name = "name", identifier = true)
    private String name;

    public PermServer(XPerms instance, String name) {
        super(instance);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
