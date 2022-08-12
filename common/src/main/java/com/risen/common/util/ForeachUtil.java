package com.risen.common.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/5/1 11:51
 */
public class ForeachUtil {

    /**
     * @param iterable
     * @param action
     * @return void
     * @author: zhangxin
     * @description: 带索引的遍历
     * @date: 2022/5/13 13:53
     */
    public static <E> void forEach(Iterable<E> iterable, BiConsumer<Integer, E> action) {
        Integer index = -1;
        for (E e : iterable) {
            action.accept(++index, e);
        }
    }

    /**
     * @param lst
     * @param consumer
     * @return void
     * @author: zhangxin
     * @description: 通用的循环遍历方法
     * @date: 2022/5/13 13:53
     */
    public static <T extends Object> void forEach(List<T> lst, Consumer<List<T>> consumer) {
        if (!CollectionUtils.isEmpty(lst)) {
            consumer.accept(lst);
        }
    }

    public static <T extends Object> void forEachOne(List<T> lst, Consumer<T> consumer) {
        if (!CollectionUtils.isEmpty(lst)) {
            lst.forEach(item -> {
                consumer.accept(item);
            });
        }
    }


    public static <T extends Object> void forEach(Set<T> lst, Consumer<Set<T>> consumer) {
        if (!CollectionUtils.isEmpty(lst)) {
            consumer.accept(lst);
        }
    }

    public static <T extends Object> void forEachOne(Set<T> lst, Consumer<T> consumer) {
        if (!CollectionUtils.isEmpty(lst)) {
            lst.forEach(item -> {
                consumer.accept(item);
            });
        }
    }
}
