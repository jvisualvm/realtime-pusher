package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 14:15
 */
@Getter
@Setter
@TableName("push_source")
public class PushSourceEntity implements Serializable {

    @TableField("task_key")
    private String taskKey;

    @TableField("data_table")
    private String dataTable;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

}
