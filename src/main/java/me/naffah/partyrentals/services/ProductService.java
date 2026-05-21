package me.naffah.partyrentals.services;
import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {
    public ArrayList<Product> get(String fetchLocation) throws SQLException {
        ArrayList<Product> productArrayList = new ArrayList<>();

        Connection conn = new DBService().connect();
        PreparedStatement ps = null;

        switch (fetchLocation) {
            case "all" -> ps = conn.prepareStatement("SELECT * FROM products");
            case "last" -> ps = conn.prepareStatement("SELECT * FROM products ORDER BY id DESC LIMIT 1;");
            default -> {
            }
        }

        assert ps != null;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Product product = new Product();

            product.setId(rs.getInt(1));
            product.setName(rs.getString(2));
            product.setSku(rs.getString(3));
            product.setDescription(rs.getString(4));
            product.setPrice(rs.getDouble(5));
            product.setQty(rs.getInt(6));
            product.setCategoryId(rs.getInt(7));
            product.setCreatedDate(rs.getDate(8));
            product.setModifiedDate(rs.getDate(9));

            productArrayList.add(product);
        }
        conn.close();

        return productArrayList;
    }

    public void add(Product product) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO products (name, sku, description, price, qty, category_id) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, product.getName());
        ps.setString(2, product.getSku());
        ps.setString(3, product.getDescription());
        ps.setDouble(4, product.getPrice());
        ps.setInt(5, product.getQty());
        ps.setInt(6, product.getCategoryId());
        ps.execute();
        conn.close();
    }

    public void update(Product product) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("UPDATE products SET name=?, sku=?, description=?, price=?, qty=?, category_id=? WHERE id=?");
        ps.setString(1, product.getName());
        ps.setString(2, product.getSku());
        ps.setString(3, product.getDescription());
        ps.setDouble(4, product.getPrice());
        ps.setInt(5, product.getQty());
        ps.setInt(6, product.getCategoryId());
        ps.setInt(7, product.getId());
        ps.execute();
        conn.close();
    }

    public void delete(int id) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
        conn.close();
    }
}
