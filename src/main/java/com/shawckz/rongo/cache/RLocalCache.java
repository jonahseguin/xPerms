package com.shawckz.rongo.cache;

import com.shawckz.rongo.Cacheable;
import com.shawckz.rongo.LocalCacheable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public class RLocalCache<K extends Cacheable> implements IRongoCache<String, K> {

    private final ConcurrentMap<String, LocalCacheable<K>> cache = new ConcurrentHashMap<>();

    @Override
    public boolean inCache(String search) {
        return cache.containsKey(search.toLowerCase());
    }

    @Override
    public void cache(K cacheable) {
        cache.put(cacheable.getIdentifier().toLowerCase(), new LocalCacheable<>(cacheable, System.currentTimeMillis()));
    }

    @Override
    public void uncache(K cacheable) {
        cache.remove(cacheable.getIdentifier().toLowerCase());
    }

    @Override
    public K get(String search) {
        if (inCache(search)) {
            return cache.get(search).getCacheable();
        }
        return null;
    }
}
