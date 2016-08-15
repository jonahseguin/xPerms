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

    @ConfigData("server-name")
    private String server = "default";

    @ConfigData("profile-cache.async-caching")
    private boolean asyncCaching = false;

    @ConfigData("profile-cache.fail-retry-interval-seconds")
    private int cacheFailIntervalSeconds = 180; // 3 minutes

    @ConfigData("profile-cache.failure-handling")
    private boolean cacheFailureHandling = true;

    @ConfigData("chat.placeholder.prefix")
    private String placeholderPrefix = "{PREFIX}";

    @ConfigData("chat.placeholder.suffix")
    private String placeholderSuffix = "{SUFFIX}";

}
