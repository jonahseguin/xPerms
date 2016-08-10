package com.shawckz.xperms.config.exceptions;


public class NotASerializerException extends Exception {

    public NotASerializerException() {
        super("Config given an object which doesn't extend serializer");
    }
}
