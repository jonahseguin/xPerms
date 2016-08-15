package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.profile.ProfileGroupSet;
import com.shawckz.xperms.profile.XProfile;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdShowGroups implements GCommand {

    @GCmd(name = "xperms showgroups", aliases = {"perms showgroups"}, permission = "xperms.showgroups",
            usage = "/xperms showgroups <player> [server|ALL]", minArgs = 2)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                XProfile profile = XPerms.getInstance().getCache().lookupProfile(args.getArg(0));
                if (profile != null) {
                    String permServerString = args.getArg(1);
                    if (permServerString.equalsIgnoreCase("ALL")) {
                        sender.sendMessage("&9xPerms &7- &e" + profile.getName() + " &7- &eGroups for all servers");
                        profile.getGroups().getGroups().entrySet().forEach(set -> {
                            PermServer permServer = set.getKey();
                            ProfileGroupSet groupSet = set.getValue();
                            sender.sendMessage("&8--> &6" + permServer.getName());
                            groupSet.getGroups().forEach(group ->
                                    sender.sendMessage("&7- &a" + group));
                        });
                    } else {
                        if (XPerms.getInstance().getServerCache().hasPermServer(permServerString)) {
                            PermServer permServer = XPerms.getInstance().getServerCache().getPermServer(permServerString);
                            ProfileGroupSet groupSet = profile.getGroups().getGroupSet(permServer);
                            sender.sendMessage("&9xPerms &7- &e" + profile.getName() + " &7- &eGroups server '" + permServer.getName() + "'");
                            groupSet.getGroups().forEach(group ->
                                    sender.sendMessage("&7- &a" + group));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Perm Server not found in cache.");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found in database.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

}
