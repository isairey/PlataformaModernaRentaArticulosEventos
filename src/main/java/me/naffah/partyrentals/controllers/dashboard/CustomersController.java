package me.naffah.partyrentals.controllers.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.models.Customer;
import me.naffah.partyrentals.models.CustomerType;
import me.naffah.partyrentals.services.CategoriesService;
import me.naffah.partyrentals.services.CustomersService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {
    public TextField fullNameField;
    public ComboBox<String> typeCombobox;
    public TextField phoneField;
    public TextField addressField;
    public TextField emailField;
    public TableView<Customer> customersTable;
    public TableColumn<Customer, Integer> idCol;
    public TableColumn<Customer, String> fullNameCol;
    public TableColumn<Customer, String> addressCol;
    public TableColumn<Customer, String> emailCol;
    public TableColumn<Customer, String> phoneCol;
    public TableColumn<Customer, String> typeCol;
    public TableColumn<Customer, Date> createdDateCol;
    public TableColumn<Customer, Date> modifiedDateCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomersService customersService = new CustomersService();
        ArrayList<Customer> customers;

        try {
            customers = customersService.get("all");
            customerObservableList.addAll(customers);  // Populate table
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        typeCombobox.setItems(FXCollections.observableArrayList("Regular", "Government", "Resort"));

        //make sure the property value factory should be exactly same as the e.g getId from your model class
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        modifiedDateCol.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
        customersTable.setItems(customerObservableList);

        // Update fields with the selected row data
        customersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCustomer = newSelection;  // Save selected category to variable
            fullNameField.setText(newSelection.getFullName());
            addressField.setText(String.valueOf(newSelection.getAddress()));
            emailField.setText(String.valueOf(newSelection.getEmail()));
            phoneField.setText(String.valueOf(newSelection.getPhone()));
            typeCombobox.setValue(newSelection.getType());
        });
    }

    private Customer selectedCustomer = null;
    // This holds the table data
    private final ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    public void onAddButtonClick() throws SQLException {
        String type = (String) typeCombobox.getValue();

        if (type != null) {
            String fullName = fullNameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            // Add to db
            CustomersService customersService = new CustomersService();
            customersService.add(new Customer(fullName, address, email, phone, type));

            // Get last customer from db and update TableView
            Customer lastCustomer = customersService.get("last").get(0);
            customerObservableList.add(lastCustomer);
        } else {
            // TODO: Display error
        }
    }

    public void onUpdateButtonClick() throws SQLException {
        if (selectedCustomer == null) return;

        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String type = (String) typeCombobox.getValue();

        // Update object
        Customer customerToUpdate = selectedCustomer;
        customerToUpdate.setFullName(fullName);
        customerToUpdate.setAddress(address);
        customerToUpdate.setEmail(email);
        customerToUpdate.setPhone(phone);
        customerToUpdate.setType(type);

        // Update record in db
        CustomersService customersService = new CustomersService();
        customersService.update(customerToUpdate);

        int index = customerObservableList.indexOf(selectedCustomer);
        customerObservableList.set(index, customerToUpdate);
    }

    public void onDeleteButtonClick() throws SQLException {
        if (selectedCustomer == null) return;

        // Delete category from db
        Customer customerToDelete = selectedCustomer;
        CustomersService customersService = new CustomersService();
        customersService.delete(customerToDelete.getId());

        // Remove from TableView
        customerObservableList.remove(customerToDelete);
    }
}
