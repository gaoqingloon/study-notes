package com.lolo.preparedstatement;

import com.lolo.bean.Customer;
import com.lolo.bean.Order;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对于不同的表，使用PreparedStatement实现通用的查询操作
 *
 * 体会：
 * 两种思想：
 *      1. 面向接口编程
 *      2. ORM 思想
 *
 * 两种技术：
 *      1. 结果集的元数据：ResultSetMetaData
 *      2. 反射的技术
 */
public class PreparedStatementTest1 {

    @Test
    public void getGetForList() {
        String sql = "select id,name from customers where id<=?";
        List<Customer> customers = getForList(Customer.class, sql, 14);
        /*for (Customer customer : customers) {
            System.out.println(customer);
        }*/
        customers.forEach(System.out::println);

//        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id=?";
//        Order order = getInstance(Order.class, sql1, 1);
//        System.out.println(order);
    }

    /**
     * 通用的查询，返回多条记录
     */
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<>();
        try {
            // 1. 获取连接
            conn = JDBCUtils.getConnection();
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
            while (rs.next()) {  // 处理行
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {  // 处理列
                    Object columnValue = rs.getObject(i + 1);  // 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);  // 获取结果集中列的名称
                    // 反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    @Test
    public void getGetInstance() {
        String sql = "select id,name from customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 14);
        System.out.println(customer);

        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id=?";
        Order order = getInstance(Order.class, sql1, 1);
        System.out.println(order);
    }

    /**
     * 通用的查询，返回一条记录
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1. 获取连接
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
