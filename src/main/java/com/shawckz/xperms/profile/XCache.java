package com.shawckz.xperms.profile;

import com.shawckz.xperms.XPerms;
import com.shawckz.xperms.profile.internal.AbstractCache;
import com.shawckz.xperms.profile.internal.CachePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class XCache extends AbstractCache {

    private final XPerms instance;

    public XCache(XPerms plugin) {
        super(plugin, XProfile.class);
        this.instance = plugin;
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new XProfile(instance, name, uuid);
    }

    @Override
    public void init(Player player, CachePlayer cachePlayer) {
        if (cachePlayer instanceof XProfile) {
            XProfile profile = (XProfile) cachePlayer;
            profile.refreshPermissions();
        }
    }

    public XProfile lookupProfile(String name) {
        Player targetPlayer = Bukkit.getPlayer(name);
        if (targetPlayer != null) {
            name = targetPlayer.getName();
        }
        return getProfile(name);
    }

    public XProfile getProfile(String name) {
        return (XProfile) super.getBasePlayer(name);
    }

    public XProfile getProfile(Player player) {
        return (XProfile) super.getBasePlayer(player);
    }

}
