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
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class MachinesAddController {
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_pricePerDay;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_manageMachines;

    private DBConnect conn;

    @FXML
    public void btn_add_onClick() {
        if (validateField(text_pricePerDay) && validateField(text_name)) {
            try {
                conn = new DBConnect();
                conn.upload(String.format("INSERT INTO leaseMachines (name, pricePerDay) VALUES ('%1$s','%2$s')",
                        text_name.getText(),
                        text_pricePerDay.getText()));
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                displayMessage(INFORMATION, "Lease machine added successfully.");
                resetTextFields();
            }
        }
    }

    @FXML
    public void btn_clear_onClick() {
        resetTextFields();
    }

    @FXML
    public void btn_manageMachines_onClick() {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/machinesManage.fxml"));
                Stage window = new Stage();
                window.setTitle("Manage machines");
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
        text_pricePerDay.clear();
        text_name.clear();
    }
}
