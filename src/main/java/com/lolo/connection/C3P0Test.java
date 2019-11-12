package com.lolo.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;

/**
 * 1. 实现数据库连接的方式：
 *  (1) 手动实现的连接(DriverManager)
 *  (2) 使用数据库连接池
 *
 * 2. 使用数据库连接池的好处：
 *  (1) 更快的连接速度
 *  (2) 更好的资源重用
 *  (3) 更便捷的数据库连接的管理
 *
 * 3. JDBC的数据库连接池使用javax.sql.DataSource来表示，
 * DataSource只是一个接口
 *
 * 4. 两种数据库连接池技术：
 *  (1)C3P0数据库连接池
 *  (2)DBCP数据库连接池
 */
public class C3P0Test {

    /**
     * 方式一：
     */
    @Test
    public void testConnection1() throws Exception {

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("123456");

        //
        //cpds.setMaxPoolSize(100);

        Connection conn = cpds.getConnection();
        System.out.println(conn);
        //com.mchange.v2.c3p0.impl.NewProxyConnection@edf4efb
    }

    /**
     * 方式二：使用配置文件
     */
    @Test
    public void testConnection2() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);

    }

}
