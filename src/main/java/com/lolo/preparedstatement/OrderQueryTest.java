package com.lolo.preparedstatement;

import com.lolo.bean.Order;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于order表的查询操作
 */
public class OrderQueryTest {

    @Test
    public void testGetInstance() {
        //String sql = "select order_id,order_name,order_date from `order` where order_id=?";
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = getInstance(sql, 1);
        System.out.println(order);
        // java.lang.NoSuchFieldException: order_id
    }

    public Order getInstance(String sql, Object... args) {
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
            if (rs.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);  // 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);  // 获取结果集中列的名称
                    // 反射
                    Class<Order> clazz = Order.class;
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void testQuery1() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1. 获取连接
            conn = JDBCUtils.getConnection();
            // 2. 提供sql语句（包含占位符）
            //String sql = "SELECT id,name,email,birth FROM customers WHERE id=?";
            String sql = "SELECT order_id,order_name,order_date FROM `order` WHERE order_id=?";
            // 3. 预编译sql语句
            ps = conn.prepareStatement(sql);
            // 4. 填充占位符
            ps.setInt(1, 1);
            // 5. 调用executeQuery()，获取结果集
            rs = ps.executeQuery();
            // 6. 处理结果集
            //(1)判断指针的下一个位置是否有数据
            //(2)如果返回true，指针下移；否则不下移
            if (rs.next()) {
                int orderId = rs.getInt(1);
                String orderName = rs.getString(2);
                Date orderDate = rs.getDate(3);

                // 封装为对象
                Order order = new Order(orderId, orderName, orderDate);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
