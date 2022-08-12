package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.common.util.PredicateUtil;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.mapper.PushTaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushTaskCache extends PermanentCacheAbstract<String, PushTaskEntity> {

    private PushTaskMapper pushTaskMapper;
    private PushSinkCache sinkCache;


    public PushTaskCache(PushTaskMapper pushTaskMapper, PushSinkCache sinkCache) {
        super(4);
        this.pushTaskMapper = pushTaskMapper;
        this.sinkCache = sinkCache;
    }

    @Override
    public void loadCache() {
        List<PushTaskEntity> pushConfigEntities = pushTaskMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            List<PushTaskEntity> pushList = pushConfigEntities.stream().filter(s -> StringUtils.isNotEmpty(s.getTaskKey())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pushList)) {
                pushList.forEach(item -> {
                    put(item.getTaskKey(), item);
                });
            }
        }
    }


    public PushSinkEntity getTaskSink(String taskKey) {
        PushTaskEntity pushTask = this.get(taskKey);
        PushSinkEntity pushSink = null;
        if (PredicateUtil.isNotEmpty(pushTask) && PredicateUtil.isNotEmpty(pushTask.getTargetId())) {
            pushSink = sinkCache.get(pushTask.getTargetId());
        }
        return pushSink;
    }

}
