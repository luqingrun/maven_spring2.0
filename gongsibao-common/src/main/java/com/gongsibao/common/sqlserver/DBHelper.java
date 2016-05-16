package com.gongsibao.common.sqlserver;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.PropertiesReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
    private final static String DRIVER_NAME = PropertiesReader.getValue("ssdb", "sqlserver.driverName");
    private final static String	URL	= PropertiesReader.getValue("ssdb", "sqlserver.url");
    private final static String	USER = PropertiesReader.getValue("ssdb", "sqlserver.user");
    private final static String	PWD	= PropertiesReader.getValue("ssdb", "sqlserver.pwd");
    private final static int maxPoolSize	= NumberUtils.toInt(PropertiesReader.getValue("ssdb", "sqlserver.maxPoolSize"));
    private final static int minPoolSize	= NumberUtils.toInt(PropertiesReader.getValue("ssdb", "sqlserver.minPoolSize"));
    private final static int initialPoolSize = NumberUtils.toInt(PropertiesReader.getValue("ssdb", "sqlserver.initialPoolSize"));
    private final static int idleConnectionTestPeriod = NumberUtils.toInt(PropertiesReader.getValue("ssdb", "sqlserver.idleConnectionTestPeriod"));
    private final static int maxIdleTime = NumberUtils.toInt(PropertiesReader.getValue("ssdb", "sqlserver.maxIdleTime"));

    private final static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    static {
        try {
            Class.forName(DRIVER_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            dataSource.setDriverClass(DRIVER_NAME);
            dataSource.setJdbcUrl(URL);
            dataSource.setUser(USER);
            dataSource.setPassword(PWD);
            dataSource.setMaxPoolSize(maxPoolSize);
            dataSource.setMinPoolSize(minPoolSize);
            dataSource.setInitialPoolSize(initialPoolSize);
            dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
            dataSource.setMaxIdleTime(maxIdleTime);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    //取得连接
    public static Connection getConn() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭连接
    public static void closeConn(Connection conn)
    {
        try {
            if (null != conn) {
                conn.close();
                conn = null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            conn = null;
        }
    }

    public static List<Map<String, Object>> query(String sql)
    {
        Connection conn = getConn();
        List<Map<String, Object>> mapList = query(sql, conn);
        closeConn(conn);
        return mapList;
    }

    public static List<Map<String, Object>> query(String sql, Connection conn) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData rsmData = rs.getMetaData();
            int nCols = rsmData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for(int k = 0; k < nCols; k++) {
                    String columnName = rsmData.getColumnName(k + 1);
                    map.put(columnName, rs.getString(columnName) == null ? "" : rs.getString(columnName));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return list;
    }
}
