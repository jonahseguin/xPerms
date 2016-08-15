package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.permissions.PermServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdCreateServer implements GCommand {

    @GCmd(name = "xperms createserver", aliases = {"perms createserver"}, permission = "xperms.createserver",
            usage = "/xperms createserver <name>", minArgs = 1)
    public void onCmd(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        new BukkitRunnable() {
            @Override
            public void run() {
                String serverName = args.getArg(0);
                PermServer exists = XPerms.getInstance().getServerCache().getPermServer(serverName);
                if (exists == null) {
                    PermServer server = new PermServer(serverName);
                    XPerms.getInstance().getServerCache().savePermServer(server);
                    // .savePermServer also saves to database async.

                    sender.sendMessage(ChatColor.GREEN + "Server created.");

                    XPerms.getInstance().getNetworkManager().callEvent(NetworkEvent.REFRESH_SERVERS);
                } else {
                    sender.sendMessage(ChatColor.RED + "A server with that name already exists.");
                }
            }
        }.runTaskAsynchronously(XPerms.getInstance());


    }

}
