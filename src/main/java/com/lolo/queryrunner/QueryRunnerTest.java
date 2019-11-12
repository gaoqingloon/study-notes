package com.lolo.queryrunner;

import com.lolo.bean.Customer;
import com.lolo.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {

    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtils.getC3P0Connection();
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            // 调用如下的update()方法实现增删改的操作
            runner.update(conn, sql, "楠", "nan@gmail.com", "1993-10-27");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JDBCUtils.closeResource(conn, null);
    }

    /**
     * 查询表中的一条记录：BeanHandler
     */
    @Test
    public void testQueryInstance() throws Exception {
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";

        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, handler, 14);

        System.out.println(customer);

        JDBCUtils.closeResource(conn, null);
    }

    /**
     * 查询表中的多条记录构成的集合：BeanListHandler
     */
    @Test
    public void testQueryForList() throws Exception {
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id <= ?";

        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> customers = runner.query(conn, sql, handler, 14);

        for (Customer customer : customers) {
            System.out.println(customer);
        }

        JDBCUtils.closeResource(conn, null);
    }

    /**
     * 查询表中的一条记录，以key-value的方式显示：MapHandler
     */
    @Test
    public void testQueryForMap() throws Exception {
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";

        MapHandler handler = new MapHandler();
        Map<String, Object> map = runner.query(conn, sql, handler, 14);

        System.out.println(map);
        //{id=14, name=楠楠, email=linannan@163.com, birth=1993-10-27}

        JDBCUtils.closeResource(conn, null);
    }

    /**
     * 查询一些特殊数据：查询最大值、平均数：BeanListHandler
     */
    @Test
    public void testQueryScalarValue() throws Exception {
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();
        ScalarHandler handler = new ScalarHandler();

        // 需求1：查询birth的最大值
        String sql1 = "select max(birth) from customers";
        Date birth = (Date) runner.query(conn, sql1, handler);
        System.out.println(birth);  //2014-06-15

        // 需求2：查询数据的总条数
        String sql2 = "select count(*) from customers";
        long count = (long) runner.query(conn, sql2, handler);
        System.out.println(count);  //29

        JDBCUtils.closeResourceWithDbUtils(conn, null, null);
    }
}
