package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.mapper.PushSinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushSinkCache extends PermanentCacheAbstract<Integer, PushSinkEntity> {

    private PushSinkMapper pushSinkMapper;


    public PushSinkCache(PushSinkMapper pushSinkMapper) {
        super(10);
        this.pushSinkMapper = pushSinkMapper;
    }

    @Override
    public void loadCache() {
        List<PushSinkEntity> pushConfigEntities = pushSinkMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            pushConfigEntities.forEach(index -> {
                put(index.getId(), index);
            });
        }
    }

}
