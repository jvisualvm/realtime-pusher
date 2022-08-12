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
@TableName("push_user")
@ApiModel(value = "PushUserEntity对象", description = "")
public class PushUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("type")
    private Integer type;

    @TableField("description")
    private String description;
}
