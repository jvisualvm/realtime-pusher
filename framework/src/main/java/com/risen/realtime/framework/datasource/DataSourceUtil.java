package com.risen.realtime.framework.datasource;

import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.cosntant.DataBaseTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 15:32
 */
public class DataSourceUtil {

    public static Connection getConnection(String url, String userName, String password) {
        Connection con = null;
        try {
            if (StringUtils.isEmpty(url)) {
                String error = "数据源缺失url信息，请在管理页面进行配置";
                LogUtil.info(error);
                throw new RuntimeException(error);
            }
            if (url.contains(DataBaseTypeEnum.POSTGRESQL.getType())) {
                Class.forName(DataBaseTypeEnum.POSTGRESQL.getDriver());
                con = DriverManager.getConnection(createPostgresqlUrl(url), userName, password);
                con.setSchema(createSchema(url));
            } else if (url.contains(DataBaseTypeEnum.MYSQL.getType())) {
                Class.forName(DataBaseTypeEnum.MYSQL.getDriver());
                con = DriverManager.getConnection(url, userName, password);
            } else if (url.contains(DataBaseTypeEnum.MDB.getType())) {
                Class.forName(DataBaseTypeEnum.MDB.getDriver());
                con = DriverManager.getConnection(url, userName, password);
            } else {
                throw new RuntimeException("目前系统只支持postgres/mysql/mdb，请在管理页面进行配置");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    private static String createPostgresqlUrl(String url) {
        StringBuilder urlBuilder = new StringBuilder();
        if (url.contains(Symbol.SYMBOL_SCHEMA) && !url.contains(Symbol.SYMBOL_BQ)) {
            urlBuilder.append(url.replace(url.substring(url.indexOf(Symbol.SYMBOL_QUESTION)), Symbol.EMPTY_STR));
        }
        if (url.contains(Symbol.SYMBOL_SCHEMA) && url.contains(Symbol.SYMBOL_BQ)) {
            urlBuilder.append(url.replace(url.substring(url.indexOf(Symbol.SYMBOL_QUESTION), url.indexOf(Symbol.SYMBOL_BQ)), Symbol.EMPTY_STR));
        }
        return urlBuilder.toString();
    }

    private static String createSchema(String url) {
        StringBuilder urlBuilder = new StringBuilder();
        if (url.contains(Symbol.SYMBOL_SCHEMA_EQUALS) && !url.contains(Symbol.SYMBOL_BQ)) {
            urlBuilder.append(url.substring(url.lastIndexOf(Symbol.SYMBOL_SCHEMA_EQUALS)));
        }
        if (url.contains(Symbol.SYMBOL_SCHEMA_EQUALS) && url.contains(Symbol.SYMBOL_BQ)) {
            urlBuilder.append(url.substring(url.lastIndexOf(Symbol.SYMBOL_SCHEMA_EQUALS), url.indexOf(Symbol.SYMBOL_BQ)));
        }
        return urlBuilder.toString().replace(Symbol.SYMBOL_SCHEMA_EQUALS, Symbol.EMPTY_STR);
    }

}
