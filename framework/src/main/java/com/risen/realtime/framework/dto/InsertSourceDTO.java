package com.risen.realtime.framework.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 15:42
 */
@Data
public class InsertSourceDTO {

    @ApiModelProperty(name = "dataSourceName", value = "数据源名称")
    @NotEmpty
    private String dataSourceName;

    @ApiModelProperty(name = "url", value = "数据源地址")
    @NotEmpty
    private String url;

    @ApiModelProperty(name = "dataTable", value = "表名")
    @NotEmpty
    private String dataTable;

    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    @ApiModelProperty(name = "topic", value = "kafka topic")
    private String topic;

    @ApiModelProperty(name = "type", value = "数据源类型 0是数据库 1是kafka")
    private Integer type;

    @ApiModelProperty(name = "field", value = "字段列表,号分割")
    private String field;

    @ApiModelProperty(name = "enable", value = "1启用 0禁用")
    private Boolean enable;

}
