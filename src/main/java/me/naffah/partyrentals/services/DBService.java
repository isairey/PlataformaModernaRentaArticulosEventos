package me.naffah.partyrentals.services;

import me.naffah.partyrentals.constants.CreateTableStatements;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DBService {

    public static void logException(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

    public Connection connect() {
        Connection conn = null;

        try {
            String projectDir = System.getProperty("user.dir");
            String url = "jdbc:sqlite://" + projectDir + "/partyrentals.db";

            conn = DriverManager.getConnection(url);
            logConnectionInfo(conn);

        } catch (SQLException e) {
            logException("Error connecting to the database", e);
        }

        return conn;
    }

    public void logConnectionInfo(Connection conn) {
        if (conn != null) {
            try {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Connected to database");
                System.out.println("Driver Name: " + meta.getDriverName());
                System.out.println("Database Product: " + meta.getDatabaseProductName());
            } catch (SQLException e) {
                logException("Error fetching database metadata", e);
            }
        }
    }

    public void createTables(Connection conn) {
        // Table creation queries
        HashMap<String, String> statements = CreateTableStatements.statements;

        // Execute each statement to create a table if not already created
        for (Map.Entry<String, String> entry : statements.entrySet()) {
            String tableName = entry.getKey();
            String tableCreationStatements = entry.getValue();

            try {
                // Check if table already exists
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getTables(null, null, tableName, null);

                if (rs.next()) {
                    // Table exists
                    System.out.println("Table with the name '" + tableName + "' exists!");
                } else {
                    // Table does not exist - create table
                    Statement stmt = conn.createStatement();
                    stmt.execute(tableCreationStatements);
                    System.out.println("Table '" + tableName + "' successfully created!");

                    // Create admin user for employees table
                    if (Objects.equals(tableName, "users")) {
                        stmt.execute("INSERT INTO users (fullName, username, password) VALUES ('Administrator', 'admin', 'admin')");
                    }
                }


            } catch (SQLException e) {
                logException("Error when creating tables", e);
            }
        }
    }
}
