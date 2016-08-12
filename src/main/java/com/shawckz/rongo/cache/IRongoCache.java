package com.shawckz.rongo.cache;

import com.shawckz.rongo.Cacheable;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public interface IRongoCache<K, T extends Cacheable> {

    boolean inCache(K search);

    void cache(T cacheable);

    void uncache(T cacheable);

    T get(K search);


}
