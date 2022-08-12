package com.risen.common.thread;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/6 14:29
 */
@Component
@Data
public class ThreadPoolConfig {

    @Value("${thread.pool.corePoolSize:6}")
    private Integer corePoolSize;

    @Value("${thread.pool.maximumPoolSize:64}")
    private Integer maximumPoolSize;

    @Value("${thread.pool.keepAliveTime:60}")
    private Integer keepAliveTime;

    @Value("${thread.pool.capacity:1024}")
    private Integer capacity;


}
