package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
@TableName("push_sink")
@ApiModel(value = "PushSinkEntity对象", description = "")
public class PushSinkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("data_source_name")
    private String dataSourceName;

    @TableField("url")
    private String url;

    @TableField("data_table")
    private String dataTable;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("topic")
    private String topic;

    @TableField("type")
    private Integer type;

    @TableField("field")
    private String field;

    @TableField("enable")
    private Boolean enable = true;

    @TableField("start_index")
    private Integer startIndex;

    @TableField("columns_count")
    private Integer columnsCount;

    @TableField("data_mode")
    private Integer dataMode;

    @TableField("end_flag")
    private String endFlag;

    @TableField("data_map")
    private String dataMap;

    @TableField("discard_rule")
    private String discardRule;
}
