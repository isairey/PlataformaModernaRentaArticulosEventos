package me.naffah.partyrentals.constants;

import java.util.HashMap;

//                "(id INTEGER PRIMARY KEY NOT NULL, " +
//                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
//                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

public class CreateTableStatements {
    public static HashMap<String, String> statements = new HashMap<>() {{
        put("users", "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "fullName VARCHAR(50) NOT NULL, " +
                "username VARCHAR(30) UNIQUE NOT NULL, " +
                "phone VARCHAR(15), " +
                "email VARCHAR(320) UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        put("categories", "CREATE TABLE IF NOT EXISTS categories " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "name VARCHAR(50) NOT NULL, " +
                "rentalRate REAL NOT NULL, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        put("products", "CREATE TABLE IF NOT EXISTS products " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "name VARCHAR(70) NOT NULL, " +
                "sku VARCHAR(16), " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "qty INTEGER NOT NULL, " +
                "category_id INTEGER, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL)");

        put("customers", "CREATE TABLE IF NOT EXISTS customers " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "fullName VARCHAR(50) NOT NULL, " +
                "address VARCHAR(120) NOT NULL, " +
                "email VARCHAR(320) UNIQUE, " +
                "phone VARCHAR(15) NOT NULL, " +
                "type VARCHAR(30) NOT NULL, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        put("orders", "CREATE TABLE IF NOT EXISTS orders " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "customerId INTEGER NOT NULL, " +
                "paymentMethod VARCHAR(16) NOT NULL, " +
                "paidAmount REAL NOT NULL, " +
                "startDate TIMESTAMP NOT NULL, " +
                "endDate TIMESTAMP NOT NULL, " +
                "returnedDate TIMESTAMP, " +
                "status VARCHAR(16) NOT NULL, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (customerId) REFERENCES customers (id) ON DELETE SET NULL)");

        put("orderLines", "CREATE TABLE IF NOT EXISTS orderLines " +
                "(id INTEGER PRIMARY KEY NOT NULL, " +
                "orderId INTEGER NOT NULL, " +
                "productId INTEGER NOT NULL, " +
                "name VARCHAR(70) NOT NULL, " +
                "sku VARCHAR(16), " +
                "price REAL NOT NULL, " +
                "qty INTEGER NOT NULL, " +
                "taxAmount REAL NOT NULL, " +
                "totalAmount REAL NOT NULL, " +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (orderId) REFERENCES orders (id) ON DELETE CASCADE)");
    }};
}
