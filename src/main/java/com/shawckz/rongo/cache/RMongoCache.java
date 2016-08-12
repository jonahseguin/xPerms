package com.shawckz.rongo.cache;

import com.shawckz.rongo.Cacheable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by jonahseguin on 2016-08-11.
 */
public abstract class RMongoCache<K extends Cacheable> implements IRongoCache<String, K> {

    private final Datastore datastore;

    public RMongoCache(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public boolean inCache(String search) {
        Query<K> q = datastore.createQuery(getTypeClass());
        q.criteria(getQueryKey()).equalIgnoreCase(search);
        return !q.asList().isEmpty();
    }

    @Override
    public void cache(K cacheable) {
        datastore.save(cacheable);
    }

    @Override
    public void uncache(K cacheable) {
        // Not going to implement..
    }

    @Override
    public K get(String search) {
        Query<K> q = datastore.createQuery(getTypeClass());
        q.criteria(getQueryKey()).equalIgnoreCase(search);
        Stream<K> stream = q.asList().stream();
        Optional<K> opt = stream.findFirst();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public abstract Class<K> getTypeClass();

    public abstract String getQueryKey();


}
