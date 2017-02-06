package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Lease;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class LeasesManageController implements Initializable {
    @FXML
    public JFXButton btn_search_clear;
    @FXML
    public JFXTextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public TableView tableView_leases;
    @FXML
    public JFXTextField text_machineID;
    @FXML
    public JFXTextField text_customerID;
    @FXML
    public JFXTextField text_price;
    @FXML
    public JFXDatePicker datePicker_borrowDate;
    @FXML
    public JFXDatePicker datePicker_returnDate;
    @FXML
    public JFXButton btn_save;
    @FXML
    public JFXButton btn_endLease;
    @FXML
    public JFXButton btn_delete;


    private ObservableList<Lease> searchResults;
    private ObservableList<Lease> leases;
    private Lease selectedLease;
    private DBConnect conn;

    //columns
    private TableColumn<Lease, String> leaseMachineIDCOl;
    private TableColumn<Lease, String> customerIDCol;
    private TableColumn<Lease, String> borrowDateCol;
    private TableColumn<Lease, String> borrowTimeCol;
    private TableColumn<Lease, String> returnDateCol;
    private TableColumn<Lease, String> returnTimeCol;
    private TableColumn<Lease, String> priceCol;
    private TableColumn<Lease, String> employeeIDCol;



    private static final String[] leasesCriteria = new String[]{"Lease machine ID", "Customer ID", "Price", "Employee ID"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchResults = FXCollections.observableArrayList();
        leases = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll(leasesCriteria);
        conn = new DBConnect();
        retrieveData();
        initTableColumns(leases);
        selectedLease = (Lease) tableView_leases.getFocusModel().getFocusedItem();
    }

    private void retrieveData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leases");
            while (rs.next()) {
                /*if (
                        rs.getString("id") != null &&
                        rs.getString("leaseMachineID") != null &&
                        rs.getString("customerID") != null &&
                        rs.getString("borrowDate") != null &&
                        rs.getString("borrowTime") != null &&
                        rs.getString("returnDate") != null &&
                        rs.getString("returnTime") != null &&
                        rs.getString("price") != null &&
                        rs.getString("employeeID") != null)*/
                {
                    Lease tmp = new Lease(
                            rs.getInt("id"),
                            rs.getInt("leaseMachineID"),
                            rs.getInt("customerID"),
                            rs.getFloat("price"),
                            rs.getDate("borrowDate"),
                            rs.getTime("borrowTime"),
                            rs.getDate("returnDate"),
                            rs.getTime("returnTime"),
                            rs.getInt("employeeID"));
                    leases.add(tmp);
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<Lease> source) {
        leaseMachineIDCOl = new TableColumn<Lease, String>("Machine ID");
        leaseMachineIDCOl.setMinWidth(80);
        leaseMachineIDCOl.setCellValueFactory(new PropertyValueFactory<>("leaseMachineID"));

        customerIDCol = new TableColumn<Lease, String>("Customer ID");
        customerIDCol.setMinWidth(150);
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        borrowDateCol = new TableColumn<Lease, String>("Borrow date");
        borrowDateCol.setMinWidth(80);
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        borrowTimeCol = new TableColumn<Lease, String>("Borrow time");
        borrowTimeCol.setMinWidth(80);
        borrowTimeCol.setCellValueFactory(new PropertyValueFactory<>("borrowTime"));

        returnDateCol = new TableColumn<Lease, String>("Return date");
        returnDateCol.setMinWidth(80);
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        returnTimeCol = new TableColumn<Lease, String>("Return time");
        returnTimeCol.setMinWidth(80);
        returnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnTime"));

        priceCol = new TableColumn<Lease, String>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        employeeIDCol = new TableColumn<Lease, String>("EmployeeID");
        employeeIDCol.setMinWidth(80);
        employeeIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        tableView_leases.setItems(source);
        tableView_leases.getColumns().addAll(leaseMachineIDCOl, customerIDCol, borrowDateCol, borrowTimeCol, returnDateCol, returnTimeCol, priceCol, employeeIDCol);
    }

    @FXML
    public void tableView_leases_onMouseClicked() {
        selectedLease = (Lease) tableView_leases.getFocusModel().getFocusedItem();
        text_machineID.setText(String.valueOf(selectedLease.getLeaseMachineID()));
        text_customerID.setText(String.valueOf(selectedLease.getCustomerID()));
        text_price.setText(String.valueOf(selectedLease.getPrice()));
        datePicker_borrowDate.setValue(selectedLease.getBorrowDate().toLocalDate());
        datePicker_returnDate.setValue(selectedLease.getReturnDate().toLocalDate());
        System.out.println(selectedLease.toString());
    }


    @FXML
    public void btn_save_onClick() throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Update info confirmation");
        a.setContentText(String.format("Are you sure you want to update information about this lease?"));
        Button yesButton = (Button)a.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");
        Optional<ButtonType> yesResponse = a.showAndWait();
        if(yesResponse.isPresent() && ButtonType.OK.equals(yesResponse.get())){
            if (
                    validateField(text_machineID) &&
                    validateField(text_customerID) &&
                    validateField(text_price) &&
                    datePicker_borrowDate.getValue() != null &&
                    datePicker_returnDate.getValue() != null)
            {
                System.out.println("Validation passed");
                conn = new DBConnect();
                try {
                    conn.upload(String.format("UPDATE leases SET leaseMachineID='%1$s',customerID='%2$s', borrowDate='%3$s', returnDate='%4$s', price='%5$s' WHERE id='%6$d'",
                            text_machineID.getText(),
                            text_customerID.getText(),
                            datePicker_borrowDate.getValue(),
                            datePicker_returnDate.getValue(),
                            text_price.getText(),
                            selectedLease.getId()));
                } catch (Exception ex) {
                    displayMessage(ERROR, ex.getMessage());
                } finally {
                    refreshTable();
                }
            }
        }
    }

    @FXML
    public void btn_delete_onClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an non-revertable action!");
        alert.setContentText(String.format("Are you sure you want to delete this lease from the list?"));
        Button deleteButton = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        Alert alertFinal = new Alert(Alert.AlertType.CONFIRMATION);
        alertFinal.setHeaderText("Are you sure?");
        alertFinal.setContentText("There is no way to take back this operation. Are you fully aware of that?");
        if(deleteResponse.isPresent()){
            if(ButtonType.OK.equals(deleteResponse.get())){
                Optional<ButtonType> deleteFinal = alertFinal.showAndWait();
                if(deleteFinal.isPresent()){
                    if(ButtonType.OK.equals(deleteFinal.get())){
                        conn = new DBConnect();
                        try {
                            conn.upload(String.format("DELETE FROM leases WHERE id='%1$d'", selectedLease.getId()));
                        } catch (Exception ex) {
                            displayMessage(ERROR, ex.getMessage());
                        } finally {
                            refreshTable();
                        }
                    }
                }
            }
        }
    }

    @FXML
    public void btn_endLease_onClick() {

    }

    private void refreshTable() {
        leases.removeAll();
        leases = null;
        leases = FXCollections.observableArrayList();
        tableView_leases.getColumns().removeAll(leaseMachineIDCOl, customerIDCol, borrowDateCol, borrowTimeCol, returnDateCol, returnTimeCol, priceCol, employeeIDCol);
        retrieveData();
        initTableColumns(leases);
    }

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(leases);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_leases.getColumns().removeAll(leaseMachineIDCOl, customerIDCol, borrowDateCol, borrowTimeCol, returnDateCol, returnTimeCol, priceCol, employeeIDCol);
        initTableColumns(searchResults);
    }

    private ObservableList<Lease> performSearch(String query) {
        ObservableList<Lease> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return leases;
        }
        for (Lease lease : leases) {
            ObservableList<String> searchCriteriaList;
            if(checkComboBox_search_criteria.getCheckModel().getCheckedItems().size() != 0){
                searchCriteriaList = checkComboBox_search_criteria.getCheckModel().getCheckedItems();
            } else {
                searchCriteriaList = FXCollections.observableArrayList(leasesCriteria);
            }
            for (String criteria : searchCriteriaList) {
                switch (criteria) {
                    case "Lease machine ID":
                        if (String.valueOf(lease.getLeaseMachineID()).contains(query)) {
                            results.add(lease);
                        }
                        break;
                    case "Customer ID":
                        if (String.valueOf(lease.getCustomerID()).contains(query)) {
                            results.add(lease);
                        }
                        break;
                    case "Price":
                        if (String.valueOf(lease.getPrice()).contains(query)) {
                            results.add(lease);
                        }
                        break;
                    case "Employee ID":
                        if (String.valueOf(lease.getEmployeeID()).contains(query)) {
                            results.add(lease);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }
}
