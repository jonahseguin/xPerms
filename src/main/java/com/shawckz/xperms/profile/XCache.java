package com.shawckz.xperms.profile;

import com.shawckz.xperms.Permissions;
import com.shawckz.xperms.profile.internal.AbstractCache;
import com.shawckz.xperms.profile.internal.CachePlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class XCache extends AbstractCache {

    private final Permissions instance;

    public XCache(Permissions plugin) {
        super(plugin, XProfile.class);
        this.instance = plugin;
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new XProfile(instance, name, uuid);
    }

    @Override
    public void init(Player player, CachePlayer cachePlayer) {

    }
}
