package com.shawckz.xperms.permissions;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by 360 on 9/21/2015.
 */
@Entity("xperms_servers")
public class PermServer {

    @Id
    private ObjectId id;

    private String name;

    public PermServer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ObjectId getId() {
        return id;
    }
}
