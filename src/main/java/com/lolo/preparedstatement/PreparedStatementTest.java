package com.lolo.preparedstatement;

import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用PreparedStatement实现数据表中数据的增删改查操作。增删改；查
 *
 * 1. PreparedStatement 是java.sql下定义的一个接口，是Statement的子接口
 *
 */
public class PreparedStatementTest {

    /**
     * 测试连接, 【单元测试没有参数】
     */
    @Test
    public void testConnection() throws Exception {
        System.out.println(JDBCUtils.getConnection());
    }

    /**
     * 使用PreparedStatement实现数据的添加
     * create table customers (id int primary key autoincrement not null,
     * name varchar(15), email varchar(20), birth date, photo mediumblob);
     */
    @Test
    public void testInsert() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            conn = JDBCUtils.getConnection();
            // 2. 提供一个包含占位符的sql语句
            // ?: 占位符
            String sql = "insert into customers(name,email,birth) values(?,?,?)";

            // 3. 调用Connection的方法，获取一个PreparedStatement：预编译sql语句
            ps = conn.prepareStatement(sql);

            // 4. 填充占位符
            ps.setString(1, "李楠楠");
            ps.setString(2, "lnn@163.com");
            ps.setDate(3, new Date(1595634233L)); // 数据库中的Date对应java中的java.sql.Date

            // 5. 执行
            //ps.execute();
            // 返回修改的条数
            int count = ps.executeUpdate();
            System.out.println("添加了" + count + "条记录");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 资源的关闭
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用PreparedStatement实现数据的修改
     */
    @Test
    public void testUpdate() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "update customers set email=? where id >= ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, "linannan@163.com");
            ps.setInt(2, 14);

            int count = ps.executeUpdate();
            System.out.println("修改了" + count + "条记录");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert ps != null;
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用PreparedStatement实现通用增删修操作
     * 通用：
     *      1.增删改操作都可以使用
     *      2.针对于数据库下的不同的表都可以用
     */
    public void update(String sql, Object... args) {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);

            // 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            int count = ps.executeUpdate();
            System.out.println("影响了" + count + "条记录");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testCommonUpdate() {
//        String sql = "delete from customers where id=?";
//        update(sql, 15);

        // order是关键字，使用`order`
        String sql = "update `order` set order_name=? where order_id=?";
        update(sql, "MM", "4");
    }
}
