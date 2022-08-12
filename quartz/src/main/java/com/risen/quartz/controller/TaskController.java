package com.risen.quartz.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 8:36
 */
@RestController
@RequestMapping(value = "/task")
@AllArgsConstructor
@Api(tags = "定时任务管理接口")
public class TaskController {


}
