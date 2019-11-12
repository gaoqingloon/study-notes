package com.lolo.jdbc;

import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 1.jdbc:java提供的一套api，使用这套api可以实现对数据表的操作。这里的api：java.sql 和 javax.sql
 *
 */
public class ConnectionTest {

    /**
     * 方式一：
     *
     *  要想获取数据库连接，需要准备如下的几个事情：
     *      1. 加载mysql驱动
     *      2. 正确的填写url / user / password
     *      3. 数据库服务需要开启
     */
    @Test
    public void testConnection1() throws SQLException {
        // 1. 提供一个mysql中实现接口Driver的类的对象
        Driver driver = new com.mysql.jdbc.Driver();
        // 2. 提供url
        String url = "jdbc:mysql://localhost:3306/test";
        // 3. 提供用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        // 4. 获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
        // com.mysql.jdbc.JDBC4Connection@449b2d27
    }

    /**
     * 方式二：
     *      在方式一的基础上，使用反射创建驱动
     */
    @Test
    public void testConnection2() throws Exception {
        // 1. 提供一个mysql中实现接口Driver的类的对象
        String className = "com.mysql.jdbc.Driver";
        Class clazz = Class.forName(className);
        Driver driver = (Driver) clazz.newInstance();
        // 2. 提供url
        String url = "jdbc:mysql://localhost:3306/test";
        // 3. 提供用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        // 4. 获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
        // com.mysql.jdbc.JDBC4Connection@449b2d27
    }

    /**
     * 方式三：
     *      使用DriverManager实现数据库的连接
     */
    @Test
    public void testConnection3() throws Exception {

        // 1. 提供连接所需的四个基本信息
        String className = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        // 2. 创建驱动
        Class clazz = Class.forName(className);
        Driver driver = (Driver) clazz.newInstance();
        // 3. 注册驱动
        DriverManager.registerDriver(driver);
        // 4. 获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    /**
     * 方式四：
     *      在方式三的基础上迭代
     */
    @Test
    public void testConnection4() throws Exception {

        // 1. 提供连接所需的四个基本信息
        String className = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        // 2. 加载驱动
        Class.forName(className);
        /*
        在mysql的Driver中找到了如下的代码：
        static {
            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException var1) {
                throw new RuntimeException("Can't register driver!");
            }
        }
         */
        // 3. 获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    /**
     * 方式五：(推荐)
     *      在方式四的基础上迭代:将数据库连接使用的4个基本信息使用配置文件提供
     *
     * 好处：
     *      1. 将连接所需的基本信息提供在配置文件中，便于维护和修改
     *      2. 如果修改配置文件中的配置信息，不需要重新编译代码
     */
    @Test
    public void testConnection5() throws Exception {

        // 1. 加载配置文件
        Properties pros = new Properties();
        FileInputStream fis = new FileInputStream("E:\\myStudyProject\\jdbc\\src\\main\\resources\\jdbc.properties");
        pros.load(fis);

        // 2. 获取连接所需的四个基本信息
        String driverClass = pros.getProperty("driverClass");
        String url = pros.getProperty("url");
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");

        // 3. 加载驱动
        Class.forName(driverClass);
        // 4. 获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    /**
     * 写成工具类
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println(JDBCUtils.getConnection());
    }
}
