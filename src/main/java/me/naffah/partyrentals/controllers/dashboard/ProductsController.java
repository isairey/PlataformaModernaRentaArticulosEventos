package me.naffah.partyrentals.controllers.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.models.Product;
import me.naffah.partyrentals.services.CategoriesService;
import me.naffah.partyrentals.services.ProductService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    public TextField nameField;
    public TextField priceField;
    public TextField skuField;
    public TextField qtyField;
    public ComboBox<Category> categoryCombobox;
    public TextArea descriptionArea;
    public TableView<Product> productsTable;
    public TableColumn<Product, Integer> idCol;
    public TableColumn<Product, String> nameCol;
    public TableColumn<Product, String> skuCol;
    public TableColumn<Product, Double> priceCol;
    public TableColumn<Product, Integer> qtyCol;
    public TableColumn<Product, Integer> categoryCol;
    public TableColumn<Product, Date> createdDateCol;
    public TableColumn<Product, Date> modifiedDateCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductService productService = new ProductService();
        CategoriesService categoriesService = new CategoriesService();

        try {
            ArrayList<Product> products = productService.get("all");
            productObservableList.addAll(products);

            ArrayList<Category> categories = categoriesService.get("all");
            categoryObservableList.addAll(categories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //make sure the property value factory should be exactly same as the e.g getId from your model class
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        skuCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        modifiedDateCol.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
        productsTable.setItems(productObservableList);

        Callback<ListView<Category>, ListCell<Category>> cellFactory = lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        categoryCombobox.setButtonCell(cellFactory.call(null));
        categoryCombobox.setCellFactory(cellFactory);
        categoryCombobox.setItems(categoryObservableList);


        // Update fields with the selected row data
        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedProduct = newSelection;  // Save selected product to variable
            nameField.setText(newSelection.getName());
            skuField.setText(newSelection.getSku());
            descriptionArea.setText(newSelection.getDescription());
            priceField.setText(String.valueOf(newSelection.getPrice()));
            qtyField.setText(String.valueOf(newSelection.getQty()));

            Category selectedCategory = categoryObservableList.stream()
                            .filter(category -> category.getId() == newSelection.getCategoryId())
                            .findFirst()
                            .orElse(null);
            categoryCombobox.setValue(selectedCategory);
        });
    }

    private final ObservableList<Category> categoryObservableList = FXCollections.observableArrayList();
    private Product selectedProduct = null;
    // This holds the table data
    private final ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    public void onAddButtonClick(ActionEvent event) throws SQLException {
        Category category = categoryCombobox.getValue();

        if (category != null) {
            String name = nameField.getText();
            String sku = skuField.getText();
            String description = descriptionArea.getText();
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            // Add to db
            ProductService productService = new ProductService();
            productService.add(new Product(name, sku, description, price, qty, category.getId()));

            // Get last category from db and update TableView
            Product lastProduct = productService.get("last").get(0);
            productObservableList.add(lastProduct);
        } else {
            // TODO: Handle error message
        }

    }

    public void onUpdateButtonClick(ActionEvent event) throws SQLException {
        if (selectedProduct == null) return;

        String name = nameField.getText();
        String sku = skuField.getText();
        String description = descriptionArea.getText();
        double price = Double.parseDouble(priceField.getText());
        int qty = Integer.parseInt(qtyField.getText());
        Category category = categoryCombobox.getValue();

        // Update object
        Product productToUpdate = selectedProduct;
        productToUpdate.setName(name);
        productToUpdate.setSku(sku);
        productToUpdate.setDescription(description);
        productToUpdate.setPrice(price);
        productToUpdate.setQty(qty);
        productToUpdate.setCategoryId(category.getId());

        // Update record in db
        ProductService productService = new ProductService();
        productService.update(productToUpdate);

        int index = productObservableList.indexOf(selectedProduct);
        productObservableList.set(index, productToUpdate);
    }

    public void onDeleteButtonClick(ActionEvent event) throws SQLException {
        if (selectedProduct == null) return;

        // Delete category from db
        Product productToDelete = selectedProduct;
        ProductService productService = new ProductService();
        productService.delete(productToDelete.getId());

        // Remove from TableView
        productObservableList.remove(productToDelete);
    }
}
