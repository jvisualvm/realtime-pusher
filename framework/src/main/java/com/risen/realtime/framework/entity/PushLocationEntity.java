package com.risen.realtime.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("push_location")
@ApiModel(value = "PushLocationEntity对象", description = "")
public class PushLocationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("location_key")
    private String locationKey;

    @TableField("start_key")
    private Integer startKey;

    @TableField("timestamp")
    private String timestamp;

}
