package me.naffah.partyrentals.services;

import me.naffah.partyrentals.models.CartItem;
import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.models.Customer;
import me.naffah.partyrentals.models.Order;

import java.sql.*;
import java.util.ArrayList;

public class RentalService {

    public ArrayList<Order> getOrders(String fetchLocation) throws SQLException {
        ArrayList<Order> orderArrayList = new ArrayList<>();

        Connection conn = new DBService().connect();
        PreparedStatement ps = null;

        switch (fetchLocation) {
            case "all" -> ps = conn.prepareStatement("SELECT * FROM orders");
            case "last" -> ps = conn.prepareStatement("SELECT * FROM orders ORDER BY id DESC LIMIT 1;");
            default -> {
            }
        }

        assert ps != null;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Order order = new Order();

            order.setId(rs.getInt(1));
            order.setCustomer(rs.getInt(2));
            order.setPaymentMethod(rs.getString(3));
            order.setPaidAmount(rs.getDouble(4));
            order.setStartDate(rs.getDate(5));
            order.setEndDate(rs.getDate(6));
            order.setReturnedDate(rs.getDate(7));
            order.setStatus(rs.getString(8));
            order.setCreatedDate(rs.getDate(9));
            order.setModifiedDate(rs.getDate(10));

            orderArrayList.add(order);
        }
        conn.close();

        return orderArrayList;
    }

    public void createRentalOrder(Customer customer, Date startDate, Date endDate, String paymentMethod, double paidAmount, ArrayList<CartItem> cartItemList) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (customerId, paymentMethod, paidAmount, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setInt(1, customer.getId());
        ps.setString(2, "CASH");
        ps.setDouble(3, paidAmount);
        ps.setDate(4, startDate);
        ps.setDate(5, endDate);
        ps.setString(6, "RENTED");
        ps.execute();

        ArrayList<Order> lastOrder = this.getOrders("last");

        for (CartItem item: cartItemList) {
            PreparedStatement ps1 = conn.prepareStatement("INSERT INTO orderLines (orderId, productId, name, sku, price, qty, taxAmount, totalAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps1.setInt(1, lastOrder.get(0).getId());
            ps1.setInt(2, item.getProduct().getId());
            ps1.setString(3, item.getProduct().getName());
            ps1.setString(4, item.getProduct().getSku());
            ps1.setDouble(5, item.getPrice());
            ps1.setInt(6, item.getQty());
            ps1.setDouble(7, item.getTax());
            ps1.setDouble(8, item.getTotal());
            ps1.execute();

            PreparedStatement ps2 = conn.prepareStatement("UPDATE products SET qty=? WHERE id=?");
            int updatedQty = item.getProduct().getQty() - item.getQty();
            ps2.setInt(1, updatedQty);
            ps2.setInt(2, item.getProduct().getId());
        }

        conn.close();
    }
}
