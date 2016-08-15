package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;

public class CmdListServers implements GCommand {

    @GCmd(name = "xperms listservers", aliases = {"perms listservers"}, permission = "xperms.listservers",
            usage = "/xperms listservers")
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();

        sender.sendMessage("&9xPerms &7- &6Registered Servers");
        XPerms.getInstance().getServerCache().getServers().values().forEach(server ->
                sender.sendMessage("&7- &e" + server.getName())
        );
    }

}
