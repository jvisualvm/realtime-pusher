package com.risen.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:14
 */

public class Event extends ApplicationEvent {

    public Event(EventSource source) {
        super(source);
    }

}
