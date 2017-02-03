package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.cell.PropertyValueFactory;


import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class LeasesAddController {
    @FXML
    public TextField text_machineID;
    @FXML
    public TextField text_customerName;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_manageLeases;
    //TODO: Some easy to use method for adding the machine and customer
    //TODO: Add date picker for due date

    private DBConnect conn;
    private static Employee loggedUser = UserController.getUser();

    @FXML
    public void btn_add_onClick() {
        if (validateField(text_machineID) && validateField(text_customerName)) {
            DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            try {
                conn = new DBConnect();
                conn.upload(String.format("INSERT INTO leases (leaseMachineID, customerName, borrowDate, borrowTime, employeeID) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s')",
                        text_machineID.getText(),
                        text_customerName.getText(),
                        dtf_date.format(now),
                        dtf_time.format(now),
                        loggedUser.getId()));
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                displayMessage(INFORMATION, "Lease added successfully.");
                resetTextFields();
            }
        }
    }

    @FXML
    public void btn_clear_onClick() {
        resetTextFields();
    }

    @FXML
    public void btn_manageLeases_onClick() {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/leasesManage.fxml"));
                Stage window = new Stage();
                window.setTitle("Manage leases");
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
        text_customerName.clear();
        text_machineID.clear();
    }
}
