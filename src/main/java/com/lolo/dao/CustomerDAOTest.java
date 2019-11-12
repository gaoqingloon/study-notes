package com.lolo.dao;

import com.lolo.bean.Customer;
import com.lolo.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * CustomerDAO的测试类
 */
public class CustomerDAOTest {

    @Test
    public void testAdd() throws Exception {

        CustomerDAO dao = new CustomerDAO();
        Connection conn = JDBCUtils.getConnection();

        Customer cust = new Customer(1, "李楠楠1", "lnn1@126.com", new Date(123456344L));
        dao.addCustomer(conn, cust);

        JDBCUtils.closeResource(conn, null);
    }

    @Test
    public void testGetOne() throws Exception {
        CustomerDAO dao = new CustomerDAO();
        Connection conn = JDBCUtils.getConnection();

        Customer customer = dao.getById(conn, 14);
        System.out.println(customer);

        JDBCUtils.closeResource(conn, null);
    }

    @Test
    public void testGetAll() throws Exception {
        CustomerDAO dao = new CustomerDAO();
        //Connection conn = JDBCUtils.getConnection();
        //Connection conn = JDBCUtils.getC3P0Connection();
        Connection conn = JDBCUtils.getDBCPConnection();

        List<Customer> customers = dao.getAll(conn);
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        JDBCUtils.closeResource(conn, null);
    }
}
