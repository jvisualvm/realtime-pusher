package com.risen.realtime.framework.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.realtime.framework.cache.PushLocationCache;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.mapper.PushLocationMapper;

import java.util.Optional;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 19:15
 */
public class TaskLocationService {

    public static int getCurrentLocation(String locationKey) {
        PushLocationCache locationCache = SystemCacheService.getSystemObj(PushLocationCache.class);
        if (!locationCache.containKey(locationKey)) {
            PushLocationEntity pushLocation = new PushLocationEntity();
            pushLocation.setLocationKey(locationKey);
            pushLocation.setStartKey(0);
            pushLocation.setTimestamp(String.valueOf(System.currentTimeMillis()));
            locationCache.put(locationKey, pushLocation);
            return 0;
        } else {
            PushLocationEntity pushLocation = locationCache.get(locationKey);
            pushLocation.setTimestamp(String.valueOf(System.currentTimeMillis()));
            locationCache.put(locationKey, pushLocation);
            return pushLocation.getStartKey();
        }
    }

    public static void updateLocation(String locationKey, Integer dataKey) {
        PushLocationCache locationCache = SystemCacheService.getSystemObj(PushLocationCache.class);
        PushLocationEntity pushLocation = locationCache.get(locationKey);
        pushLocation.setTimestamp(String.valueOf(System.currentTimeMillis()));
        pushLocation.setStartKey(dataKey);
        locationCache.put(locationKey, pushLocation);
    }

    public static void removeLocation(String locationKey) {
        Optional.ofNullable(locationKey).ifPresent(item -> {
            PushLocationCache locationCache = SystemCacheService.getSystemObj(PushLocationCache.class);
            locationCache.remove(locationKey);
            PushLocationMapper pushLocationMapper = SystemCacheService.getSystemObj(PushLocationMapper.class);
            LambdaQueryWrapper<PushLocationEntity> query = new LambdaQueryWrapper<>();
            query.eq(true, PushLocationEntity::getLocationKey, locationKey);
            pushLocationMapper.delete(query);
        });
    }


}
