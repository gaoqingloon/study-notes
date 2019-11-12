package com.lolo.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther: gordon    Email:gordon_ml@163.com
 * @Date: 11/12/2019
 * @Description: 获取连接或释放连接的工具类（Druid）
 * @version: 1.0
 */
public class JDBCUtilsFinish {

    // 数据源,即连接池
    private static DataSource source;
    // ThreadLocal对象
    private static ThreadLocal<Connection> threadLocal;

    static {
        try {
            // 1、读取druid.properties文件
            Properties pros = new Properties();
            pros.load(JDBCUtilsFinish.class
                    .getClassLoader().getResourceAsStream("druid.properties"));

            // 2、连接连接池
            source = DruidDataSourceFactory.createDataSource(pros);

            //3、创建线程池
            threadLocal = new ThreadLocal<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JDBCUtilsFinish() {

    }

    /**
     * 获取数据库连接的方法
     * @return
     */
    public static Connection getConnection() {

        // 从当前线程中获取连接
        Connection connection = threadLocal.get();
        if (connection == null) {
            // 从连接池中获取一个连接
            try {
                connection = source.getConnection();
                // 将连接与当前线程绑定
                threadLocal.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 释放数据库连接的方法
     * @param connection
     */
    public static void releaseConnection(Connection connection) {

        // 获取当前线程池中的连接
        connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.close();
                // 将已经关闭的连接从当前线程中移除
                threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = JDBCUtilsFinish.getConnection();
        System.out.println(connection);
    }
}
