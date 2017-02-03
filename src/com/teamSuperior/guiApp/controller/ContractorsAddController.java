package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class ContractorsAddController {
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_address;
    @FXML
    public TextField text_city;
    @FXML
    public TextField text_zip;
    @FXML
    public TextField text_phone;
    @FXML
    public TextField text_email;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_manageContractors;

    private DBConnect conn;

    @FXML
    public void btn_add_onClick() {
        if (validateField(text_address) &&
                validateField(text_city) &&
                validateField(text_email) &&
                validateField(text_name) &&
                validateField(text_phone) &&
                validateField(text_zip)) {
            try {
                conn = new DBConnect();
                conn.upload(String.format("INSERT INTO contractors (name, address, city, zip, phone, email) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s')",
                        text_name.getText(),
                        text_address.getText(),
                        text_city.getText(),
                        text_zip.getText(),
                        text_phone.getText(),
                        text_email.getText()));
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                resetTextFields();
            }
        }
    }

    @FXML
    public void btn_clear_onClick() {
        resetTextFields();
    }

    @FXML
    public void btn_manageContractors_onClick() {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/contractorsManage.fxml"));
                Stage window = new Stage();
                window.setTitle("Manage contractors");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ioex) {
                displayMessage(ERROR, "This page couldn't be loaded", ioex.getMessage());
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    private void resetTextFields() {
        text_address.clear();
        text_city.clear();
        text_email.clear();
        text_name.clear();
        text_phone.clear();
        text_zip.clear();
    }
}
