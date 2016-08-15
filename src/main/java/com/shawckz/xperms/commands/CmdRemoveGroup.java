package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import com.shawckz.xperms.profile.XProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdRemoveGroup implements GCommand {

    @GCmd(name = "xperms removegroup", aliases = {"perms removegroup"}, permission = "xperms.removegroup",
            usage = "/xperms removegroup <player> <server> <group>", minArgs = 3)
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
                        if (profile.getGroups().getGroupSet(permServer).hasGroup(group)) {
                            profile.getGroups().getGroupSet(permServer).removeGroup(group);
                            profile.refreshPermissions();
                            profile.saveProfile();
                            sender.sendMessage(ChatColor.GREEN + "Removed group " + group.getName() + " from " + profile.getName()
                                    + " for server " + permServer.getName() + ".");
                            XPerms.getInstance().getNetworkManager().callEvent(NetworkEvent.REFRESH_GROUPS);
                        } else {
                            sender.sendMessage(ChatColor.RED + "That player does not have that group for that server.");
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
