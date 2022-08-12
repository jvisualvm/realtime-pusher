package com.risen.common.thread;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/6 15:02
 */
public class ThreadPoolView {

    /**
     * @param
     * @return java.util.Map
     * @author: zhangxin
     * @description: TODO
     * @date: 2022/3/6 15:16
     */
    public static Map getThreadPoolRunStatusView() {
        Map linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("corePoolSize", ThreadPoolUtil.threadPoolExecutor.getCorePoolSize());
        linkedHashMap.put("maximumPoolSize", ThreadPoolUtil.threadPoolExecutor.getMaximumPoolSize());
        linkedHashMap.put("largestPoolSize", ThreadPoolUtil.threadPoolExecutor.getLargestPoolSize());
        linkedHashMap.put("activeCount", ThreadPoolUtil.threadPoolExecutor.getActiveCount());
        linkedHashMap.put("taskCount", ThreadPoolUtil.threadPoolExecutor.getTaskCount());
        linkedHashMap.put("completedTaskCount", ThreadPoolUtil.threadPoolExecutor.getCompletedTaskCount());
        linkedHashMap.put("rejectTaskCount", ThreadPoolRejectStrategy.longAdder.longValue());
        return linkedHashMap;
    }


}
