package com.lolo.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 使用DBCP数据库连接池技术实现数据库连接
 */
public class DBCPTest {

    /**
     * 方式一：在代码中直接设置参数
     */
    @Test
    public void testGetConnection1() throws SQLException {

        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        //source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUrl("jdbc:mysql:///test");
        source.setUsername("root");
        source.setPassword("123456");

        //
        source.setInitialSize(10);
        //...

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    /**
     * 方式二：使用配置文件
     */
    @Test
    public void testGetConnection2() throws Exception {

        Properties pros = new Properties();
        // 方式1：
        //FileInputStream fis = new FileInputStream("E:\\myStudyProject\\jdbc\\src\\main\\resources\\dbcp.properties");
        //pros.load(fis);
        // 方式2：使用类的加载器
        InputStream is = DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
        pros.load(is);

        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
