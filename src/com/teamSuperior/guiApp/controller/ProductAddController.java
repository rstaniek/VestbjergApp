package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.service.Contractor;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.*;
import static com.teamSuperior.core.connection.DBConnect.*;
import static com.teamSuperior.guiApp.GUI.Error.*;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;

/**
 * Created by rajmu on 17.01.26.
 */
public class ProductAddController implements Initializable {
    @FXML
    public JFXTextField text_name;
    @FXML
    public JFXTextField text_subname;
    @FXML
    public JFXTextField text_barcode;
    @FXML
    public JFXComboBox<String> comboBox_category;
    @FXML
    public JFXTextField text_price;
    @FXML
    public JFXTextField text_quantity;
    @FXML
    public JFXTextField text_contractorID;
    @FXML
    public Label label_contractor;
    @FXML
    public JFXButton btn_add;
    @FXML
    public JFXButton btn_clear;
    @FXML
    public JFXTextField text_location;

    private ObservableList<Contractor> contractors;
    private DBConnect conn;

    private double validatedPrice;
    private int validatedQuantity;
    private int validatedContractorID;

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
        conn = new DBConnect();
        if(validateField(text_barcode) &&
                validateField(text_contractorID) &&
                validateField(text_name) &&
                validateField(text_price) &&
                validateField(text_quantity) &&
                validateField(text_subname) &&
                validateField(text_location)){
            try {
                conn.upload(String.format("INSERT INTO products (name,subname,barcode,category,price,warehouseLocation,quantity,contractorId) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$.2f','%6$s','%7$d','%8$d')",
                        text_name.getText(),
                        text_subname.getText(),
                        text_barcode.getText(),
                        comboBox_category.getSelectionModel().getSelectedItem(),
                        validatedPrice,
                        text_location.getText(),
                        validatedQuantity,
                        validatedContractorID));
                displayMessage(INFORMATION, "New product added successfully.", String.format("Name: %1$s\nSubname: %2$s\nPrice: %3$.2f\nQuantity: %4$d",
                        text_name.getText(),
                        text_subname.getText(),
                        validatedPrice,
                        validatedQuantity));
                clearFields();
            } catch (SQLException sqlException) {
                displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields(){
        text_contractorID.clear();
        text_barcode.clear();
        text_name.clear();
        text_price.clear();
        text_quantity.clear();
        comboBox_category.getSelectionModel().selectFirst();
        text_subname.clear();
        text_location.clear();
        label_contractor.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatedPrice = 0.0;
        validatedQuantity = 0;
        validatedContractorID = 0;
        label_contractor.setText("");
        contractors = FXCollections.observableArrayList();
        comboBox_category.setItems(retrieveCategoriesData());
        comboBox_category.getSelectionModel().selectFirst();
        retrieveContractorData();

        text_contractorID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                contractorID_onTextChanged(newValue);
            }
        });

        text_price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                price_onTextChanged(newValue);
            }
        });

        text_quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                quantity_onTextChanged(newValue);
            }
        });
    }

    private void quantity_onTextChanged(String newValue) {
        if(!newValue.isEmpty()){
            if(isInteger(newValue)){
                int intValue = Integer.parseInt(newValue);
                if(intValue > 0){
                    if(intValue <= ProductsController.maxCap){
                        validatedQuantity = intValue;
                    } else {
                        validatedQuantity = ProductsController.maxCap;
                        text_quantity.setText(String.valueOf(ProductsController.maxCap));
                        displayMessage(INFORMATION, "Value set is greater than the ma capacity of the storage.", "Resetting the value to fit the max capacity.");
                    }
                } else {
                    displayError(VALUE_LESS_THAN_ZERO);
                    text_quantity.clear();
                }
            } else {
                displayError(TEXT_FIELD_NON_NUMERIC);
                text_quantity.clear();
            }
        }
    }

    private void price_onTextChanged(String newValue) {
        if(!newValue.isEmpty()){
            if(isNumeric(newValue)){
                double doubleValue = Double.parseDouble(newValue);
                if(doubleValue > 0.0){
                    validatedPrice = doubleValue;
                } else {
                    displayError(VALUE_LESS_THAN_ZERO);
                    text_price.clear();
                }
            } else {
                displayError(TEXT_FIELD_NON_NUMERIC);
                text_price.clear();
            }
        }
    }

    private void retrieveContractorData(){
        conn = new DBConnect();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM contractors");
            while (rs.next()){
                Contractor tmp = new Contractor(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zip"),
                        rs.getString("phone"),
                        rs.getString("email"));
                contractors.add(tmp);
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private ObservableList<String> retrieveCategoriesData(){
        conn = new DBConnect();
        ObservableList<String> results = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT category FROM productPictures");
            while (rs.next()){
                if(rs.getString("category") != null){
                    results.add(rs.getString("category"));
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
        return results;
    }

    private void contractorID_onTextChanged(String newValue){
        if(!newValue.isEmpty()){
            if (isInteger(newValue)){
                int intValue = Integer.parseInt(newValue);
                boolean isFound = false;
                for (Contractor contractor : contractors){
                    if(contractor.getId() == intValue){
                        label_contractor.setText(contractor.getName());
                        validatedContractorID = contractor.getId();
                        isFound = true;
                    }
                }
                if(!isFound){
                    displayMessage(WARNING, "Such contractor ID doesn't exist!", "Please check validity of this field content.");
                    label_contractor.setText("");
                    text_contractorID.clear();
                }
            } else {
                displayError(TEXT_FIELD_NON_NUMERIC);
            }
        }
    }
}
