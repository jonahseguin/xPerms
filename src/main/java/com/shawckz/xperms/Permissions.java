package com.shawckz.xperms;

import com.shawckz.xperms.config.XConfig;
import com.shawckz.xperms.database.DatabaseManager;
import lombok.Getter;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Permissions extends JavaPlugin {

    private XConfig config;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable(){
        this.config = new XConfig(this);
        this.config.load();
        this.config.save();
        this.databaseManager = new DatabaseManager(this);
    }

    @Override
    public void onDisable(){

    }

    public XConfig getXConfig() {
        return config;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
