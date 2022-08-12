package com.risen.realtime.framework.service;

import com.risen.realtime.framework.cache.PushLocationCache;
import com.risen.realtime.framework.cache.PushSinkCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.PushUserCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 13:00
 */
@AllArgsConstructor
@Service
public class SystemCacheRefreshService {

    private PushSinkCache pushSinkCache;
    private PushTaskCache pushTaskCache;
    private PushLocationCache pushLocationCache;
    private PushUserCache pushUserCache;

    public void refreshSinkCache() {
        pushSinkCache.loadCache();
    }

    public void refreshTaskCache() {
        pushTaskCache.loadCache();
    }

    public void refreshLocationCache() {
        pushLocationCache.loadCache();
    }

    public void refreshUserCache() {
        pushUserCache.loadCache();
    }


}
