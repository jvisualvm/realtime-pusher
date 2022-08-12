package com.risen.common.consumer;

import java.util.Objects;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/5/16 15:59
 */
@FunctionalInterface
public interface ConditionConsumer {

    void accept();

    default ConditionConsumer andThen(ConditionConsumer after) {
        Objects.requireNonNull(after);
        return () -> {
            accept();
            after.accept();
        };
    }

}
