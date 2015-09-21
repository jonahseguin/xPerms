package com.shawckz.xperms.config;

import com.shawckz.xperms.config.annotations.ConfigData;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.plugin.Plugin;

@Getter
@Setter
public class XConfig extends Configuration {

    public XConfig(Plugin plugin) {
        super(plugin);
    }

    @ConfigData("server-name") private String server;

}
