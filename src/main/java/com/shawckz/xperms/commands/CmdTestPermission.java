package com.shawckz.xperms.commands;

import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;
import com.shawckz.xperms.command.GCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdTestPermission implements GCommand {

    @GCmd(name = "xperms testperm", aliases = {"perms testperm"}, permission = "xperms.testperm",
            usage = "/xperms testperm <player> <perm>", minArgs = 2)
    public void onCmd(final GCmdArgs args) {
        final GCommandSender sender = args.getSender();
        Player target = Bukkit.getPlayer(args.getArg(0));
        String perm = args.getArg(1);
        if (target != null) {
            String hasPerm = (target.hasPermission(perm) ? "&aYES" : "&cNO");
            sender.sendMessage("&e" + target.getName() + " has permission " + perm + "&7: " + hasPerm);
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found.");
        }


    }

}
