package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/30 23:23
 */
public enum DataModelEnum {

    STR_MODE(0, "全部字符串类型"),
    MAP_MODEL(1, "按照MAP匹配"),
    FILE_MODEL(2, "按照文件里面的数据类型（mdb)匹配");

    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String msg;


    DataModelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
