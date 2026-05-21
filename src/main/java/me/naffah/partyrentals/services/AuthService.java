package me.naffah.partyrentals.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static me.naffah.partyrentals.services.DBService.logException;

public class AuthService {
    // Checks if the username and password matches
    public boolean authenticate(String username, String password) {
        boolean authStatus = false;

        try {
            DBService dbService = new DBService();
            Connection conn = dbService.connect();

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                authStatus = true;
                System.out.println("User successfully authenticated!");
            } else {
                System.out.println("User credentials incorrect!");
            }

            conn.close();
        } catch (SQLException e) {
            logException("Error when creating tables", e);
        }

        return authStatus;
    }
}
