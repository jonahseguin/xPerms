package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.permissions.PermServer;
import com.shawckz.xperms.permissions.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdAddSubgroup implements GCommand {

    @GCmd(name = "xperms addsubgroup", aliases = {"perms addsubgroup"}, permission = "xperms.addsubgroup",
            usage = "/xperms addsubgroup <group> <server> <subgroup>", minArgs = 3)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(0));
                if (group != null) {
                    PermServer permServer = XPerms.getInstance().getServerCache().getPermServer(args.getArg(1));
                    if (permServer != null) {
                        Group subGroup = XPerms.getInstance().getGroupManager().getGroup(args.getArg(2));
                        if (subGroup != null) {
                            group.getSubGroups().addSubGroup(subGroup);
                            XPerms.getInstance().getDatabaseManager().getDataStore().save(group);
                            XPerms.getInstance().getNetworkManager().callEvent(NetworkEvent.REFRESH_GROUPS);
                            sender.sendMessage("&aSubGroup added to " + group.getName() + ".");
                        } else {
                            sender.sendMessage("&cThat (subgroup) group does not exist.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Perm Server not found in cache.");
                    }
                } else {
                    sender.sendMessage("&cThat group does not exist.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

}
