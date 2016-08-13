package com.shawckz.xperms.profile;

import org.bukkit.Bukkit;

public class FailedCachedProfile {

    private final String username;
    private final String uniqueId;

    public FailedCachedProfile(String username, String uniqueId) {
        this.username = username;
        this.uniqueId = uniqueId;
    }

    public boolean isOnline() {
        return Bukkit.getPlayerExact(username) != null;
    }

    public String getUsername() {
        return username;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
