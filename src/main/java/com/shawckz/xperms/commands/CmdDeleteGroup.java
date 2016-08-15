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
import org.mongodb.morphia.query.Query;

public class CmdDeleteGroup implements GCommand {

    @GCmd(name = "xperms deletegroup", aliases = {"perms deletegroup"}, minArgs = 1,
            usage = "/xperms deletegroup <group>", permission = "xperms.deletegroup")
    public void onCmd(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        new BukkitRunnable() {
            @Override
            public void run() {
                Group group = XPerms.getInstance().getGroupManager().getGroup(args.getArg(0));
                if (group != null) {
                    XPerms.getInstance().getGroupManager().unregisterGroup(group);
                    Query<Group> q = XPerms.getInstance().getDatabaseManager().getDataStore().createQuery(Group.class);
                    q.criteria("name").equalIgnoreCase(group.getName());
                    XPerms.getInstance().getDatabaseManager().getDataStore().delete(q);
                    sender.sendMessage(ChatColor.GREEN + "Group deleted.");
                    XPerms.getInstance().getNetworkManager().callEvent(NetworkEvent.REFRESH_GROUPS);
                } else {
                    sender.sendMessage(ChatColor.RED + "That group does not exist.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());
    }

}
