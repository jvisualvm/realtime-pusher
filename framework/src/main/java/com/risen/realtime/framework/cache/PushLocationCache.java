package com.risen.realtime.framework.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.realtime.framework.entity.PushLocationEntity;
import com.risen.realtime.framework.mapper.PushLocationMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 12:47
 */
@Component
public class PushLocationCache extends PermanentCacheAbstract<String, PushLocationEntity> {

    private PushLocationMapper pushLocationMapper;

    public PushLocationCache(PushLocationMapper pushLocationMapper) {
        super(10);
        this.pushLocationMapper = pushLocationMapper;
    }

    @Override
    public void loadCache() {
        List<PushLocationEntity> result = pushLocationMapper.selectList(new LambdaQueryWrapper<>());
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        result.forEach(s -> {
            put(s.getLocationKey(), s);
        });
    }
}
