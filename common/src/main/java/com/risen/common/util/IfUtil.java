package com.risen.common.util;

import com.risen.common.consumer.ConditionConsumer;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/5/16 18:43
 */
public class IfUtil {

    private final static Integer inc = 1;


    public static void goIf(ConditionConsumer ifConsumer, Boolean branchCondition) {
        if (branchCondition) {
            Optional.ofNullable(ifConsumer).ifPresent(oif -> {
                oif.accept();
            });
        }
    }


    /**
     * @param ifConsumer
     * @param elseConsumer
     * @param branchCondition
     * @return void
     * @author: zhangxin
     * @description: 去掉if else
     * @date: 2022/5/16 16:22
     */
    public static void goIf(ConditionConsumer ifConsumer, ConditionConsumer elseConsumer, Boolean branchCondition) {
        if (branchCondition) {
            Optional.ofNullable(ifConsumer).ifPresent(oif -> {
                oif.accept();
            });
        } else {
            Optional.ofNullable(elseConsumer).ifPresent(oelse -> {
                oelse.accept();
            });
        }
    }


    public static void goIf(ConditionConsumer ifConsumer, ConditionConsumer ifElseConsumer, ConditionConsumer elseConsumer, Boolean ifBranchCondition, Boolean ifElseBranchCondition) {
        if (ifBranchCondition) {
            Optional.ofNullable(ifConsumer).ifPresent(oif -> {
                oif.accept();
            });
        } else if (ifElseBranchCondition) {
            Optional.ofNullable(ifElseConsumer).ifPresent(ifelse -> {
                ifelse.accept();
            });
        } else {
            Optional.ofNullable(elseConsumer).ifPresent(oelse -> {
                oelse.accept();
            });
        }
    }


    public static void goIf(ConditionConsumer ifConsumer, ConditionConsumer ifElseConsumer, ConditionConsumer ifIfElseConsumer, ConditionConsumer elseConsumer, Boolean ifBranchCondition, Boolean ifElseBranchCondition, Boolean ifIfElseBranchCondition) {
        if (ifBranchCondition) {
            Optional.ofNullable(ifConsumer).ifPresent(oif -> {
                oif.accept();
            });
        } else if (ifElseBranchCondition) {
            Optional.ofNullable(ifElseConsumer).ifPresent(ifelse -> {
                ifelse.accept();
            });
        } else if (ifIfElseBranchCondition) {
            Optional.ofNullable(ifIfElseConsumer).ifPresent(ifelse -> {
                ifelse.accept();
            });
        } else {
            Optional.ofNullable(elseConsumer).ifPresent(oelse -> {
                oelse.accept();
            });
        }
    }

    /**
     * @param isContinue
     * @param consumers
     */
    public static void goIf(LinkedList<Boolean> isContinue, ConditionConsumer... consumers) {
        if (PredicateUtil.isAnyEmpty(isContinue, consumers)) {
            return;
        }
        if (isContinue.size() != consumers.length - inc) {
            LogUtil.debug("if判断条件的数量只能ConditionConsumer数量-1，当前判断条件数量：{}，ConditionConsumer-1数量：{}", isContinue.size(), consumers.length - 1);
            return;
        }
        AtomicReference<Integer> index = new AtomicReference(0);
        AtomicReference<Integer> falseCount = new AtomicReference(0);
        isContinue.forEach(item -> {
            if (item) {
                consumers[index.get()].accept();
            } else {
                falseCount.set(falseCount.get() + inc);
            }
            index.set(index.get() + inc);
            //最后一个判断条件的时候，判断一下false的数量，执行else操作
            if (falseCount.get().equals(isContinue.size())) {
                consumers[index.get()].accept();
            }
        });
    }


}
