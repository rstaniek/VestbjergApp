package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class EmployeeManagementController implements Initializable {
    @FXML
    public TableView tableView_employees;
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_surname;
    @FXML
    public TextField text_email;
    @FXML
    public TextField text_position;
    @FXML
    public TextField text_address;
    @FXML
    public TextField text_city;
    @FXML
    public TextField text_zip;
    @FXML
    public Button btn_save;
    @FXML
    public Button btn_saveQuit;
    @FXML
    public Button btn_quit;
    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public Button btn_delete;

    private ObservableList<Employee> employees;
    private ObservableList<Employee> searchResults;
    private static Employee loggedInUser;
    private Employee selectedEmployee;
    private DBConnect conn;

    //Columns
    private TableColumn<Employee, String> nameColumn;
    private TableColumn<Employee, String> surnameColumn;
    private TableColumn<Employee, String> emailColumn;
    private TableColumn<Employee, String> positionColumn;
    private TableColumn<Employee, String> numOfSalesColumn;
    private TableColumn<Employee, String> totalRevenueColumn;
    private TableColumn<Employee, String> accessLevelColumn;
    private TableColumn<Employee, String> addressColumn;
    private TableColumn<Employee, String> cityColumn;
    private TableColumn<Employee, String> zipColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employees = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("Name", "Surname", "Address", "City", "ZIP", "Phone", "Position");
        conn = new DBConnect();
        loggedInUser = UserController.getUser();

        retrieveData();
        //fill the table with data
        initTableColumns(loggedInUser.getAccessLevel(), employees);
        selectedEmployee = (Employee) tableView_employees.getFocusModel().getFocusedItem();
    }

    private void retrieveData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM employees");
            while (rs.next()) {
                if (rs.getInt("id") != -1 && rs.getString("name") != null
                        && rs.getString("surname") != null
                        && rs.getString("address") != null
                        && rs.getString("city") != null
                        && rs.getString("zip") != null
                        && rs.getString("email") != null
                        && rs.getString("phone") != null
                        && rs.getString("password") != null
                        && rs.getString("position") != null
                        && rs.getInt("accessLevel") >= 1
                        ) {
                    employees.add(new Employee(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("zip"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getString("position"),
                            rs.getInt("numberOfSales"),
                            rs.getDouble("totalRevenue"),
                            rs.getInt("accessLevel")));
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "Server connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(int accessLevel, ObservableList<Employee> source) {
        if (accessLevel >= 2) {
            nameColumn = new TableColumn<>("Name");
            nameColumn.setMinWidth(90);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            surnameColumn = new TableColumn<>("Surname");
            surnameColumn.setMinWidth(90);
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            emailColumn = new TableColumn<>("Email");
            emailColumn.setMinWidth(150);
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            positionColumn = new TableColumn<>("Position");
            positionColumn.setMinWidth(90);
            positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

            numOfSalesColumn = new TableColumn<>("Number of sales");
            numOfSalesColumn.setMinWidth(80);
            numOfSalesColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("numberOfSales_str"));

            totalRevenueColumn = new TableColumn<>("Total revenue");
            totalRevenueColumn.setMinWidth(80);
            totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("totalRevenue_str"));

            accessLevelColumn = new TableColumn<>("Access level");
            accessLevelColumn.setMinWidth(80);
            accessLevelColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessLevel_str"));

            tableView_employees.setItems(source);
            tableView_employees.getColumns().addAll(nameColumn, surnameColumn, emailColumn, positionColumn, numOfSalesColumn, totalRevenueColumn, accessLevelColumn);
        }
        if (accessLevel >= 3) {
            //stuff
            addressColumn = new TableColumn<>("Address");
            addressColumn.setMinWidth(120);
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

            cityColumn = new TableColumn<>("City");
            cityColumn.setMinWidth(100);
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

            zipColumn = new TableColumn<>("ZIP code");
            zipColumn.setMinWidth(80);
            zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));

            tableView_employees.getColumns().addAll(addressColumn, cityColumn, zipColumn);
        }
    }

    @FXML
    public void tableView_employees_onMouseClicked(MouseEvent mouseEvent) {
        selectedEmployee = (Employee) tableView_employees.getFocusModel().getFocusedItem();
        text_name.setText(selectedEmployee.getName());
        text_surname.setText(selectedEmployee.getSurname());
        text_email.setText(selectedEmployee.getEmail());
        text_position.setText(selectedEmployee.getPosition());
        text_address.setText(selectedEmployee.getAddress());
        text_city.setText(selectedEmployee.getCity());
        text_zip.setText(selectedEmployee.getZip());
    }

    @FXML
    public void btn_save_onClick(ActionEvent actionEvent) {
        saveChanges(selectedEmployee);
    }

    @FXML
    public void btn_saveQuit_onClick(ActionEvent actionEvent) {
        saveChanges(selectedEmployee);
        Stage window = (Stage) btn_saveQuit.getScene().getWindow();
        window.close();
    }

    @FXML
    public void btn_quit_onClick(ActionEvent actionEvent) {
        Stage window = (Stage) btn_quit.getScene().getWindow();
        window.close();
    }

    private void saveChanges(Employee e) {
        boolean result = ConfirmBox.display("Saving changes", "Are you sure you want to update information about" + selectedEmployee.getName() + "?");
        if (result &&
                validateField(text_name) &&
                validateField(text_surname) &&
                validateField(text_email) &&
                validateField(text_position) &&
                validateField(text_address) &&
                validateField(text_city) &&
                validateField(text_zip)) {
            conn = new DBConnect();
            try {
                conn.upload(String.format("UPDATE employees SET name='%1$s',surname='%2$s',address='%3$s',city='%4$s',zip='%5$s',position='%6$s',email='%7$s' WHERE id='%8$d'",
                        text_name.getText(),
                        text_surname.getText(),
                        text_address.getText(),
                        text_city.getText(),
                        text_zip.getText(),
                        text_position.getText(),
                        text_email.getText(),
                        e.getId()));
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        } else if (result) displayError(ErrorCode.VALIDATION_ILLEGAL_CHARS);
        refreshTable();
    }

    private void refreshTable() {
        employees.removeAll();
        employees = null;
        employees = FXCollections.observableArrayList();
        if (loggedInUser.getAccessLevel() < 3) {
            tableView_employees.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn);
        } else {
            tableView_employees.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn,
                    addressColumn,
                    cityColumn,
                    zipColumn);
        }
        retrieveData();
        initTableColumns(loggedInUser.getAccessLevel(), employees);
    }

    //search bar
    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        printQueryLog("clear_onClick");
        initTableColumns(loggedInUser.getAccessLevel(), employees);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        printQueryLog("onKeyReleased");
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        if (loggedInUser.getAccessLevel() < 3) {
            tableView_employees.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn);
        } else {
            tableView_employees.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn,
                    addressColumn,
                    cityColumn,
                    zipColumn);
        }
        initTableColumns(loggedInUser.getAccessLevel(), searchResults);
    }

    private void printQueryLog(String sender) {
        String c = "";
        for (String s : checkComboBox_search_criteria.getCheckModel().getCheckedItems()) {
            c += s + ", ";
        }
        System.out.printf("%s@[%s]: %s%n", sender, c, text_search_query.getText());
    }

    private ObservableList<Employee> performSearch(String query) {
        ObservableList<Employee> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return employees;
        }
        for (Employee employee : employees) {
            for (String criteria : checkComboBox_search_criteria.getCheckModel().getCheckedItems()) {
                switch (criteria) {
                    case "Name":
                        if (employee.getName().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "Surname":
                        if (employee.getSurname().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "Address":
                        if (employee.getAddress().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "City":
                        if (employee.getCity().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "ZIP":
                        if (employee.getZip().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "Phone":
                        if (employee.getPhone().contains(query)) {
                            results.add(employee);
                        }
                        break;
                    case "Position":
                        if (employee.getPosition().contains(query)) {
                            results.add(employee);
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
    public void btn_delete_onClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an non-revertable action!");
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the employees list?", selectedEmployee.getName()));
        Button deleteButton = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        if(deleteResponse.isPresent()){
            if(ButtonType.OK.equals(deleteResponse.get())){
                conn = new DBConnect();
                try{
                    conn.upload(String.format("DELETE FROM employees WHERE id='%1$s'", selectedEmployee.getId()));
                } catch (SQLException sqlEx){
                    displayMessage(ERROR, "SQL connection error", sqlEx.getMessage());
                } finally {
                    refreshTable();
                }
            }
        }
    }
}
