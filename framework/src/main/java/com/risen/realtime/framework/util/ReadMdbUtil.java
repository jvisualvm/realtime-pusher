package com.risen.realtime.framework.util;

import com.risen.common.cosntant.Symbol;
import com.risen.common.cosntant.SystemConstant;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.datasource.DataSourceUtil;
import com.risen.realtime.framework.dto.MDBFileDTO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 14:10
 */
public class ReadMdbUtil {

    public static MDBFileDTO readMDBFile(String userName, String password, String table, File file, int readStartPoint, int currentMax) {
        LogUtil.info("开始读取文件" + file.getName());
        MDBFileDTO mdbFileDTO = new MDBFileDTO();
        List<String> dataList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        int maxKey = 0;
        try {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            Connection conn = null;
            try {
                conn = getConnect(userName, password, file);
                preparedStatement = conn.prepareStatement("select * from " + table + " where UniqueID > ? and UniqueID <= ?  order by UniqueID asc limit ?");
                preparedStatement.setInt(1, readStartPoint);
                preparedStatement.setInt(2, currentMax);
                preparedStatement.setInt(3, 10000);
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    StringBuilder builder = new StringBuilder();
                    int columnCount = rs.getMetaData().getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        //填充列名称
                        if (columnList.size() < columnCount) {
                            columnList.add(rs.getMetaData().getColumnName(i).toUpperCase(Locale.ROOT));
                        }
                        Object obj = rs.getObject(i);
                        builder.append(obj);
                        builder.append(Symbol.SYMBOL_COMMA);
                        maxKey = rs.getInt(1);
                    }
                    dataList.add(builder.deleteCharAt(builder.length() - 1).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeAllConnect(rs, preparedStatement, conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mdbFileDTO.setFieldList(columnList);
        mdbFileDTO.setMaxKey(maxKey);
        mdbFileDTO.setDataList(dataList);
        LogUtil.info("读取行数:" + dataList.size());
        return mdbFileDTO;
    }


    private static Connection getConnect(String userName, String password, File file) throws SQLException {
        StringBuilder mdbPath = new StringBuilder("jdbc:ucanaccess://");
        mdbPath.append(file.getAbsolutePath());
        System.out.println("path:"+SystemConstant.getTempPath());
        mdbPath.append(";memory=false;keepMirror=" + SystemConstant.getTempPath());
        return DataSourceUtil.getConnection(mdbPath.toString(), userName, password);
    }

    public static int queryMaxKey(String userName, String password, String table, File file) {
        int maxKey = 0;
        try {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            Connection conn = null;
            try {
                conn = getConnect(userName, password, file);
                preparedStatement = conn.prepareStatement("select max(UniqueID) from " + table);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    maxKey = rs.getInt(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeAllConnect(rs, preparedStatement, conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxKey;
    }


    private static void closeAllConnect(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != ps) {
                ps.close();
            }
            if (null != conn) {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
