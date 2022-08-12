package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.realtime.framework.entity.PushSourceEntity;
import com.risen.realtime.framework.mapper.PushSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangxin
 * @since 2022-07-20
 */
@Component
public class PushSourceCache extends PermanentCacheAbstract<String, PushSourceEntity> {


    private PushSourceMapper pushSourceMapper;


    public PushSourceCache(PushSourceMapper pushSourceMapper) {
        super(10);
        this.pushSourceMapper=pushSourceMapper;
    }

    @Override
    public void loadCache() {
        List<PushSourceEntity> pushConfigEntities = pushSourceMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            pushConfigEntities.forEach(index -> {
                put(index.getTaskKey(), index);
            });
        }
    }


}
