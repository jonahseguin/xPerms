package com.shawckz.rongo;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public interface Cacheable {

    String getIdentifier();

    String getDatabaseIdentifier();

    String toJSON();

}
