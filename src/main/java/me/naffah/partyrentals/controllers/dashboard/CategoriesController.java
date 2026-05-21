package me.naffah.partyrentals.controllers.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import me.naffah.partyrentals.models.Category;
import me.naffah.partyrentals.services.CategoriesService;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {
    public VBox parentVBox;
    public TextField nameField;
    public TextField rentalRateField;
    public TableView<Category> categoriesTable;
    public TableColumn<Category, Integer> idCol;
    public TableColumn<Category, String> nameCol;
    public TableColumn<Category, Double> rentalRateCol;
    public TableColumn<Category, Date> createdDateCol;
    public TableColumn<Category, Date> modifiedDateCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CategoriesService categoriesService = new CategoriesService();
        ArrayList<Category> categories;

        try {
            categories = categoriesService.get("all");
             categoryObservableList.addAll(categories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //make sure the property value factory should be exactly same as the e.g getId from your model class
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rentalRateCol.setCellValueFactory(new PropertyValueFactory<>("rentalRate"));
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        modifiedDateCol.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
        categoriesTable.setItems(categoryObservableList);

        // Update fields with the selected row data
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCategory = newSelection;  // Save selected category to variable
            nameField.setText(newSelection.getName());
            rentalRateField.setText(String.valueOf(newSelection.getRentalRate()));
        });
    }

    private Category selectedCategory = null;
    // This holds the table data
    private final ObservableList<Category> categoryObservableList = FXCollections.observableArrayList();

    public void onAddButtonClick() throws SQLException {
        String name = nameField.getText();
        double rentalRate = Double.parseDouble(rentalRateField.getText());

        // Add to db
        CategoriesService categoriesService = new CategoriesService();
        categoriesService.add(new Category(name, rentalRate));

        // Get last category from db and update TableView
        Category lastCategory = categoriesService.get("last").get(0);
        categoryObservableList.add(lastCategory);
    }

    public void onUpdateButtonClick() throws SQLException {
        if (selectedCategory == null) return;

        String name = nameField.getText();
        double rentalRate = Double.parseDouble(rentalRateField.getText());

        // Update object
        Category categoryToUpdate = selectedCategory;
        categoryToUpdate.setName(name);
        categoryToUpdate.setRentalRate(rentalRate);

        // Update record in db
        CategoriesService categoriesService = new CategoriesService();
        categoriesService.update(categoryToUpdate);

        int index = categoryObservableList.indexOf(selectedCategory);
        categoryObservableList.set(index, categoryToUpdate);
    }

    public void onDeleteButtonClick() throws SQLException {
        if (selectedCategory == null) return;

        // Delete category from db
        Category categoryToDelete = selectedCategory;
        CategoriesService categoriesService = new CategoriesService();
        categoriesService.delete(categoryToDelete.getId());

        // Remove from TableView
        categoryObservableList.remove(categoryToDelete);
    }
}
