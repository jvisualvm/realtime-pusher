package com.risen.common.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:15
 */
@Component
@AllArgsConstructor
public class EventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(String eventType, Object value) {
        EventSource eventSource = new EventSource(eventType, value);
        Event event = new Event(eventSource);
        applicationEventPublisher.publishEvent(event);
    }

}
