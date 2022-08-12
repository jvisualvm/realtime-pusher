package com.risen.quartz.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/15 17:54
 */
@Data
@Builder
public class TaskDetail {

    private String jobName;


    private String jobGroupName;


    private String triggerName;


    private String triggerGroupName;


    private String cron;


    private Long runTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fireTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextRunTime;

}
