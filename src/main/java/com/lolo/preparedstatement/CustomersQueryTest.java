package com.lolo.preparedstatement;

import com.lolo.bean.Customer;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于Customers表的通用查询操作
 *
 * 通用：查询的字段个数、字段名不确定
 *
 */
public class CustomersQueryTest {

    /**
     * 如果表的字段名和类的属性名不一致，要求：
     * 查询的语句中，使用类的属性名作为字段的别名出现
     */
    @Test
    public void testGetInstance() {
        String sql = "select id,name from customers where id=?";
        Customer customer = getInstance(sql, 14);
        System.out.println(customer);
    }

    public Customer getInstance(String sql, Object... args) {
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
                Customer customer = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);  // 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);  // 获取结果集中列的别名
                    /*if ("id".equals(columnLabel)) {
                        customer.setId((Integer) columnValue);
                    } else if ("name".equals(columnLabel)) {
                        customer.setName((String) columnValue);
                    } else if ("email".equals(columnLabel)) {
                        customer.setEmail(columnLabel);
                    } else {
                        customer.setBirth(Date.valueOf(columnLabel));
                    }*/
                    // 反射
                    Class<Customer> clazz = Customer.class;
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    /**
     * 查询操作
     * ORM 的思想 (object relational mapping)
     *  一个数据表与一个java类对应
     *  表中的一个列与java类的一个属性对应
     *  表中的一个行与java类的一个对象对应
     */
    @Test
    public void testQuery1() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1. 获取连接
            conn = JDBCUtils.getConnection();
            // 2. 提供sql语句（包含占位符）
            String sql = "SELECT id,name,email,birth FROM customers WHERE id=?";
            // 3. 预编译sql语句
            ps = conn.prepareStatement(sql);
            // 4. 填充占位符
            ps.setInt(1, 14);
            // 5. 调用executeQuery()，获取结果集
            rs = ps.executeQuery();
            // 6. 处理结果集
            //(1)判断指针的下一个位置是否有数据
            //(2)如果返回true，指针下移；否则不下移
            if (rs.next()) {
                // int id = rs.getInt(1);
                // String name = rs.getString(2);
                // String email = rs.getString(3);
                // Date birth = rs.getDate(4);

                // 或者
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                // 方式一：
                System.out.println("id: " + id
                        + ", name: " + name
                        + ", email: " + email
                        + ", birth: " + birth);

                // 方式二：Object[]  不建议
                // 方式三：封装为对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
