package com.lolo.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.lolo.connection.DBCPTest;
import com.lolo.connection.DruidTest;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 提供数据库连接、关闭等操作的工具类
 */
public class JDBCUtils {

    private JDBCUtils() {}

    /**
     * 方式一：通过DriverManager手动获取数据库连接
     */
    public static Connection getConnection() throws Exception {

        // 1. 加载配置文件(这种加载方式不好)
        Properties prop = new Properties();
        FileInputStream fis =
                new FileInputStream("E:\\myStudyProject\\jdbc\\src\\main\\resources\\jdbc.properties");
        prop.load(fis);

        // 2. 获取连接所需的四个基本信息
        String driverClass = prop.getProperty("driverClass");
        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");

        // 3. 加载驱动
        Class.forName(driverClass);
        // 4. 获取连接
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 方式二：使用c3p0数据库连接池获取连接
     * 注意：不是调用一次方法创建一个池子，而是只有一个池子
     */
    private static ComboPooledDataSource cpds =
            new ComboPooledDataSource("helloc3p0");//保证此数据源是唯一的！
    public static Connection getC3P0Connection() throws SQLException {
        return cpds.getConnection();
    }

    /**
     * 方式三：使用dbcp数据库连接池获取连接
     */
    private static DataSource source = null;//保证此数据源是唯一的！
    static {
        try {
            Properties pros = new Properties();
            InputStream is = DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getDBCPConnection() throws Exception {
        return source.getConnection();
    }

    /**
     * 方式四：使用Druid数据库连接池获取连接【推荐】
     */
    private static DataSource druidSource = null;
    static {
        try {
            // 1、读取druid.properties文件
            Properties props = new Properties();
            props.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));

            // 2、连接连接池
            druidSource = DruidDataSourceFactory.createDataSource(props);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 3. 获取连接
    public static Connection getDruidConnection() {
        Connection connection = null;
        try {
            connection = druidSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭资源的操作
     */
    public static void closeResource(Connection conn, Statement st) {
        closeResource(conn, st, null);
    }

    /**
     * 关闭资源的操作
     */
    public static void closeResource(Connection conn, Statement st, ResultSet rs) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源的操作: DbUtils
     */
    public static void closeResourceWithDbUtils(Connection conn, Statement st, ResultSet rs) {

        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DbUtils.close(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DbUtils.close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
