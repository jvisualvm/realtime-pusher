package com.risen.realtime.framework.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/21 10:03
 */
@Data
public class UserInfoDTO {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;

}
