package com.shawckz.xperms.commands;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;

public class CmdListGroups implements GCommand {

    @GCmd(name = "xperms listgroups", aliases = {"perms listgroups"}, permission = "xperms.listgroups",
            usage = "/xperms listgroups")
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();

        sender.sendMessage("&9xPerms &7- &6Registered Groups");
        XPerms.getInstance().getGroupManager().getGroupSet().getGroups().values().forEach(group -> {
            sender.sendMessage("&7- &e" + group.getName());
            sender.sendMessage("    &7- Prefix: " + group.getPrefix() + " [Prefix]");
            sender.sendMessage("    &7- Suffix: " + group.getSuffix() + " [Suffix]");
        });
    }

}
