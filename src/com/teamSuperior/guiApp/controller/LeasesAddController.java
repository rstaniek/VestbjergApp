package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Machine;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.tuiApp.modelLayer.LeaseMachine;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;


import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class LeasesAddController implements Initializable {

    @FXML
    public TextField text_customerName;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_manageLeases;
    @FXML
    public ComboBox lease_machines_available;
    @FXML
    public ComboBox customers;
    @FXML
    public TextField price;

    //TODO: Add date picker for due date
    /*TODO: FINISH ADDING THE SHIT INTO THE DATABASE
    get the id from the selected machine*/

    private DBConnect conn;
    private static Employee loggedUser = UserController.getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = new DBConnect();
        btn_manageLeases.setText("titty cunt");
        retrieveMachines();
        retrieveCutomers();
    }


    public void retrieveMachines(){
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leaseMachines");
            while (rs.next())
                lease_machines_available.getItems().add(rs.getString("name"));
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    public void retrieveCutomers()
    {
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM customers");
            while (rs.next())
                customers.getItems().add(rs.getString("name"));
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    @FXML
    public void btn_add_onClick() {
        /*if (validateField(text_machineID) && validateField(text_customerName)) {
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
        }*/
        if(lease_machines_available.getSelectionModel().getSelectedItem() != null && customers.getSelectionModel().getSelectedItem() != null){
            DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            try {
                //conn = new DBConnect();
                //conn.upload(String.format("INSERT INTO leases (leaseMachineID, customerName, borrowDate, borrowTime, employeeID) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s')",
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
        /*text_customerName.clear();
        text_machineID.clear();*/
    }
}
