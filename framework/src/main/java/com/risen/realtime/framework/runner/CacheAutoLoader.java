package com.risen.realtime.framework.runner;

import com.risen.common.thread.ThreadPoolUtil;
import com.risen.common.util.ForeachUtil;
import com.risen.realtime.framework.cache.PermanentCacheAbstract;
import com.risen.realtime.framework.cache.SystemObjectCache;
import com.risen.realtime.framework.mapper.PushLocationMapper;
import com.risen.realtime.framework.monitor.BaseDirectoryFilter;
import com.risen.realtime.framework.monitor.BaseFileListener;
import com.risen.realtime.framework.util.ServiceUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:38
 */
@Component
@Order(value = 5)
@AllArgsConstructor
public class CacheAutoLoader implements CommandLineRunner {

    private SystemObjectCache systemObjectCache;
    private PushLocationMapper pushLocationMapper;
    private ServiceUtil serviceUtil;
    private ThreadPoolUtil threadPoolUtil;

    @Override
    public void run(String... args) throws Exception {

        Consumer<PermanentCacheAbstract> permanentCacheInitConsumer = t -> {
            t.loadCache();
        };
        ForeachUtil.forEachOne(PermanentCacheAbstract.getImplTree(), permanentCacheInitConsumer);

        Consumer<PermanentCacheAbstract> permanentCacheAbstractConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(PermanentCacheAbstract.getImplTree(), permanentCacheAbstractConsumer);

        Consumer<BaseFileListener> baseFileListenerConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(BaseFileListener.getImplTree(), baseFileListenerConsumer);


        Consumer<BaseDirectoryFilter> baseDirectoryFilterConsumer = (t) -> {
            systemObjectCache.put(t.getClass().hashCode(), t);
        };
        ForeachUtil.forEachOne(BaseDirectoryFilter.getImplTree(), baseDirectoryFilterConsumer);

        systemObjectCache.put(PushLocationMapper.class.hashCode(), pushLocationMapper);

        systemObjectCache.put(ServiceUtil.class.hashCode(), serviceUtil);

        systemObjectCache.put(ThreadPoolUtil.class.hashCode(), threadPoolUtil);

    }


}
