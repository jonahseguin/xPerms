package com.shawckz.xperms.network;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by jonahseguin on 2016-08-13.
 */
public abstract class NetworkListener extends JedisPubSub {

    private final NetworkEvent networkEvent;

    public NetworkListener(NetworkEvent networkEvent) {
        this.networkEvent = networkEvent;
    }

    public NetworkEvent getNetworkEvent() {
        return networkEvent;
    }
}
