package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdAddPermission implements GCommand {

    @GCmd(name = "xperms addperm", aliases = {"perms addperm"}, permission = "xperms.addperm",
            usage = "/xperms addperm <group> <server> <perm>", minArgs = 3)
    public void onCmdAddGroup(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                PermServer server = XPerms.getInstance().getServerCache().getPermServer(args.getArg(1));
                if (server != null) {
                    Group group = XPerms.getInstance().getGroupManager().getGroup(server, args.getArg(0));
                    if (group != null) {
                        String perm = args.getArg(2);
                        if (!group.getGroupPermissions().hasPermission(perm)) {
                            group.getGroupPermissions().addPermission(perm);
                            XPerms.getInstance().getDatabaseManager().getDataStore().save(group);
                            XPerms.getInstance().getGroupManager().refreshPlayerPermissions();
                        } else {
                            sender.sendMessage(ChatColor.RED + "That group already has that permission.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "That group does not exist.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That server does not exist.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());


    }

}
