package com.risen.realtime.framework.util;

import java.lang.ref.WeakReference;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/2 13:32
 */
public class WeakReferenceUtil {


    public static <T extends Object> T getWeakObject(T t) {
        WeakReference<T> viewReference = new WeakReference<>(t);
        return viewReference.get();
    }


}
