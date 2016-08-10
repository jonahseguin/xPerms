package com.shawckz.xperms.permissions.serial;

import com.mongodb.util.JSON;
import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.config.AbstractSerializer;
import com.shawckz.xperms.exception.PermissionsException;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.profile.ProfileGroupSet;
import com.shawckz.xperms.permissions.groups.profile.XProfileGroupSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 360 on 9/21/2015.
 */
public class ProfileGroupSerializer extends AbstractSerializer<ProfileGroupSet> {

    private final PermServerSerializer permServerSerializer = new PermServerSerializer();

    @Override
    public String toString(ProfileGroupSet data) {
        Map<String, String> result = new HashMap<>();
        for (PermServer server : data.getGroups().keySet()) {
            String key = permServerSerializer.toString(server);
            XProfileGroupSet xProfileGroupSet = data.getGroupSet(server);
            String value = JSON.serialize(xProfileGroupSet.getGroups());
            result.put(key, value);
        }
        return JSON.serialize(result);
    }

    @Override
    public ProfileGroupSet fromString(Object data) {
        if (data instanceof String) {
            Object parse = JSON.parse(((String) data));
            Map<String, String> val = (Map<String, String>) parse;
            Map<PermServer, XProfileGroupSet> result = new HashMap<>();
            for (String key : val.keySet()) {
                PermServer server = permServerSerializer.fromString(key);
                Set<String> groups = (Set<String>) JSON.parse(val.get(key));
                XProfileGroupSet xProfileGroupSet = new XProfileGroupSet(XPerms.getInstance(), server, groups);
                result.put(server, xProfileGroupSet);
            }
            return new ProfileGroupSet(XPerms.getInstance(), result);
        }
        throw new PermissionsException("Could not de-serialize ProfileGroup (data not a String?)");
    }
}
