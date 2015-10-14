package com.shawckz.xperms.permissions.perms;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by 360 on 9/21/2015.
 */
@Data
public class Permission {

    private final String permission;
    @NonNull private boolean value;

    @Override
    public String toString(){
        return (!value ? "-" : "") + permission;
    }

    public static Permission fromString(String permission){
        String onlyPerm;
        if(permission.startsWith("-")){
            onlyPerm = permission.substring(1, permission.length());
        }
        else{
            onlyPerm = permission;
        }
        return new Permission(onlyPerm, !permission.startsWith("-"));
    }

}
