package com.risen.common.util;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/5/13 13:58
 */
public class ThreadLocalUtil {

    public final static InheritableThreadLocal<Object> inLocal = new InheritableThreadLocal<>();
    public final static ThreadLocal<Object> local = new ThreadLocal<>();


}
