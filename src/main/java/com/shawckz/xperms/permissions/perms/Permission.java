package com.shawckz.xperms.permissions.perms;

import lombok.Getter;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by 360 on 9/21/2015.
 */
@Getter
@Embedded
public class Permission {

    private String permission;
    private boolean value;

    public Permission() {
        //Empty constructor
    }

    public Permission(String permission, boolean value) {
        this.permission = permission;
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (!value ? "-" : "") + permission;
    }

    public static Permission fromString(String permission) {
        String onlyPerm;
        if (permission.startsWith("-")) {
            onlyPerm = permission.substring(1, permission.length());
        } else {
            onlyPerm = permission;
        }
        return new Permission(onlyPerm, !permission.startsWith("-"));
    }

}
