package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import com.shawckz.xperms.permissions.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdSetSuffix implements GCommand {

    @GCmd(name = "xperms setsuffix", aliases = {"perms setsuffix"}, permission = "xperms.setsuffix",
            usage = "/xperms setsuffix <group> <suffix>", minArgs = 2)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();
        new BukkitRunnable() {
            @Override
            public void run() {
                Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(0));
                if (group != null) {
                    group.setSuffix(args.getArg(1));
                    XPerms.getInstance().getDatabaseManager().getDataStore().save(group);
                    sender.sendMessage(ChatColor.GREEN + "Group suffix set to '" + group.getSuffix() + "'.");
                } else {
                    sender.sendMessage("&cGroup not found.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

}
