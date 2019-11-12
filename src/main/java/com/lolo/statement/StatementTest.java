package com.lolo.statement;

import com.lolo.bean.User;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

public class StatementTest {

    /**
     * 使用Statement的弊端：
     *      1. 需要拼接sql语句，
     *      2. 并且存在sql注入的问题
     *
     * create table user_table (user varchar(10), password varchar(10), balance int);
     * insert into user_table values("AA", "123456", 1000);
     * insert into user_table values("BB", "654321", 1000);
     * insert into user_table values("CC", "abcd", 2000);
     * insert into user_table values("DD", "abcder", 3000);
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("用户名：");
        String userName = scan.nextLine();
        System.out.println("密码：");
        String password = scan.nextLine();

//        String sql = "select user, password from user_table where user='"
//                + userName + "' and password='" + password + "'";
        //System.out.println(sql);
        //select user, password from user_table where user='AA' and password='123456'

        //select user, password from user_table where user='1' or ' and password='='1' or '1'='1';
        //user:1' or
        //password:='1' or '1'='1
        /*
            用户名：
            1' or
            密码：
            ='1' or '1'='1
            登陆成功
         */
        String sql = "select user, password from user_table where user='"
                + userName + "' and password='" + password + "'";

        User user = get(sql, User.class);
        if (user != null) {
            System.out.println("登陆成功");
        } else {
            System.out.println("用户名或密码错误");
        }
    }

    public static <T> T get(String sql, Class<T> clazz) {

        T t;

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            // 获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 1. 获取列的名称
                    //String columnName = rsmd.getColumnName(i+1);

                    // 1. 获取列的别名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    // 2. 根据列名获取对应数据表中的数据
                    Object columnValue = rs.getObject(columnName);

                    // 3. 将数据表中的数据，封装到对象
                    Field field = clazz.getDeclaredField(columnName);

                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
