package com.risen.realtime.framework.util;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 10:30
 */
@Component
@AllArgsConstructor
public class ServiceUtil {


    private Environment environment;


    public String getServiceIp() {
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public Integer getServicePort() {
        return Integer.valueOf(environment.getProperty("server.port"));
    }

    public String getServiceName() {
        return environment.getProperty("server.servlet.context-path");
    }

}
