package com.shawckz.xperms.network.listeners;

import com.mongodb.BasicDBObject;
import com.shawckz.xperms.network.NetworkEvent;
import com.shawckz.xperms.network.NetworkListener;
import com.shawckz.xperms.network.XNetworkManager;

/**
 * Created by jonahseguin on 2016-08-13.
 */
public class ListenerBroadcast extends NetworkListener {

    public ListenerBroadcast() {
        super(NetworkEvent.BROADCAST);
    }

    @Override
    public void onMessage(String channel, String message) {
        BasicDBObject o = BasicDBObject.parse(message);
        String msg = o.getString("message");
        if (channel.equalsIgnoreCase(getNetworkEvent().getEvent())) {
            XNetworkManager.staffMessage(msg);
        }
    }
}
