package me.naffah.partyrentals.services;

import me.naffah.partyrentals.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriesService {

    public void add(Category category) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO categories (name, rentalRate) VALUES (?, ?)");
        ps.setString(1, category.getName());
        ps.setDouble(2, category.getRentalRate());
        ps.execute();
        conn.close();
    }

    public ArrayList<Category> get(String fetchLocation) throws SQLException {
        ArrayList<Category> categoryArrayList = new ArrayList<>();

        Connection conn = new DBService().connect();
        PreparedStatement ps = null;

        switch (fetchLocation) {
            case "all" -> ps = conn.prepareStatement("SELECT * FROM categories");
            case "last" -> ps = conn.prepareStatement("SELECT * FROM categories ORDER BY id DESC LIMIT 1;");
            default -> {
            }
        }

        assert ps != null;
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Category category = new Category();

            category.setId(rs.getInt(1));
            category.setName(rs.getString(2));
            category.setRentalRate(rs.getDouble(3));
            category.setCreatedDate(rs.getDate(4));
            category.setModifiedDate(rs.getDate(5));

            categoryArrayList.add(category);
        }
        conn.close();

        return categoryArrayList;
    }

    public void update(Category category) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("UPDATE categories SET name=?, rentalRate=? WHERE id=?");
        ps.setString(1, category.getName());
        ps.setDouble(2, category.getRentalRate());
        ps.setInt(3, category.getId());
        ps.execute();
        conn.close();
    }

    public void delete(int id) throws SQLException {
        Connection conn = new DBService().connect();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM categories WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
        conn.close();
    }
}
