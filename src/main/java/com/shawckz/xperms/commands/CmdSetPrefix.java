package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import com.shawckz.xperms.permissions.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdSetPrefix implements GCommand {

    @GCmd(name = "xperms setprefix", aliases = {"perms setprefix"}, permission = "xperms.setprefix",
            usage = "/xperms setprefix <group> <prefix>", minArgs = 2)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();
        new BukkitRunnable() {
            @Override
            public void run() {
                Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(0));
                if (group != null) {
                    group.setPrefix(args.getArg(1));
                    XPerms.getInstance().getDatabaseManager().getDataStore().save(group);
                    sender.sendMessage(ChatColor.GREEN + "Group prefix set to '" + group.getPrefix() + "'.");
                } else {
                    sender.sendMessage("&cGroup not found.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

}
