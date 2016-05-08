package com.shawckz.xperms.commands;

import com.shawckz.xperms.command.GCmd;
import com.shawckz.xperms.command.GCmdArgs;
import com.shawckz.xperms.command.GCommand;


public class CmdXPerms implements GCommand {

    @GCmd(name = "xperms", aliases = {"perms", "perm", "permissions", "xperm", "xpermissions"}, permission = "xperms.use")
    public void onCommand(GCmdArgs args) {
        
    }

}
