package com.shawckz.xperms.commands;

import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class CmdXPerms implements GCommand {

    @GCmd(name = "xperms", aliases = {"perms"}, permission = "xperms.use")
    public void onCommand(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        msg(sender, "&7*** &9xPerms &7****");
        msg(sender, "&eDeveloped by Shawckz");
        msg(sender, "&7--> &6Commands");
        msg(sender, "&7/xperms createserver <name>");
        msg(sender, "&7/xperms listservers");
        msg(sender, "&7/xperms listgroup");
        msg(sender, "&7/xperms showgroups <player> [server|(blank for ALL)]");
        msg(sender, "&7/xperms addgroup <player> <server> <group>");
        msg(sender, "&7/xperms removegroup <player> <server> <group>");
        msg(sender, "&7/xperms addperm <group> <server> <perm>");
        msg(sender, "&7/xperms removeperm <group> <server> <perm>");
        msg(sender, "&7/xperms addsubgroup <group> <server> <subgroup> ");
        msg(sender, "&7/xperms removesubgroup <group> <server> <subgroup> ");
        msg(sender, "&7/xperms creategroup <name>");
        msg(sender, "&7/xperms deletegroup <name>");
        msg(sender, "&7/xperms groupinfo <group>");
        msg(sender, "&7/xperms setprefix <group> <prefix>");
        msg(sender, "&7/xperms setsuffix <group> <suffix>");
        msg(sender, "&7/xperms testperm <player>");
    }

    private void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
