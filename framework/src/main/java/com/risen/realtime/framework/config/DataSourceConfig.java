package com.risen.realtime.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/12 11:02
 */

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("mainDruidDataSource")
    @Primary
    public DataSource czReportDataSource() {
        return new SQLiteDataSource();
    }


}
