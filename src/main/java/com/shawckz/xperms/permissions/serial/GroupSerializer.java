package com.shawckz.xperms.permissions.serial;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.config.AbstractSerializer;
import com.shawckz.xperms.exception.PermissionsException;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.groups.SubGroups;
import com.shawckz.xperms.permissions.perms.Permissions;
import org.bson.Document;

/**
 * Created by 360 on 9/21/2015.
 */
public class GroupSerializer extends AbstractSerializer<Group> {

    private final PermServerSerializer permServerSerializer = new PermServerSerializer();

    @Override
    public String toString(Group data) {
        final String name = data.getName();
        final String prefix = data.getPrefix();
        final String suffix = data.getSuffix();
        final String server = permServerSerializer.toString(data.getPermServer());
        final String[] subGroups = data.getSubGroups().getSubGroupsAsString();
        final String[] permissions = data.getGroupPermissions().getPermissionsAsString();
        Document document = new Document("name", name)
                .append("prefix", prefix)
                .append("suffix", suffix)
                .append("subGroups", subGroups)
                .append("permissions", permissions)
                .append("server", server);
        return document.toJson();
    }

    @Override
    public Group fromString(Object data) {
        if(data instanceof String){
            Document document = Document.parse(((String) data));
            final String name = document.getString("name");
            final String prefix = document.getString("prefix");
            final String suffix = document.getString("suffix");
            final String[] subGroupsString = (String[]) document.get("subGroups");
            final String[] permissionsString = (String[]) document.get("permissions");
            final PermServer permServer = permServerSerializer.fromString(document.getString("server"));
            Permissions permissions = new Permissions();
            for(String perm : permissionsString){
                permissions.addPermission(perm);
            }
            SubGroups subGroups = new SubGroups(XPerms.getInstance());
            for(String g : subGroupsString){
                subGroups.addSubGroup(XPerms.getInstance().getGroupManager().getGroup(permServer, g));
            }
            Group group = new Group(XPerms.getInstance(), permServer, name, prefix, suffix, permissions, subGroups);
            subGroups.setSuperGroup(group);
            return group;
        }
        throw new PermissionsException("Error de-serializing Group (data not a String?)");
    }
}
