package com.risen.realtime.framework.util;

import com.risen.realtime.framework.annotation.InitAnnotation;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/4 8:11
 */
public class SubClassUtil {

    public static void initSubClass(String packagePath) {
        Reflections reflections = new Reflections(packagePath);
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(InitAnnotation.class)) {
            try {
                Constructor<?> cons = clazz.getConstructor();
                cons.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
