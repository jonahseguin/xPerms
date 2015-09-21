package com.shawckz.xperms.profile;

import com.shawckz.xperms.Permissions;
import com.shawckz.xperms.database.mongo.annotations.CollectionName;
import com.shawckz.xperms.profile.internal.CachePlayer;
import lombok.Getter;
import lombok.Setter;

@CollectionName(name = "")
@Getter
@Setter
public class XProfile extends CachePlayer {

    //I would make these private, but due to how AutoMongo populates values I cannot :(
    private String uniqueId;
    private String name;

    public XProfile(Permissions permissions) {
        super(permissions);
    }

    public XProfile(Permissions permissions, String uniqueId, String name) {
        super(permissions);
        this.uniqueId = uniqueId;
        this.name = name;
    }

}
