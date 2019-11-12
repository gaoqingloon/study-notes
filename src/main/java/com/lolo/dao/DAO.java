package com.lolo.dao;

import com.lolo.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: data(base) access object 数据访问对象
 */
public class DAO {

    // 通用的查询，返回多条记录 （version 2.0:考虑到事务）
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<>();

        try {
            // 1.预编译sql语句
            ps = conn.prepareStatement(sql);

            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 3.调用executeQuery()，获取结果集
            rs = ps.executeQuery();

            // 4.获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 5.获取结果集中列的个数
            int columnCount = rsmd.getColumnCount();

            // 6.处理结果集
            while (rs.next()) {// ①判断指针的下一个位置是否有数据 ②如果返回true,指针下移。如果返回false,指针不下移。

                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {// 处理列
                    Object columnValue = rs.getObject(i + 1);// 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);// 获取结果集中列的别名

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
            // 7.关闭资源
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    // 通用的查询，返回一条记录 （version 2.0:考虑到事务）
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1.预编译sql语句
            ps = conn.prepareStatement(sql);

            // 2.填充占位符
            for (int i = 0; i < args.length; i++)
                ps.setObject(i + 1, args[i]);

            // 3.调用executeQuery()，获取结果集
            rs = ps.executeQuery();

            // 4.获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 5.获取结果集中列的个数
            int columnCount = rsmd.getColumnCount();

            // 6.处理结果集
            if (rs.next()) {// ①判断指针的下一个位置是否有数据 ②如果返回true,指针下移。如果返回false,指针不下移。

                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {// 处理列
                    Object columnValue = rs.getObject(i + 1);// 获取列值
                    String columnLabel = rsmd.getColumnLabel(i + 1);// 获取结果集中列的别名

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
            // 7.关闭资源
            JDBCUtils.closeResource(null, ps, rs);
        }

        return null;
    }

    // 通用的增删改 （version 2.0:考虑到事务）
    public void update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            // 1.
            ps = conn.prepareStatement(sql);

            // 2.填充占位符
            for (int i = 0; i < args.length; i++)
                ps.setObject(i + 1, args[i]);

            // 3.执行
            int count = ps.executeUpdate();

            System.out.println("影响了" + count + "条数据");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.连接一定不要关闭！
            JDBCUtils.closeResource(null, ps, null);
        }
    }
}
