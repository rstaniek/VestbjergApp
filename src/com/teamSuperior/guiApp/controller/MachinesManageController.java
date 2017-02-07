package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Machine;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class MachinesManageController implements Initializable {
    @FXML
    public TableView tableView_machines;
    @FXML
    public JFXTextField text_name;
    @FXML
    public JFXTextField text_pricePerDay;
    @FXML
    public JFXCheckBox checkBox_leased;
    @FXML
    public JFXButton btn_save;
    @FXML
    public JFXButton btn_delete;
    @FXML
    public JFXButton btn_search_clear;
    @FXML
    public JFXTextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public JFXButton btn_show;

    private ObservableList<Machine> machines;
    private ObservableList<Machine> searchResults;
    private static Employee loggedInUser;
    private Machine selectedMachine;
    private DBConnect conn;

    //columns
    private TableColumn<Machine, String> nameCol;
    private TableColumn<Machine, String> pricePerDayCol;
    private TableColumn<Machine, String> leasedCol;


    private static final String[] machinesCriteria = new String[]{"Name", "Price per day", "Leased"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchResults = FXCollections.observableArrayList();
        machines = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll(machinesCriteria);
        conn = new DBConnect();
        loggedInUser = UserController.getUser();
        retrieveData();
        //init table
        initTableColumns(machines);
        selectedMachine = (Machine) tableView_machines.getFocusModel().getFocusedItem();
    }

    private void retrieveData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leaseMachines");
            while (rs.next()) {
                if (rs.getString("name") != null &&
                        rs.getString("pricePerDay") != null &&
                        rs.getString("leased") != null) {
                    Machine tmp = new Machine(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getFloat("pricePerDay"),
                            rs.getBoolean("leased"));
                    //System.out.print(tmp.toString());
                    machines.add(tmp);
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void retrieveNotLeasedData(){
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leaseMachines");
            while (rs.next()) {
                if (rs.getString("name") != null &&
                        rs.getString("pricePerDay") != null &&
                        rs.getBoolean("leased") == false) {
                    Machine tmp = new Machine(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getFloat("pricePerDay"),
                            rs.getBoolean("leased"));
                    machines.add(tmp);
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<Machine> source) {
        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Machine, String>("name"));

        pricePerDayCol = new TableColumn<>("Price per day");
        pricePerDayCol.setMinWidth(150);
        pricePerDayCol.setCellValueFactory(new PropertyValueFactory<Machine, String>("pricePerDay"));

        leasedCol = new TableColumn<>("Leased");
        leasedCol.setMinWidth(80);
        leasedCol.setCellValueFactory(new PropertyValueFactory<Machine, String>("leased"));

        tableView_machines.setItems(source);
        tableView_machines.getColumns().addAll(nameCol, pricePerDayCol, leasedCol);
    }

    @FXML
    public void tableView_machines_onMouseClicked() {
        selectedMachine = (Machine) tableView_machines.getFocusModel().getFocusedItem();
        text_name.setText(selectedMachine.getName());
        text_pricePerDay.setText(Float.toString(selectedMachine.getPricePerDay()));
        checkBox_leased.setText(selectedMachine.isLeased() ? "Leased" : "Available");
        checkBox_leased.setSelected(selectedMachine.isLeased());
        System.out.println(selectedMachine.toString());
    }

    @FXML
    public void checkBox_leased_onClick(ActionEvent actionEvent) {
        if (checkBox_leased.isSelected()) {
            //TODO: Would be nice if it passed the machine's ID right to do field in addLease from here
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/leaseAdd.fxml"));
                Stage window = new Stage();
                window.setTitle("Add new lease");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void btn_save_onClick() throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Update info confirmation");
        a.setContentText(String.format("Are you sure you want to update information about %1$s machine?", selectedMachine.getName()));
        Button yesButton = (Button)a.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");
        Optional<ButtonType> yesResponse = a.showAndWait();
        if(yesResponse.isPresent() && ButtonType.OK.equals(yesResponse.get())){
            if (validateField(text_name) && validateField(text_pricePerDay)) {
                System.out.println("Validation passed");
                conn = new DBConnect();
                try {
                    conn.upload(String.format("UPDATE leaseMachines SET name='%1$s',pricePerDay='%2$s', leased='%3$d' WHERE id='%4$d'",
                            text_name.getText(),
                            text_pricePerDay.getText(),
                            checkBox_leased.isSelected() ? 1 : 0,
                            selectedMachine.getId()));
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
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the machines list?", selectedMachine.getName()));
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
                            conn.upload(String.format("DELETE FROM leaseMachines WHERE id='%1$d'", selectedMachine.getId()));
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

    private void refreshTable() {
        machines.removeAll();
        machines = null;
        machines = FXCollections.observableArrayList();
        tableView_machines.getColumns().removeAll(nameCol,
                pricePerDayCol,
                leasedCol);
        retrieveData();
        initTableColumns(machines);
    }

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(machines);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_machines.getColumns().removeAll(nameCol,
                pricePerDayCol,
                leasedCol);
        initTableColumns(searchResults);
    }

    private ObservableList<Machine> performSearch(String query) {
        ObservableList<Machine> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return machines;
        }
        for (Machine machine : machines) {
            ObservableList<String> searchCriteriaList;
            if(checkComboBox_search_criteria.getCheckModel().getCheckedItems().size() != 0){
                searchCriteriaList = checkComboBox_search_criteria.getCheckModel().getCheckedItems();
            } else {
                searchCriteriaList = FXCollections.observableArrayList(machinesCriteria);
            }
            for (String criteria : searchCriteriaList) {
                switch (criteria) {
                    case "Name":
                        if (machine.getName().contains(query)) {
                            results.add(machine);
                        }
                        break;
                    case "Price per day":
                        if (String.valueOf(machine.getPricePerDay()).contains(query)) {
                            results.add(machine);
                        }
                        break;
                    case "Leased":
                        if (String.valueOf(machine.isLeased()).contains(query)) {
                            results.add(machine);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }

    @FXML
    public void btn_show_onClick(ActionEvent event){
        switch(btn_show.getText()){
            case "Show available machines":
                machines.clear();
                retrieveNotLeasedData();
                initTableColumns(machines);
                btn_show.setText("Show all machines");
                break;
            case "Show all machines":
                machines.clear();
                retrieveData();
                initTableColumns(machines);
                btn_show.setText("Show available machines");
                break;
        }
    }
}
