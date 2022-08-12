package com.risen.realtime.framework.listener;

import com.risen.realtime.framework.cosntant.ServerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/21 20:09
 */
@Component
@Slf4j
public class ServiceFailListener {

    @PreDestroy
    public void serverStop() {
        ServerConstant.IsStopping=true;
    }

}
