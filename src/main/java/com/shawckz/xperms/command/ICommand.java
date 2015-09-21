package com.shawckz.xperms.command;

/**
 * Created by 360 on 5/30/2015.
 */


public interface ICommand {

    /**
     * Called when the command is processed by a CommandSender
     * @param cmdArgs The Command arguements ({@link CmdArgs})
     */
    void onCommand(CmdArgs cmdArgs);

}
