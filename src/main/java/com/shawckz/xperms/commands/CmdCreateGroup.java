package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.permissions.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdCreateGroup implements GCommand {

    @GCmd(name = "xperms creategroup", aliases = {"perms creategroup"}, permission = "xperms.creategroup",
            usage = "/xperms creategroup <name>", minArgs = 1)
    public void onCmd(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                Group group = new Group(XPerms.getInstance(), args.getArg(1));

                XPerms.getInstance().getGroupManager().registerGroup(group);

                XPerms.getInstance().getDatabaseManager().getDataStore().save(group);

                sender.sendMessage(ChatColor.GREEN + "Created group '" + group.getName() + "'.");
                XPerms.getInstance().getNetworkManager().callEvent(NetworkEvent.REFRESH_GROUPS);

            }
        }.runTaskAsynchronously(XPerms.getInstance());


    }

}
