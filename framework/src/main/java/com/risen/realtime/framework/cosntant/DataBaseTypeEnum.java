package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 15:21
 */
public enum DataBaseTypeEnum {

    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL("postgresql", "org.postgresql.Driver"),
    MDB("mdb", "net.ucanaccess.jdbc.UcanaccessDriver");

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String driver;

    DataBaseTypeEnum(String type, String driver) {
        this.type = type;
        this.driver = driver;
    }
}
