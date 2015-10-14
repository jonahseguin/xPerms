package com.shawckz.xperms.permissions.serial;

import com.shawckz.xperms.config.AbstractSerializer;
import com.shawckz.xperms.exception.PermissionsException;
import com.shawckz.xperms.permissions.PermServer;
import org.bson.Document;

/**
 * Created by 360 on 9/21/2015.
 */
public class PermServerSerializer extends AbstractSerializer<PermServer> {

    @Override
    public String toString(PermServer data) {
        return data.getName();
    }

    @Override
    public PermServer fromString(Object data) {
        if(data instanceof String){
            return new PermServer(((String)data));
        }
        throw new PermissionsException("Could not de-serialize PermServer (data not a String?)");
    }
}
