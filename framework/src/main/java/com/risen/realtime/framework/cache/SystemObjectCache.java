package com.risen.realtime.framework.cache;

import com.risen.common.thread.ThreadPoolUtil;
import com.risen.realtime.framework.manager.JobManager;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 13:22
 */
@Component
public class SystemObjectCache extends PermanentCacheAbstract<Object, Object> {

    private ThreadPoolUtil threadPoolUtil;
    private JobManager jobManager;

    public SystemObjectCache(ThreadPoolUtil threadPoolUtil, JobManager jobManager) {
        super(null);
        this.threadPoolUtil = threadPoolUtil;
        this.jobManager = jobManager;
    }

    /**
     * 将对象缓存起来
     */
    @Override
    public void loadCache() {
        put(ThreadPoolUtil.class.hashCode(), threadPoolUtil);
        put(JobManager.class.hashCode(), jobManager);
    }


}
