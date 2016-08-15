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
        msg(sender, "&eDeveloped by Shawckz - https://shawckz.com");
        msg(sender, "&7--> &6Commands");

        msg(sender, "&7/xperms createserver <name>"); // Done
        msg(sender, "&7/xperms listservers"); // Done
        msg(sender, "&7/xperms listgroups"); // Done
        msg(sender, "&7/xperms showgroups <player> [server|ALL]"); // Done
        msg(sender, "&7/xperms addgroup <player> <server> <group>"); // Done
        msg(sender, "&7/xperms removegroup <player> <server> <group>"); // Done
        msg(sender, "&7/xperms addperm <group> <server> <perm>"); // Done
        msg(sender, "&7/xperms removeperm <group> <server> <perm>"); // Done
        msg(sender, "&7/xperms addsubgroup <group> <server> <subgroup> "); // Done
        msg(sender, "&7/xperms removesubgroup <group> <server> <subgroup> "); // Done
        msg(sender, "&7/xperms creategroup <server> <name>"); // Done
        msg(sender, "&7/xperms deletegroup <name>"); // Done
        msg(sender, "&7/xperms groupinfo <group>"); // Done
        msg(sender, "&7/xperms setprefix <group> <prefix>"); // Done
        msg(sender, "&7/xperms setsuffix <group> <suffix>"); // Done
        msg(sender, "&7/xperms testperm <player> <perm>"); // Done
    }

    private void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

}
