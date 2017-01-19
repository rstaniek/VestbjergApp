package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Contractor;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.CheckComboBox;

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
public class ContractorsManageController implements Initializable {
    @FXML
    public TableView tableView_contractors;
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
    public Button btn_save;
    @FXML
    public Button btn_delete;
    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public TextField text_email;

    private ObservableList<Contractor> contractors;
    private ObservableList<Contractor> searchResults;
    private static Employee loggedInUser;
    private Contractor selectedContractor;
    private DBConnect conn;

    //columns
    private TableColumn<Contractor, String> nameCol;
    private TableColumn<Contractor, String> addressCol;
    private TableColumn<Contractor, String> cityCol;
    private TableColumn<Contractor, String> zipCol;
    private TableColumn<Contractor, String> phoneCol;
    private TableColumn<Contractor, String> emailCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchResults = FXCollections.observableArrayList();
        contractors = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("Name", "Address", "City", "ZIP", "Phone", "Email");
        conn = new DBConnect();
        loggedInUser = UserController.getUser();

        retrieveData();
        //init table
        initTableColumns(contractors);
        selectedContractor = (Contractor) tableView_contractors.getFocusModel().getFocusedItem();
    }

    private void retrieveData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM contractors");
            while (rs.next()) {
                if (rs.getString("name") != null &&
                        rs.getString("address") != null &&
                        rs.getString("city") != null &&
                        rs.getString("zip") != null &&
                        rs.getString("phone") != null &&
                        rs.getString("email") != null) {
                    Contractor tmp = new Contractor(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("zip"),
                            rs.getString("phone"),
                            rs.getString("email"));
                    //System.out.print(tmp.toString());
                    contractors.add(tmp);
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<Contractor> source) {
        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("name"));

        addressCol = new TableColumn<>("Address");
        addressCol.setMinWidth(150);
        addressCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("address"));

        cityCol = new TableColumn<>("City");
        cityCol.setMinWidth(80);
        cityCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("city"));

        zipCol = new TableColumn<>("ZIP");
        zipCol.setMinWidth(50);
        zipCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("zip"));

        phoneCol = new TableColumn<>("Phone number");
        phoneCol.setMinWidth(150);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("phone"));

        emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(150);
        emailCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("email"));

        tableView_contractors.setItems(source);
        tableView_contractors.getColumns().addAll(nameCol, addressCol, cityCol, zipCol, phoneCol, emailCol);
    }

    @FXML
    public void tableView_contractors_onMouseClicked() {
        selectedContractor = (Contractor) tableView_contractors.getFocusModel().getFocusedItem();
        text_name.setText(selectedContractor.getName());
        text_address.setText(selectedContractor.getAddress());
        text_city.setText(selectedContractor.getCity());
        text_zip.setText(selectedContractor.getZip());
        text_phone.setText(selectedContractor.getPhone());
        text_email.setText(selectedContractor.getEmail());
        System.out.println(selectedContractor.toString());
    }

    @FXML
    public void btn_save_onClick() throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Update info confirmation");
        a.setContentText(String.format("Are you sure you want to update information about %1$s contractor?", selectedContractor.getName()));
        Button yesButton = (Button)a.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");
        Optional<ButtonType> yesResponse = a.showAndWait();
        if(yesResponse.isPresent() && ButtonType.OK.equals(yesResponse.get())){
            if (validateField(text_name) &&
                    validateField(text_address) &&
                    validateField(text_city) &&
                    validateField(text_zip) &&
                    validateField(text_phone)) {
                System.out.println("Validation passed");
                conn = new DBConnect();
                try {
                    System.out.println(String.format("UPDATE contractors SET name='%2$s',address='%3$s',city='%4$s',zip='%5$s',phone='%6$s', email='%1$s' WHERE id='%7$d'",
                            text_email.getText(),
                            text_name.getText(),
                            text_address.getText(),
                            text_city.getText(),
                            text_zip.getText(),
                            text_phone.getText(),
                            selectedContractor.getId()));
                    conn.upload(String.format("UPDATE contractors SET name='%2$s',address='%3$s',city='%4$s',zip='%5$s',phone='%6$s', email='%1$s' WHERE id='%7$d'",
                            text_email.getText(),
                            text_name.getText(),
                            text_address.getText(),
                            text_city.getText(),
                            text_zip.getText(),
                            text_phone.getText(),
                            selectedContractor.getId()));
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
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the contractors list?", selectedContractor.getName()));
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
                            conn.upload(String.format("DELETE FROM contractors WHERE id='%1$d'", selectedContractor.getId()));
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
        contractors.removeAll();
        contractors = null;
        contractors = FXCollections.observableArrayList();
        tableView_contractors.getColumns().removeAll(nameCol,
                addressCol,
                cityCol,
                zipCol,
                phoneCol,
                emailCol);
        retrieveData();
        initTableColumns(contractors);
    }

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(contractors);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_contractors.getColumns().removeAll(nameCol,
                addressCol,
                cityCol,
                zipCol,
                phoneCol,
                emailCol);
        initTableColumns(searchResults);
    }

    private ObservableList<Contractor> performSearch(String query) {
        ObservableList<Contractor> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return contractors;
        }
        for (Contractor contractor : contractors) {
            for (String criteria : checkComboBox_search_criteria.getCheckModel().getCheckedItems()) {
                switch (criteria) {
                    case "Name":
                        if (contractor.getName().contains(query)) {
                            results.add(contractor);
                        }
                        break;
                    case "Address":
                        if (contractor.getAddress().contains(query)) {
                            results.add(contractor);
                        }
                        break;
                    case "City":
                        if (contractor.getCity().contains(query)) {
                            results.add(contractor);
                        }
                        break;
                    case "ZIP":
                        if (contractor.getZip().contains(query)) {
                            results.add(contractor);
                        }
                        break;
                    case "Phone":
                        if (contractor.getPhone().contains(query)) {
                            results.add(contractor);
                        }
                        break;
                    case "Email":
                        if (contractor.getEmail().contains(query)) {
                            results.add(contractor);
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
