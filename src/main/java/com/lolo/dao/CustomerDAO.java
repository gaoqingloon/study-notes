package com.lolo.dao;

import com.lolo.bean.Customer;

import java.sql.Connection;
import java.util.List;

public class CustomerDAO extends DAO {

    /**
     * 向customers表中插入一条数据
     */
    public void addCustomer(Connection conn, Customer cust) {

        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
    }

    /**
     * 删除数据表中指定id对应的数据
     * @param conn
     * @param id
     */
    public void deleteById(Connection conn, int id) {

        String sql = "delete from customers where id = ?";
        update(conn, sql, id);
    }

    /**
     * 修改数据表中的一条数据
     * @param conn
     * @param cust
     */
    public void updateCustomer(Connection conn, Customer cust) {//cust=new Customers(10,"楠楠",..,..);

        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
    }

    /**
     * 查询指定id的对象
     * @param conn
     * @param id
     * @return
     */
    public Customer getById(Connection conn, int id) {

        String sql = "select id,name,email,birth from customers where id = ?";
        return getInstance(conn, Customer.class, sql, id);
    }

    /**
     * 查询表中的所有记录
     * @param conn
     * @return
     */
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        return getForList(conn, Customer.class, sql);
    }
}
