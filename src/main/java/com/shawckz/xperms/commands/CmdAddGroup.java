package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.profile.XProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdAddGroup implements GCommand {

    @GCmd(name = "xperms addgroup", aliases = {"perms addgroup"}, permission = "xperms.addgroup",
            usage = "/xperms addgroup <player> <server> <group>", minArgs = 3)
    public void onCmd(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                XProfile profile = XPerms.getInstance().getCache().lookupProfile(args.getArg(0));
                if (profile != null) {
                    String permServerString = args.getArg(1);
                    if (XPerms.getInstance().getServerCache().hasPermServer(permServerString)) {
                        PermServer permServer = XPerms.getInstance().getServerCache().getPermServer(permServerString);
                        Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(2));
                        if (group != null) {
                            if (!profile.getGroups().getGroupSet(permServer).hasGroup(group)) {
                                profile.getGroups().getGroupSet(permServer).saveGroup(group);
                                profile.refreshPermissions();
                                profile.saveProfile();
                                sender.sendMessage(ChatColor.GREEN + "Added group " + group.getName() + " to " + profile.getName()
                                        + " for server " + permServer.getName() + ".");
                            } else {
                                sender.sendMessage(ChatColor.RED + "That player already has that group for that server.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "That group does not exist.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Perm Server not found in cache.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found in database.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());


    }

}
