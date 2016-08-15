package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.permissions.perms.Permission;

public class CmdGroupInfo implements GCommand {

    @GCmd(name = "xperms groupinfo", aliases = {"perms groupinfo"}, permission = "xperms.groupinfo",
            usage = "/xperms groupinfo <group>", minArgs = 1)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();
        Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(0));
        if (group != null) {
            sender.sendMessage("&9xPerms &7- &eGroup " + group.getName());
            sender.sendMessage("&ePrefix&7: " + group.getPrefix() + " [PREFIX]");
            sender.sendMessage("&eSuffix&7: " + group.getSuffix() + " [SUFFIX]");
            String permissions = "";
            for (Permission permission : group.getPermissions().getPermissions().values()) {
                permissions += "&7" + permission.toString() + "&8, ";
            }
            if (!permissions.isEmpty()) {
                permissions = permissions.substring(0, permissions.length() - 4);
            }
            sender.sendMessage("&ePermissions&7: " + permissions);
            String subGroups = "";
            for (Group g : group.getSubGroups().getSubGroups()) {
                subGroups += "&7" + g.getName() + "&8, ";
            }
            if (!subGroups.isEmpty()) {
                subGroups = subGroups.substring(0, subGroups.length() - 4);
            }
            sender.sendMessage("&eSub Groups&7: " + subGroups);

        } else {
            sender.sendMessage("&cGroup not found.");
        }
    }

}
