package com.shawckz.xperms.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by jonahseguin on 2016-08-12.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CachedProfile {

    private final XProfile profile;
    private final long initialCacheTime;
    private long expiry;

}
