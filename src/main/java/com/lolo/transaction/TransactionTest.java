package com.lolo.transaction;

import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 1. 事务：一组逻辑操作单元，依数据从一种状态变换到另一种状态
 * 2. 事务处理的原则：
 * 保证所有事务作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久的保存下来；
 * 要么数据库管理系统将放弃所做的所有操作，整个事务回滚(rollback)到最初状态。
 */
public class TransactionTest {

    /**
     * 通用的查询，返回一条记录（version 2.0）
     */
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 2. 预编译sql语句
            ps = conn.prepareStatement(sql);
            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4. 调用executeQuery()，获取结果集
            rs = ps.executeQuery();
            // 5. 获取结果集中的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // 6. 处理结果集
            if (rs.next()) {  // 处理行
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {  // 处理列
                    Object columnValue = rs.getObject(i + 1);  // 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);  // 获取结果集中列的名称
                    // 反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    @Test
    public void testUpdateWithTx() {

        String sql1 = "update user_table set balance=balance-100 where user=?";
        String sql2 = "update user_table set balance=balance+100 where user=?";

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            // 1. 设置不能自动提交数据
            conn.setAutoCommit(false);

            // 过程一：
            updateWithTx(conn, sql1, "AA");

            // 模拟网络异常
            System.out.println(10 / 0);

            // 过程二：
            updateWithTx(conn, sql2, "BB");

            // 2. 提交数据
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            // 3. 回滚数据
            try {
                assert conn != null;
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(conn, null);
        }

        System.out.println("转账成功");
    }

    /**
     * 通用增删改（version 2.0）
     */
    public void updateWithTx(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
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
            // 连接一定不要关闭
            JDBCUtils.closeResource(null, ps);
        }
    }

    /**
     * 1. 数据一旦提交，不可回滚。
     * 2. 哪些操作，会导致数据的提交：
     * 情况一：执行的DML操作，默认情况下，一旦执行完，就会自动提交数据。（set autocommit=false）
     * 情况二：一旦断开数据库的连接，也会提交之前未提交的数据。
     */

    /*
     * 例子：
     * AA 向 BB 转账100
     * 过程1：
     * update user_table set balance=balance-100 where id=AA;
     * 过程2：
     * update user_table set balance=balance+100 where id=BB;
     */
    @Test
    public void testUpdate() {

        String sql1 = "update user_table set balance=balance-100 where user=?";
        String sql2 = "update user_table set balance=balance+100 where user=?";

        update(sql1, "AA");

        // 模拟网络异常
        System.out.println(10 / 0);

        update(sql2, "BB");
        System.out.println("转账成功");
    }

    /**
     * 通用增删改（version 1.0）
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
}
