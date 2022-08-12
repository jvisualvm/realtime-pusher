package com.risen.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/26 13:16
 */
public class PredicateUtil {


    public static boolean isOverLimit(List lst, Integer limitCount) {
        Predicate<List> predicate = s -> s.size() >= limitCount;
        return predicate.test(lst);
    }

    public static boolean isOverLimit(Object[] array, Integer limitCount) {
        Predicate<Object[]> predicate = s -> s.length >= limitCount;
        return predicate.test(array);
    }

    public static boolean isNotEmpty(String param) {
        Predicate<String> predicate = s -> StringUtils.isNotEmpty(param);
        return predicate.test(param);
    }

    public static boolean isNotEmpty(Object param) {
        Predicate<Object> predicate = s -> !ObjectUtils.isEmpty(param);
        return predicate.test(param);
    }

    public static boolean isNotEmpty(Collection e) {
        Predicate<Collection> predicate = s -> !CollectionUtils.isEmpty(s);
        return predicate.test(e);
    }

    public static boolean isZero(Number e) {
        Predicate<Number> predicate = s -> isAnyEmpty(e) || e.intValue() == 0;
        return predicate.test(e);
    }

    public static boolean isAllNotNull(Object... obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return false;
        }
        boolean isNull = true;
        for (Object i : obj) {
            if (ObjectUtils.isEmpty(i)) {
                isNull = false;
                break;
            }
        }
        return isNull;
    }

    public static boolean isAnyEmpty(Object... obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return true;
        }
        boolean isNull = false;
        for (Object i : obj) {
            if (ObjectUtils.isEmpty(i)) {
                isNull = true;
                break;
            }
        }
        return isNull;
    }


    public static boolean isAnyEquals(Object target, Object... obj) {
        if (ObjectUtils.isEmpty(target) || ObjectUtils.isEmpty(obj)) {
            return false;
        }
        boolean isAnyEquals = false;
        for (Object i : obj) {
            if (PredicateUtil.isNotEmpty(i) && i.equals(target)) {
                isAnyEquals = true;
                break;
            }
        }
        return isAnyEquals;
    }

    public static boolean isAnyEquals(Object target, List<Object> obj) {
        if (ObjectUtils.isEmpty(target) || ObjectUtils.isEmpty(obj)) {
            return false;
        }
        boolean isAnyEquals = false;
        for (Object i : obj) {
            if (PredicateUtil.isNotEmpty(i) && i.equals(target)) {
                isAnyEquals = true;
                break;
            }
        }
        return isAnyEquals;
    }


    public static boolean isOneNotEmpty(Object... obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return false;
        }
        boolean isOneNull = false;
        for (Object i : obj) {
            if (isNotEmpty(i)) {
                isOneNull = true;
                break;
            }
        }
        return isOneNull;
    }


    public static String filterOne(String s1, String s2) {
        return isNotEmpty(s1) ? s1 : s2;
    }


}
