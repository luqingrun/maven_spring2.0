package com.gongsibao.util.cache.impl;

import com.danga.MemCached.MemCachedClient;
import com.gongsibao.util.cache.CacheService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * Created by luqingrun on 16/3/29.
 */
@Service("cacheService")
public class MemCacheServiceImpl implements CacheService, InitializingBean {

    private MemCachedClient client = new MemCachedClient();

    @Override
    public boolean put(String key, Object value, int expireSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, expireSecond);
        return client.set(key, value, calendar.getTime());
    }

    @Override
    public Object get(String key) {
        return client.get(key);
    }

    @Override
    public Object delete(String key) {
        Object object = get(key);
        client.delete(key);
        return object;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client.setDefaultEncoding("utf-8");
    }
}
