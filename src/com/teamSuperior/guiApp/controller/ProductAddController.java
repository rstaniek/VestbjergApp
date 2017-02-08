package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.connection.IDataAccessObject;
import com.teamSuperior.core.model.service.Contractor;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.ProductCategory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.isInteger;
import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.TEXT_FIELD_NON_NUMERIC;
import static com.teamSuperior.guiApp.enums.ErrorCode.VALUE_LESS_THAN_ZERO;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Product add controller
 */
public class ProductAddController implements IDataAccessObject<Product, Integer>, Initializable {

    private static ConnectionController<Product, Integer> productConnectionController = new ConnectionController<>(Product.class);
    private static ConnectionController<ProductCategory, Integer> productCategoryConnectionController = new ConnectionController<>(ProductCategory.class);
    private static ConnectionController<Contractor, Integer> contractorConnectionController = new ConnectionController<>(Contractor.class);

    @FXML
    public JFXTextField nameField;
    @FXML
    public JFXTextField subnameField;
    @FXML
    public JFXTextField barcodeField;
    @FXML
    public JFXTextField priceField;
    @FXML
    public JFXTextField quantityField;
    @FXML
    public JFXTextField locationField;

    @FXML
    public JFXComboBox<ProductCategory> categoryComboBox;
    @FXML
    public JFXComboBox<Contractor> contractorComboBox;

    @FXML
    public JFXButton addButton;
    @FXML
    public JFXButton clearButton;

    private double validatedPrice = 0.0;
    private int validatedQuantity = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryComboBox.setItems(FXCollections.observableArrayList(productCategoryConnectionController.getAll()));
        categoryComboBox.getSelectionModel().selectFirst();

        contractorComboBox.setItems(FXCollections.observableArrayList(contractorConnectionController.getAll()));
        contractorComboBox.getSelectionModel().selectFirst();

        priceField.textProperty().addListener((observable, oldValue, newValue) -> validatePrice(newValue));

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> validateQuantity(newValue));
    }

    @FXML
    public void clickAdd() {
        persist(new Product(
                nameField.getText(),
                subnameField.getText(),
                barcodeField.getText(),
                categoryComboBox.getSelectionModel().getSelectedItem(),
                validatedPrice,
                locationField.getText(),
                validatedQuantity,
                contractorComboBox.getSelectionModel().getSelectedItem()
        ));
        displayMessage(INFORMATION, "New product added successfully.", String.format("Name: %1$s\nSubname: %2$s\nPrice: %3$.2f\nQuantity: %4$d",
                nameField.getText(),
                subnameField.getText(),
                validatedPrice,
                validatedQuantity));
    }

    @FXML
    public void clickClear() {
        clearFields();
    }

    private void clearFields() {
        barcodeField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        categoryComboBox.getSelectionModel().selectFirst();
        contractorComboBox.getSelectionModel().selectFirst();
        subnameField.clear();
        locationField.clear();
    }

    private void validateQuantity(String newValue) {
        if (!newValue.isEmpty()) {
            if (isInteger(newValue)) {
                int intValue = Integer.parseInt(newValue);
                if (intValue > 0) {
                    if (intValue <= ProductsController.MAX_CAP) {
                        validatedQuantity = intValue;
                    } else {
                        validatedQuantity = ProductsController.MAX_CAP;
                        quantityField.setText(String.valueOf(ProductsController.MAX_CAP));
                        displayMessage(INFORMATION, "Value set is greater than the ma capacity of the storage.", "Resetting the value to fit the max capacity.");
                    }
                } else {
                    displayError(VALUE_LESS_THAN_ZERO);
                    quantityField.clear();
                }
            } else {
                displayError(TEXT_FIELD_NON_NUMERIC);
                quantityField.clear();
            }
        }
    }

    private void validatePrice(String newValue) {
        if (!newValue.isEmpty()) {
            if (isNumeric(newValue)) {
                double doubleValue = Double.parseDouble(newValue);
                if (doubleValue > 0.0) {
                    validatedPrice = doubleValue;
                } else {
                    displayError(VALUE_LESS_THAN_ZERO);
                    priceField.clear();
                }
            } else {
                displayError(TEXT_FIELD_NON_NUMERIC);
                priceField.clear();
            }
        }
    }

    @Override
    public void persist(Product product) {
        productConnectionController.persist(product);
    }

    @Override
    public Product getById(Integer id) {
        return productConnectionController.getById(id);
    }

    @Override
    public List<Product> getAll() {
        return productConnectionController.getAll();
    }

    @Override
    public void update(Product product) {
        productConnectionController.update(product);
    }

    @Override
    public void delete(Product product) {
        productConnectionController.delete(product);
    }

    @Override
    public void deleteAll() {
        productConnectionController.deleteAll();
    }
}
