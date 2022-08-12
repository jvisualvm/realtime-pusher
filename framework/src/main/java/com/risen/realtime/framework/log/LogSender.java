package com.risen.realtime.framework.log;

import com.risen.common.cosntant.ChannelEnum;
import com.risen.common.util.DateUtil;
import com.risen.websocket.notify.WsMessageNotifier;
import com.risen.websocket.response.WsHeader;
import com.risen.websocket.response.WsMessage;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/26 21:35
 */
public class LogSender {


    public static void printInfo(Object msg) {
        WsHeader header = WsHeader.build(ChannelEnum.LOG.getCode());
        WsMessage message = WsMessage.build(header, info(msg));
        WsMessageNotifier.sendByChannel(message);
    }

    public static void printWarn(Object msg) {
        WsHeader header = WsHeader.build(ChannelEnum.LOG.getCode());
        WsMessage message = WsMessage.build(header, warn(msg));
        WsMessageNotifier.sendByChannel(message);
    }

    public static void printError(Object msg) {
        WsHeader header = WsHeader.build(ChannelEnum.LOG.getCode());
        WsMessage message = WsMessage.build(header, error(msg));
        WsMessageNotifier.sendByChannel(message);
    }

    private static String info(Object log) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.parseDate(System.currentTimeMillis(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        builder.append(" 提示: ");
        builder.append(log);
        return builder.toString();
    }

    private static String warn(Object log) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.parseDate(System.currentTimeMillis(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        builder.append(" 警告: ");
        builder.append(log);
        return builder.toString();
    }

    private static String error(Object log) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.parseDate(System.currentTimeMillis(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        builder.append(" 错误: ");
        builder.append(log);
        return builder.toString();
    }

}
