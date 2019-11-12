package com.lolo.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.lolo.util.JDBCUtilsFinish;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther: gordon    Email:gordon_ml@163.com
 * @Date: 11/12/2019
 * @Description: 测试获取通过Druid数据库连接池获取连接
 * @version: 1.0
 */
public class DruidTest {

    @Test
    public void testDruidConnection() {

        DataSource dataSource;
        Connection connection = null;
        try {
            // 1、读取druid.properties文件
            Properties pro = new Properties();
            pro.load(DruidTest.class
                    .getClassLoader()
                    .getResourceAsStream("druid.properties"));

            // 2、连接连接池
            dataSource = DruidDataSourceFactory.createDataSource(pro);

            // 3. 获取连接
            connection = dataSource.getConnection();
            System.out.println(connection);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4. 释放连接
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testConnection() {
        Connection connection = JDBCUtilsFinish.getConnection();
        System.out.println(connection);
    }
}
