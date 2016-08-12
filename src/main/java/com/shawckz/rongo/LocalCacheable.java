package com.shawckz.rongo;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public class LocalCacheable<T extends Cacheable> {

    private final T cacheable;
    private long cacheTime;

    public LocalCacheable(T cacheable, long cacheTime) {
        this.cacheable = cacheable;
        this.cacheTime = cacheTime;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public T getCacheable() {
        return cacheable;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

}
