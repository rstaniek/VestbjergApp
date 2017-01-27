package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.ErrorCode;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.*;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by rajmu on 17.01.13.
 */
public class CustomersManageController implements Initializable {
    @FXML
    public Button btn_search_clear;
    @FXML
    public Button btn_saveQuit;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public TableView tableView_customers;
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_surname;
    @FXML
    public TextField text_email;
    @FXML
    public TextField text_address;
    @FXML
    public TextField text_city;
    @FXML
    public TextField text_zip;
    @FXML
    public Button btn_save;
    @FXML
    public Button btn_delete;
    @FXML
    public Button btn_add;
    @FXML
    public TextField text_phone;

    private ObservableList<Customer> customers;
    private ObservableList<Customer> searchResults;
    private Customer selectedCustomer;
    private DBConnect conn;

    private TableColumn<Customer, Integer> idColumn;
    private TableColumn<Customer, Integer> salesColumn;
    private TableColumn<Customer, Double> totalSpentColumn;
    private TableColumn<Customer, String> nameColumn;
    private TableColumn<Customer, String> surnameColumn;
    private TableColumn<Customer, String> addressColumn;
    private TableColumn<Customer, String> cityColumn;
    private TableColumn<Customer, String> zipColumn;
    private TableColumn<Customer, String> emailColumn;
    private TableColumn<Customer, String> phoneColumn;

    private static final String[] customersCriteria = new String[]{"ID", "Name", "Surname", "Address", "City", "ZIP"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customers = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll(customersCriteria);
        conn = new DBConnect();

        retrieveData();
        initTableColumns(customers);
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
    }

    private void retrieveData(){
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM customers");
            while (rs.next()){
                customers.add(new Customer(rs.getInt("id"),
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
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<Customer> source){
        tableView_customers.getColumns().removeAll(idColumn, nameColumn, surnameColumn, addressColumn, cityColumn, zipColumn, emailColumn, phoneColumn, salesColumn, totalSpentColumn);

        idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));

        cityColumn = new TableColumn<>("City");
        cityColumn.setMinWidth(60);
        cityColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));

        addressColumn = new TableColumn<>("Address");
        addressColumn.setMinWidth(60);
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));

        emailColumn = new TableColumn<>("email");
        emailColumn.setMinWidth(80);
        emailColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));

        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(50);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));

        phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setMinWidth(60);
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));

        salesColumn = new TableColumn<>("Sales");
        salesColumn.setMinWidth(30);
        salesColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("salesMade"));

        surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setMinWidth(50);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("surname"));

        totalSpentColumn = new TableColumn<>("Total spent");
        totalSpentColumn.setMinWidth(60);
        totalSpentColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("totalSpent"));

        zipColumn = new TableColumn<>("ZIP");
        zipColumn.setMinWidth(40);
        zipColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("zip"));

        tableView_customers.setItems(source);
        tableView_customers.getColumns().addAll(idColumn, nameColumn, surnameColumn, addressColumn, cityColumn, zipColumn, emailColumn, phoneColumn, salesColumn, totalSpentColumn);
    }

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(customers);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        initTableColumns(searchResults);
    }

    private ObservableList<Customer> performSearch(String query) {
        ObservableList<Customer> results = FXCollections.observableArrayList();
        if(query.isEmpty()){
            return customers;
        }
        for (Customer customer : customers){
            ObservableList<String> cList;
            if(checkComboBox_search_criteria.getCheckModel().getCheckedItems().size() != 0){
                cList = checkComboBox_search_criteria.getCheckModel().getCheckedItems();
            } else {
                cList = FXCollections.observableArrayList(customersCriteria);
            }
            for (String criteria : cList){
                switch (criteria){
                    case "ID":
                        if (String.valueOf(customer.getId()).contains(query)){
                            results.add(customer);
                        }
                        break;
                    case "name":
                        if(customer.getName().contains(query)){
                            results.add(customer);
                        }
                        break;
                    case "Surname":
                        if(customer.getSurname().contains(query)){
                            results.add(customer);
                        }
                        break;
                    case "Address":
                        if(customer.getAddress().contains(query)){
                            results.add(customer);
                        }
                        break;
                    case "City":
                        if(customer.getCity().contains(query)){
                            results.add(customer);
                        }
                        break;
                    case "ZIP":
                        if(customer.getZip().contains(query)){
                            results.add(customer);
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
    public void tableView_customers_onMouseClicked(MouseEvent mouseEvent) {
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
        text_address.setText(selectedCustomer.getAddress());
        text_city.setText(selectedCustomer.getCity());
        text_email.setText(selectedCustomer.getEmail());
        text_name.setText(selectedCustomer.getName());
        text_surname.setText(selectedCustomer.getSurname());
        text_zip.setText(selectedCustomer.getZip());
        text_phone.setText(selectedCustomer.getPhone());
    }

    @FXML
    public void btn_save_onClick(ActionEvent actionEvent) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Update info confirmation");
        a.setContentText(String.format("Are you sure you want to update information about the customer: %1$s %2$s?", selectedCustomer.getName(), selectedCustomer.getSurname()));
        Button yesButton = (Button)a.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");
        Optional<ButtonType> yesResponse = a.showAndWait();
        if(yesResponse.isPresent()){
            if(ButtonType.OK.equals(yesResponse.get())){
                if(validateField(text_address) &&
                        validateField(text_city) &&
                        validateField(text_email) &&
                        validateField(text_name) &&
                        validateField(text_phone) &&
                        validateField(text_surname) &&
                        validateField(text_zip)){
                    try{
                        conn = new DBConnect();
                        conn.upload(String.format("UPDATE customers SET name='%1$s',surname='%2$s',address='%3$s',city='%4$s',zip='%5$s',email='%6$s',phone='%7$s' WHERE id='%8$d'",
                                text_name.getText(),
                                text_surname.getText(),
                                text_address.getText(),
                                text_city.getText(),
                                text_zip.getText(),
                                text_email.getText(),
                                text_phone.getText(),
                                selectedCustomer.getId()));
                    } catch (Exception ex) {
                        displayMessage(ERROR, ex.getMessage());
                    } finally {
                        refreshTable();
                    }
                }
            }
        }
    }

    private void refreshTable(){
        customers.removeAll();
        customers = null;
        customers = FXCollections.observableArrayList();
        retrieveData();
        initTableColumns(customers);
    }
    private void saveCustomer(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an non-revertable action!");
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the customers list?", selectedCustomer.getName()));
        Button deleteButton = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        Alert alertFinal = new Alert(Alert.AlertType.CONFIRMATION);
        alertFinal.setHeaderText("Are you sure?");
        alertFinal.setContentText("There is no way to take back this operation. Are you fully aware of that?");
        if(deleteResponse.isPresent() && ButtonType.OK.equals(deleteResponse.get())){
            Optional<ButtonType> deleteFinal = alertFinal.showAndWait();
            if(deleteFinal.isPresent() && ButtonType.OK.equals(deleteFinal.get())){
                conn = new DBConnect();
                try {
                    conn.upload(String.format("DELETE FROM customers WHERE id='%1$d'", selectedCustomer.getId()));
                } catch (Exception ex) {
                    displayMessage(ERROR, ex.getMessage());
                } finally {
                    refreshTable();
                }
            }
        }
    }
    @FXML
    public void btn_delete_onClick(ActionEvent actionEvent) {
        saveCustomer();
    }

    @FXML
    public void btn_saveQuit_onClick(ActionEvent actionEvent) {
        saveCustomer();
        Stage stage = (Stage) btn_saveQuit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../layout/customerAdd.fxml"));
            Stage window = new Stage();
            window.setTitle("Add new customer");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.showAndWait();
            refreshTable();
        } catch (IOException ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
    }
}
