package com.risen.common.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/6 15:10
 */
public class ThreadPoolRejectStrategy implements RejectedExecutionHandler {


    public static LongAdder longAdder = new LongAdder();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        longAdder.add(1);
    }
}
