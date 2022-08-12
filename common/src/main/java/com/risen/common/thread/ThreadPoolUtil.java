package com.risen.common.thread;

import com.alibaba.fastjson.JSON;
import com.risen.common.util.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/6 14:26
 */
@Component
public class ThreadPoolUtil implements InitializingBean, Serializable {


    public static ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolConfig threadPoolConfig;

    public ThreadPoolUtil(ThreadPoolConfig threadPoolConfig) {
        ThreadPoolUtil.threadPoolConfig = threadPoolConfig;
    }


    public void addTask(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public void removeTask(Runnable task) {
        threadPoolExecutor.remove(task);
    }


    @PreDestroy
    public void shutdown() {
        try {
            LogUtil.info("stopping ThreadPoolUtil task");
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(15, TimeUnit.SECONDS)) {
                LogUtil.error("task exception exit happens, please check");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void afterPropertiesSet() {
        threadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadPoolConfig.getCapacity()),
                Executors.defaultThreadFactory(), new ThreadPoolRejectStrategy());

        LogUtil.info("threadPoolConfig:{}", JSON.toJSONString(threadPoolConfig));

    }

}
