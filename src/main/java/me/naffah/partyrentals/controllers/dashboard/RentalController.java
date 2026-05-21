package me.naffah.partyrentals.controllers.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import me.naffah.partyrentals.models.CartItem;
import me.naffah.partyrentals.models.Customer;
import me.naffah.partyrentals.models.Product;
import me.naffah.partyrentals.services.CustomersService;
import me.naffah.partyrentals.services.ProductService;
import me.naffah.partyrentals.services.RentalService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RentalController implements Initializable {
    public DatePicker startDateField;
    public DatePicker endDateField;
    public TableView<Product> productsTable;
    public TableColumn<Product, Integer> idCol;
    public TableColumn<Product, String> nameCol;
    public TableColumn<Product, String> skuCol;
    public TableColumn<Product, Integer> qtyCol;
    public TableColumn<Product, Double> priceCol;
    public TableColumn<Product, Integer> categoryCol;
    public Button addToCartButton;
    public TextField itemAmountField;
    public TextField paidAmountField;
    public ComboBox<String> paymentMethodComboBox;
    public Button makeSaleButton;
    public ComboBox<Customer> customerCombobox;
    public TableView<CartItem> cartItemsTable;
    public TableColumn<CartItem, String> iItemCol;
    public TableColumn<CartItem, Integer> iQtyCol;
    public TableColumn<CartItem, Double> iPriceCol;
    public TableColumn<CartItem, Double> iTaxCol;
    public TableColumn<CartItem, Double> iTotalCol;
    public Label totalAmountText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDateField.setValue(LocalDate.now());
        endDateField.setValue(LocalDate.now());
        ProductService productService = new ProductService();
        CustomersService customersService = new CustomersService();

        try {
            ArrayList<Product> products = productService.get("all");
            productObservableList.addAll(products);

            ArrayList<Customer> customers = customersService.get("all");
            customerObservableList.addAll(customers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Products table
        //make sure the property value factory should be exactly same as the e.g getId from your model class
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        skuCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        productsTable.setItems(productObservableList);

        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedProduct = newSelection;  // Save selected product to variable
        });

        // Cart items table
        iItemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        iQtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        iPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        iTaxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));
        iTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        cartItemsTable.setItems(cartItemObservableList);

        cartItemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCartItem = newSelection;  // Save selected product to variable
        });

        // populate customers combobox
        Callback<ListView<Customer>, ListCell<Customer>> cellFactory = lv -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getFullName());
            }
        };
        customerCombobox.setButtonCell(cellFactory.call(null));
        customerCombobox.setCellFactory(cellFactory);
        customerCombobox.setItems(customerObservableList);

        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
                "Cash",
                "Card",
                "Transfer"
        ));
    }

    private Product selectedProduct = null;
    // This holds the table data
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();
    private final ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private CartItem selectedCartItem = null;
    private final ObservableList<CartItem> cartItemObservableList = FXCollections.observableArrayList();
    private double totalAmount = 0;

    public void onAddtoCartButtonClick() {
        Customer customer = customerCombobox.getValue();
        String itemAmount = itemAmountField.getText();

        System.out.println(customer);
        System.out.println(itemAmount);

        if (customer != null && itemAmount != null) {
            // Prevent users from selecting more products than available
            if (Integer.parseInt(itemAmount) > selectedProduct.getQty()) {
                // TODO: Show error dialog
                return;
            }

            // Get product data
            int id = selectedProduct.getId();
            String name = selectedProduct.getName();
            String item = id + " - " + name;
            int itemQty = Integer.parseInt(itemAmount);
            double price = selectedProduct.getPrice();
            double taxRate = 0.06;

            LocalDate currentDate = LocalDate.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                // Apply a 5% increase in price for weekends
                price *= 1.05;
            }

            if (Objects.equals(customer.getType(), "Resort")) {
                taxRate = 0.08; // 8% tax rate for resorts
            }

            // Calculate the total price before tax
            double totalPrice = price * itemQty;

            // Calculate tax amount
            double taxAmount = totalPrice * taxRate;

            // Calculate the final total price including tax
            double finalTotalPrice = totalPrice + taxAmount;

            Date startDate = Date.valueOf(startDateField.getValue());
            Date endDate = Date.valueOf(endDateField.getValue());

            // Convert SQL Date objects to LocalDate
            LocalDate localStartDate = startDate.toLocalDate();
            LocalDate localEndDate = endDate.toLocalDate();

            // Calculate duration between the two LocalDate objects
            Duration duration = Duration.between(localStartDate.atStartOfDay(), localEndDate.atStartOfDay());
            long days = duration.toDays();

            totalAmount += finalTotalPrice * days;
            totalAmountText.setText(String.valueOf(totalAmount));
            CartItem cartItem = new CartItem(item, itemQty, price, taxAmount, finalTotalPrice, selectedProduct);
            cartItemObservableList.add(cartItem);

            // Deduct qty from product in products table in rental view
            int index = productObservableList.indexOf(selectedProduct);
            int qty = selectedProduct.getQty();
            selectedProduct.setQty(qty - itemQty);
            productObservableList.set(index, selectedProduct);
        } else {
            // TODO: Display error
        }
    }

    public void onRemoveItemButtonClick() {
        // Check if a cart item is selected
        if (selectedCartItem != null) {
            Product product = selectedCartItem.getProduct();

            for (Product value : productObservableList) {
                if (value.getId() == product.getId()) {
                    int index = productObservableList.indexOf(value);

                    // Update product
                    product.setQty(product.getQty() + selectedCartItem.getQty());
                    productObservableList.set(index, product);
                }
            }

            cartItemObservableList.remove(selectedCartItem);
        } else {
            // TODO: Display error
        }
    }

    public void onMakeSaleButtonClick() throws SQLException {
        double paidAmount = 0;
        if (!Objects.equals(paidAmountField.getText(), "")) paidAmount = Double.parseDouble(paidAmountField.getText());

        if (paidAmount > totalAmount) {
            RentalService rentalService = new RentalService();
            Customer customer = customerCombobox.getValue();
            Date startDate = Date.valueOf(startDateField.getValue());
            Date endDate = Date.valueOf(endDateField.getValue());
            String paymentMethod = paymentMethodComboBox.getValue();
            final ArrayList<CartItem> cartItemList = new ArrayList<>(cartItemObservableList);

            rentalService.createRentalOrder(customer, startDate, endDate, paymentMethod, paidAmount, cartItemList);
        }
    }
}
