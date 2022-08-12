package com.risen.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 11:01
 */
public class LoadJarUtil {

    public static void load(String path) {
        File libPath = new File(path);
        if (libPath.exists() && libPath.isDirectory()) {
            File[] jarFiles = libPath.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            load(jarFiles);
            return;
        }
        if (libPath.exists() && libPath.isFile()) {
            File[] jarFiles = {libPath};
            load(jarFiles);
            return;
        }
        LogUtil.error("系统目录结构被改动，无法启动，请联系管理员");
    }

    private static void load(File[] jarFiles) {
        if (jarFiles != null) {
            Method method = null;
            try {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            }
            boolean accessible = method.isAccessible();
            try {
                if (accessible == false) {
                    method.setAccessible(true);
                }
                URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
                for (File file : jarFiles) {
                    try {
                        URL url = file.toURI().toURL();
                        method.invoke(classLoader, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                method.setAccessible(accessible);
            }
        }
    }

}
