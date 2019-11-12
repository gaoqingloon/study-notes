package com.lolo.daoUtral;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.lolo.bean.Customer;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

/**
 * CustomerDAO的测试类
 */
public class CustomerDAOTest {

    @Test
    public void testGetAll() throws Exception {
        CustomerDAO dao = new CustomerDAO();
        Connection conn = JDBCUtils.getConnection();

        List<Customer> list = dao.getAll(conn);
        list.forEach(System.out::println);

        JDBCUtils.closeResource(conn, null);
    }

    @Test
    public void testGetOne() throws Exception {
        CustomerDAO dao = new CustomerDAO();
        Connection conn = JDBCUtils.getConnection();
        Customer cust = dao.getById(conn, 8);
        System.out.println(cust);

        JDBCUtils.closeResource(conn, null);
    }

    @Test
    public void testAdd() throws Exception {

        CustomerDAO dao = new CustomerDAO();

        Connection conn = JDBCUtils.getConnection();
        Customer cust = new Customer(1, "任鑫", "ren@126.com", new Date(1242423423L));
        dao.addCustomer(conn, cust);

        JDBCUtils.closeResource(conn, null);
    }
}
