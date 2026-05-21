package me.naffah.partyrentals.services;

import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomersService {
    public ArrayList<Customer> get(String fetchLocation) throws SQLException {
        ArrayList<Customer> customerArrayList = new ArrayList<>();

        Connection conn = new DBService().connect();
        PreparedStatement ps = null;

        switch (fetchLocation) {
            case "all" -> ps = conn.prepareStatement("SELECT * FROM customers");
            case "last" -> ps = conn.prepareStatement("SELECT * FROM customers ORDER BY id DESC LIMIT 1;");
            default -> {
            }
        }

        assert ps != null;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Customer customer = new Customer();

            customer.setId(rs.getInt(1));
            customer.setFullName(rs.getString(2));
            customer.setAddress(rs.getString(3));
            customer.setEmail(rs.getString(4));
            customer.setPhone(rs.getString(5));
            customer.setType(rs.getString(6));
            customer.setCreatedDate(rs.getDate(7));
            customer.setModifiedDate(rs.getDate(8));

            customerArrayList.add(customer);
        }
        conn.close();

        return customerArrayList;
    }

    public void add(Customer customer) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (fullName, address, email, phone, type) VALUES (?, ?, ? ,? ,?)");
        ps.setString(1, customer.getFullName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getEmail());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getType());
        ps.execute();
        conn.close();
    }

    public void update(Customer customer) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("UPDATE customers SET fullName=?, address=?, email=?, phone=?, type=? WHERE id=?");
        ps.setString(1, customer.getFullName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getEmail());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getType());
        ps.setInt(6, customer.getId());
        ps.execute();
        conn.close();
    }

    public void delete(int id) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM customers WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
        conn.close();
    }
}
