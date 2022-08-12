package com.risen.quartz.config;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/15 13:30
 */
@Configuration
public class QuartzBeanConfig {

    @Bean
    @Primary
    public SchedulerFactory schedulerFactoryConfig() {
        return new StdSchedulerFactory();
    }

}
