package me.naffah.partyrentals.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import me.naffah.partyrentals.PartyRentalsApp;
import me.naffah.partyrentals.services.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Alert a = new Alert(Alert.AlertType.ERROR);

        AuthService authService = new AuthService();
        // True means matching username and password is found
        boolean authStatus = authService.authenticate(username, password);

        if (authStatus) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Login Successful!");
            a.show();

            a.setOnCloseRequest(e -> {
                // Create a stage for dashboard view and mount its scene onto it
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(PartyRentalsApp.class.getResource("views/dashboard-view.fxml"));

                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                primaryStage.setScene(scene);
                primaryStage.show();

                // get a handle to the auth view stage and close
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
            });
        } else {
            a.setContentText("Authentication Failed!");
            a.show();
        }

    }
}