package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.realtime.framework.entity.PushUserEntity;
import com.risen.realtime.framework.mapper.PushUserMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushUserCache extends PermanentCacheAbstract<String, String> {

    private PushUserMapper pushUserMapper;

    public PushUserCache(PushUserMapper pushUserMapper) {
        super(4);
        this.pushUserMapper = pushUserMapper;
    }

    @Override
    public void loadCache() {
        List<PushUserEntity> pushConfigEntities = pushUserMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(pushConfigEntities)) {
            pushConfigEntities.forEach(index -> {
                put(index.getUserName(), index.getPassword());
            });
        }
    }

}
