package com.shawckz.xperms.network;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public enum NetworkEvent {
    //TODO; Fill in redis pub/sub things for cross-server dynamic support
    ;

    private final String event;

    NetworkEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
