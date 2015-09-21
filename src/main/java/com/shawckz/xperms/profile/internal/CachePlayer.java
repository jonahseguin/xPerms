package com.shawckz.xperms.profile.internal;


/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */


import com.shawckz.xperms.Permissions;
import com.shawckz.xperms.database.mongo.AutoMongo;

import org.bukkit.permissions.Permission;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class CachePlayer extends AutoMongo {

    public CachePlayer(Permissions permissions) {
        super(permissions);
    }

    public abstract String getName();

    public abstract String getUniqueId();

}
