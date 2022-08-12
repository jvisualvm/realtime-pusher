package com.risen.realtime.framework.datasource;

import com.google.common.collect.Lists;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.service.SystemCacheService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 15:32
 */
public class DataInsertUtil {


    public static void delete(String condition, String url, String table, String userName, String password) {
        StopWatch sw = new StopWatch();
        sw.start("检测数据是否存在");
        Connection conn = DataSourceUtil.getConnection(url, userName, password);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(buildDeleteExecuteSql(table, condition));
            ps.executeUpdate();
            sw.stop();
            sw.prettyPrint();
        } catch (Exception e) {
            LogUtil.info("更新失败:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, null);
        }
    }


    public static void update(String condition, String url, String table, String userName, String password) {
        StopWatch sw = new StopWatch();
        sw.start("检测数据是否存在");
        Connection conn = DataSourceUtil.getConnection(url, userName, password);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(buildUpdateExecuteSql(table, condition));
            ps.executeUpdate();
            sw.stop();
            sw.prettyPrint();
        } catch (Exception e) {
            LogUtil.info("更新失败:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, null);
        }
    }


    private static boolean exists(String condition, String url, String table, String userName, String password) {
        StopWatch sw = new StopWatch();
        sw.start("检测数据是否存在");
        Connection conn = DataSourceUtil.getConnection(url, userName, password);
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(buildExistsExecuteSql(table, condition));
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            LogUtil.info("exists count:{}", count);
            sw.stop();
            sw.prettyPrint();
        } catch (Exception e) {
            LogUtil.info("判断失败:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, rs);
        }
        return count > 0;
    }


    private static String buildDeleteExecuteSql(String table, String condition) {
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ");
        builder.append(table);
        builder.append(" where 1=1 ");
        builder.append(condition);
        return builder.toString();
    }


    private static String buildUpdateExecuteSql(String table, String condition) {
        StringBuilder builder = new StringBuilder();
        builder.append("update ");
        builder.append(table);
        builder.append(" set ");
        builder.append(condition);
        return builder.toString();
    }


    private static String buildExistsExecuteSql(String table, String condition) {
        StringBuilder builder = new StringBuilder();
        builder.append("select count(*) from ");
        builder.append(table);
        builder.append(" where 1=1 ");
        builder.append(condition);
        return builder.toString();
    }


    public static void executeInsert(String url, String table, String userName, String password, LinkedHashSet<String> filedSet, List<String> insertList) {
        String errorLog = "开始执行数据批量插入";
        StopWatch sw = new StopWatch();
        sw.start(errorLog);
        LogSender.printInfo(errorLog);
        LogUtil.info(errorLog);
        Connection conn = DataSourceUtil.getConnection(url, userName, password);
        PreparedStatement ps = null;
        try {

            conn.setAutoCommit(false);
            List<List<String>> partitionList = Lists.partition(insertList, 10000);
            for (List<String> lst : partitionList) {
                String executeSql = buildExecuteSql(table, filedSet, lst);
                ps = conn.prepareStatement(executeSql);
                ps.addBatch();
                ps.executeBatch();
                conn.commit();
            }


            sw.stop();
            sw.prettyPrint();
        } catch (Exception e) {
            LogUtil.info("批量插入失败:" + e.getMessage());
            LogSender.printError("批量插入失败:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, null);
        }
    }

    private static String buildExecuteSql(String table, Set<String> filedSet, List<String> insertList) {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into ");
        builder.append(table);
        builder.append(" (");
        builder.append(String.join(Symbol.SYMBOL_COMMA, filedSet));
        builder.append(")");
        builder.append(" values ");
        insertList.forEach(item -> {
            builder.append("(");
            builder.append(item);
            builder.append("),");
        });
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    private static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void dataInsert(String taskKey, List<String> resultList, List<String> fieldList) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushSinkEntity pushSink = pushTaskCache.getTaskSink(taskKey);
        LinkedHashSet<String> filedSet = new LinkedHashSet<>(Arrays.asList(pushSink.getField().split(Symbol.SYMBOL_COMMA)));
        if (!CollectionUtils.isEmpty(fieldList)) {
            LinkedHashSet<String> dataBaseList = new LinkedHashSet<>(fieldList);
            filedSet.addAll(dataBaseList);
        }
        DataInsertUtil.executeInsert(pushSink.getUrl(), pushSink.getDataTable(), pushSink.getUserName(), pushSink.getPassword(), filedSet, resultList);
    }


    public static void dataDelete(String taskKey, String condition) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushSinkEntity pushSink = pushTaskCache.getTaskSink(taskKey);
        DataInsertUtil.delete(condition, pushSink.getUrl(), pushSink.getDataTable(), pushSink.getUserName(), pushSink.getPassword());
    }

}
