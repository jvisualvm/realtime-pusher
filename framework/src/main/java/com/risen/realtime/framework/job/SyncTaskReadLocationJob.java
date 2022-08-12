package com.risen.realtime.framework.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushLocationCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.mapper.PushLocationMapper;
import com.risen.realtime.framework.service.SystemCacheService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 11:21
 */
@Component
@DisallowConcurrentExecution
public class SyncTaskReadLocationJob extends DataJobManager {


    private static final long expireTime = 6L*30L * 24 * 60 * 60 * 1000;

    @Override
    public String taskKey() {
        return "risen:data:location:refresh";
    }

    @Override
    public JobTypeEnum taskType() {
        return JobTypeEnum.SYSTEM;
    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //定期同步到数据库
        List<DataJobManager> dataJobManagerList = DataJobManager.getImplTree();
        if (!CollectionUtils.isEmpty(dataJobManagerList)) {
            dataJobManagerList.forEach(item -> {
                executeUpdateOrInsert(item.taskKey());
            });
        }
    }

    private void executeUpdateOrInsert(String taskKey) {
        PushLocationCache pushLocationCache = SystemCacheService.getSystemObj(PushLocationCache.class);
        Map<String, PushLocationEntity> history = pushLocationCache.getAllCacheValue();
        if (PredicateUtil.isNotEmpty(history)) {
            history.forEach((k, v) -> {
                if (k.startsWith(taskKey)) {
                    updateOrInsertLocation(k, v);
                }
            });
            deleteExpiredLocation(history);
        }
    }

    private void updateOrInsertLocation(String locationKey, PushLocationEntity locCache) {
        Optional.ofNullable(locationKey).ifPresent(item -> {
            PushLocationMapper locationMapper = SystemCacheService.getSystemObj(PushLocationMapper.class);
            LambdaQueryWrapper<PushLocationEntity> query = new LambdaQueryWrapper<>();
            query.eq(true, PushLocationEntity::getLocationKey, item);
            Long total = locationMapper.selectCount(query);
            if (total <= 0) {
                PushLocationEntity pushLocation = new PushLocationEntity();
                pushLocation.setLocationKey(locationKey);
                pushLocation.setStartKey(locCache.getStartKey());
                pushLocation.setTimestamp(String.valueOf(System.currentTimeMillis()));
                locationMapper.insert(pushLocation);
            } else {
                PushLocationEntity pushLocation = new PushLocationEntity();
                pushLocation.setStartKey(locCache.getStartKey());
                pushLocation.setLocationKey(locationKey);
                pushLocation.setTimestamp(String.valueOf(System.currentTimeMillis()));
                locationMapper.update(pushLocation, query);
            }
        });
    }

    private void deleteExpiredLocation(Map<String, PushLocationEntity> history) {
        PushLocationMapper locationMapper = SystemCacheService.getSystemObj(PushLocationMapper.class);
        PushLocationCache pushLocationCache = SystemCacheService.getSystemObj(PushLocationCache.class);
        history.forEach((k, v) -> {
            LogUtil.info("currentTime:{},timestamp:{},expiredTime:{}", System.currentTimeMillis(), Long.valueOf(v.getTimestamp()), expireTime);
            if ((System.currentTimeMillis() - Long.valueOf(v.getTimestamp())) > expireTime) {
                LambdaQueryWrapper<PushLocationEntity> query = new LambdaQueryWrapper<>();
                query.eq(true, PushLocationEntity::getLocationKey, k);
                LogUtil.info("删除过期数据：{}", k);
                locationMapper.delete(query);
                pushLocationCache.remove(k);
            }
        });
    }

}
