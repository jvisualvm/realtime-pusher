package com.risen.ui.swing;

import com.risen.common.cosntant.SystemConstant;
import com.risen.common.util.LogUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 19:48
 */
public class InitSystem {

    public static boolean initSystem() {

        String corePath = initPath(SystemConstant.DEV_CORE_LIB_PATH, SystemConstant.PROD_CORE_LIB_PATH);
        if (StringUtils.isEmpty(corePath)) {
            LogUtil.info("系统目录结构被改动，无法启动，请联系管理员");
            return false;
        }
        String libPath = initPath(SystemConstant.DEV_LIB_PATH, SystemConstant.PROD_LIB_PATH);
        if (StringUtils.isEmpty(libPath)) {
            LogUtil.info("系统目录结构被改动，无法启动，请联系管理员");
            return false;
        }
        return true;
    }

    public static String changePath(String path) {
        String finalPath = null;
        switch (SystemConstant.CURRENT_ENVIRONMENT) {
            case SystemConstant.DEV:
                finalPath = path;
                break;
            case SystemConstant.PROD:
                finalPath = path.replace("\\client", "");
                break;
            default:
                break;
        }
        return finalPath;
    }


    private static String initPath(String devLibPath, String prodLibPath) {
        File prodPath = new File(prodLibPath);
        File devPath = new File(devLibPath);
        String currentLibPath = null;
        if (prodPath.exists()) {
            SystemConstant.CURRENT_ENVIRONMENT = SystemConstant.PROD;
            currentLibPath = prodLibPath;
        } else if (devPath.exists()) {
            SystemConstant.CURRENT_ENVIRONMENT = SystemConstant.DEV;
            currentLibPath = devLibPath;
        } else {
            LogUtil.info("系统目录结构被改动，无法启动，请联系管理员");
            return currentLibPath;
        }
        return currentLibPath;
    }

}
