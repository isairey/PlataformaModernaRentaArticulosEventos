package me.naffah.partyrentals.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class DashboardController {
    @FXML
    private BorderPane dashboardBorderPane;

    @FXML
    private void handleShowView(ActionEvent e) {
        String view = (String) ((Node)e.getSource()).getUserData();
        System.out.println(view);
        URL url = getClass().getResource(view);
        System.out.println(url);
        loadFXML(url);
    }

    private void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            dashboardBorderPane.setCenter(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
