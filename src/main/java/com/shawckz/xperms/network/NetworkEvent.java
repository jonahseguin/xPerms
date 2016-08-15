package com.shawckz.xperms.network;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public enum NetworkEvent {

    REFRESH_GROUPS("xPerms:refreshGroups"),
    RELOAD_PLAYERS("xPerms:reloadPlayers"),
    RELOAD_PLAYER("xPerms:reloadPlayer"),
    REFRESH_SERVERS("xPerms:refreshServers"),
    BROADCAST("xPerms:broadcast");

    private final String event;

    NetworkEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
