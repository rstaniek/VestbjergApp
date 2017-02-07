package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Machine;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
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


import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class LeasesAddController implements Initializable {

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
    @FXML
    public JFXDatePicker datePicker_returnDate;

    private DBConnect conn;
    private static Employee loggedUser = UserController.getUser();
    private ObservableList<Machine> machinesList;
    private ObservableList<Customer> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker_returnDate.setEditable(false);
        conn = new DBConnect();
        machinesList = FXCollections.observableArrayList();
        retrieveMachines();
        lease_machines_available.getSelectionModel().select(0);
        customersList = FXCollections.observableArrayList();
        retrieveCustomers();
        customers.getSelectionModel().select(0);
    }


    public void retrieveMachines(){
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leaseMachines");
            while (rs.next()) {
                if (rs.getInt("leased") == 0) {
                    lease_machines_available.getItems().add(rs.getString("name"));
                }
                machinesList.add(new Machine(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("pricePerDay"),
                        rs.getBoolean("leased")));
            }
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    public void retrieveCustomers()
    {
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM customers");
            while (rs.next()) {
                String fullName = rs.getString("name") + " " + rs.getString("surname");
                customers.getItems().add(fullName);
                customersList.add(new Customer(rs.getInt("id"),
                        rs.getInt("salesMade"),
                        rs.getDouble("totalSpent"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zip"),
                        rs.getString("email"),
                        rs.getString("phone")));
            }
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    @FXML
    public void btn_add_onClick() {
        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if(datePicker_returnDate.getValue() != null) {
            try {
                conn = new DBConnect();
                conn.upload(String.format("INSERT INTO leases (leaseMachineID, customerID, borrowDate, borrowTime, returnDate, returnTime, employeeID) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s')",
                        getSelectedMachineIndex(),
                        getSelectedCustomerIndex(),
                        dtf_date.format(now),
                        dtf_time.format(now),
                        datePicker_returnDate.getValue(),
                        dtf_time.format(now),
                        loggedUser.getId()));
                conn.upload(String.format("UPDATE leaseMachines SET leased='%1$s' WHERE id='%2$d'",
                        1,
                        getSelectedMachineIndex()));
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                displayMessage(INFORMATION, "Lease added successfully.");
            }
        }
        else displayMessage(ERROR, "Return date needs to be selected before proceeding.");
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

    public int getSelectedMachineIndex() {
        int id = 0;
        for (Machine m : machinesList)
            if(m.getName().equals(lease_machines_available.getSelectionModel().getSelectedItem().toString())) {
            id = m.getId();
        }
        return id;
    }

    public int getSelectedCustomerIndex() {
        int id = 0;
        for (Customer c : customersList)
            if((c.getName() + " " + c.getSurname()).equals(customers.getSelectionModel().getSelectedItem().toString())) {
                id = c.getId();
            }
        return id;
    }
}
