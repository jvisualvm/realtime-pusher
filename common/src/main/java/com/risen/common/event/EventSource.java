package com.risen.common.event;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/13 16:25
 */
@Data
public class EventSource implements Serializable {

    private String eventType;
    private Object data;

    public EventSource(String eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }
}
