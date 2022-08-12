package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxin
 * @since 2022-07-20
 */
@Getter
@Setter
@TableName("push_task")
public class PushTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("task_name")
    private String taskName;

    @TableField("cron")
    private String cron;

    @TableId("task_key")
    private String taskKey;

    @TableField("enable")
    private Boolean enable = true;

    @TableField("remark")
    private String remark;

    @TableField("target_id")
    private Integer targetId;

    @TableField
    private Integer taskType;

    @TableField
    private String fileDirectory;

    @TableField
    private String fileType;

    @TableField(exist = false)
    private String dataSourceName;

}
