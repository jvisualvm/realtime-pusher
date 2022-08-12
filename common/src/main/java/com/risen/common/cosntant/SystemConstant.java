package com.risen.common.cosntant;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 17:56
 */
public class SystemConstant {

    public static String DEV_LIB_PATH = System.getProperty("user.dir") + "\\client\\resource\\lib";
    public static String PROD_LIB_PATH = System.getProperty("user.dir") + "\\resource\\lib";

    public static String DEV_CORE_LIB_PATH = System.getProperty("user.dir") + "\\client\\resource\\core";
    public static String PROD_CORE_LIB_PATH = System.getProperty("user.dir") + "\\resource\\core";


    public static String DEV_TEMP_PATH = System.getProperty("user.dir") + "\\client\\temp\\data";
    public static String PROD_TEMP_PATH = System.getProperty("user.dir") + "\\temp\\data";

    public volatile static String CURRENT_ENVIRONMENT = null;
    public static final String DEV = "dev";
    public static final String PROD = "prod";

    public static String getTempPath() {
        if (DEV.equals(CURRENT_ENVIRONMENT)) {
            return DEV_TEMP_PATH;
        }
        return PROD_TEMP_PATH;
    }


}
