package com.shawckz.xperms.permissions.perms;

import com.shawckz.xperms.exception.PermissionsException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 360 on 9/21/2015.
 */
@AllArgsConstructor//Set<String> constructor
@RequiredArgsConstructor//Empty constructor
@Embedded
public class Permissions {

    @Embedded
    private Map<String, Permission> permissions = new HashMap<>();

    public void addPermission(String permission) {
        String onlyPerm;
        if (permission.startsWith("-")) {
            onlyPerm = permission.substring(1, permission.length());
        } else {
            onlyPerm = permission;
        }
        if (onlyPerm.equals("")) {
            throw new PermissionsException("Cannot add empty permission");
        }
        setPermission(onlyPerm, !permission.startsWith("-"));
    }

    public void setPermission(String permission, boolean value) {
        if (containsPermission(permission)) {
            Permission perm = getPermission(permission);
            perm.setValue(value);
        } else {
            permissions.put(permission, new Permission(permission, value));
        }
    }

    public void setPermission(Permission permission) {
        permissions.put(permission.getPermission(), permission);
    }

    public boolean containsPermission(String permission) {
        return permissions.containsKey(permission);
    }

    public boolean hasPermission(String permission) {
        Permission perm = getPermission(permission);
        return perm != null && perm.isValue();
    }

    public boolean hasPermission(Permission permission) {
        return containsPermission(permission) && permission.isValue();
    }

    public boolean containsPermission(Permission permission) {
        return permissions.containsKey(permission.getPermission());
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission.getPermission());
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    public Permission getPermission(String permission) {
        return permissions.get(permission);
    }

    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    //Will return the permissions as string (starting with '-' if the permission's value is false(negated))
    public String[] getPermissionsAsString() {
        String[] arr = new String[permissions.size()];
        int x = 0;
        for (Permission permission : getPermissions().values()) {
            arr[x] = permission.toString();
            x++;
        }
        return arr;
    }
}
