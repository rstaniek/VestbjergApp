package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.connection.IDataAccessObject;
import com.teamSuperior.core.model.Position;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Employee management controller
 */
public class EmployeeManagementController implements IDataAccessObject<Employee, Integer>, Initializable {
    private static final String[] EMPLOYEE_CRITERIA = new String[]{"Name", "Surname", "Address", "City", "ZIP", "Phone", "Position"};
    private static Employee loggedInUser;

    private static ConnectionController<Employee, Integer> controller = new ConnectionController<>(Employee.class);
    private static ConnectionController<Position, Integer> positionController = new ConnectionController<>(Position.class);

    @FXML
    public TableView<Employee> employeesTableView;
    @FXML
    public JFXTextField nameField;
    @FXML
    public JFXTextField surnameField;
    @FXML
    public JFXTextField emailField;
    @FXML
    public JFXTextField addressField;
    @FXML
    public JFXTextField cityField;
    @FXML
    public JFXTextField zipField;
    @FXML
    public JFXButton saveButton;
    @FXML
    public JFXButton saveQuitButton;
    @FXML
    public JFXButton quitButton;
    @FXML
    public JFXButton clearSearchButton;
    @FXML
    public JFXTextField searchQueryField;
    @FXML
    public CheckComboBox<String> searchCriteriaComboBox;
    @FXML
    public JFXButton deleteButton;
    @FXML
    public JFXComboBox<String> positionComboBox;

    private ObservableList<Employee> employees;
    private ObservableList<Employee> searchResults;
    private ObservableList<Position> positions;
    private Employee selectedEmployee;

    //Columns
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> surnameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> numOfSalesColumn;
    @FXML
    private TableColumn<Employee, String> totalRevenueColumn;
    @FXML
    private TableColumn<Employee, String> accessLevelColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> cityColumn;
    @FXML
    private TableColumn<Employee, String> zipColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employees = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        searchCriteriaComboBox.getItems().addAll(EMPLOYEE_CRITERIA);
        loggedInUser = UserController.getUser();

        retrieveData();
        //fill the table with data
        initTableColumns(loggedInUser.getAccessLevel(), employees);
        selectedEmployee = employeesTableView.getFocusModel().getFocusedItem();

        positions = retrievePositionsData();
        positionComboBox.setItems(getPositionNames());
        positionComboBox.getSelectionModel().selectFirst();
    }

    private ObservableList<Position> retrievePositionsData() {
        return FXCollections.observableArrayList(positionController.getAll());
    }

    private ObservableList<String> getPositionNames() {
        ObservableList<String> results = FXCollections.observableArrayList();
        for (Position p : positions) {
            results.add(p.getName());
        }
        return results;
    }

    private void retrieveData() {
        employees = FXCollections.observableArrayList(getAll());
    }

    private void initTableColumns(int accessLevel, ObservableList<Employee> source) {
        if (accessLevel >= 2) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
            numOfSalesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSales_str"));
            totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue_str"));
            accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel_str"));

            employeesTableView.setItems(source);
        }
        if (accessLevel >= 10) {
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        } else {
            employeesTableView.getColumns().remove(addressColumn);
            employeesTableView.getColumns().remove(cityColumn);
            employeesTableView.getColumns().remove(zipColumn);
        }
    }

    @FXML
    public void clickEmployeesTableView() {
        selectedEmployee = employeesTableView.getFocusModel().getFocusedItem();
        nameField.setText(selectedEmployee.getName());
        surnameField.setText(selectedEmployee.getSurname());
        emailField.setText(selectedEmployee.getEmail());
        addressField.setText(selectedEmployee.getAddress());
        cityField.setText(selectedEmployee.getCity());
        zipField.setText(selectedEmployee.getZip());
        positionComboBox.getSelectionModel().select(selectedEmployee.getPosition());
    }

    @FXML
    public void clickSave() {
        saveChanges(selectedEmployee);
    }

    @FXML
    public void clickSaveQuit() {
        saveChanges(selectedEmployee);
        Stage window = (Stage) saveQuitButton.getScene().getWindow();
        window.close();
    }

    @FXML
    public void clickQuit() {
        Stage window = (Stage) quitButton.getScene().getWindow();
        window.close();
    }

    private void saveChanges(Employee e) {
        boolean result = ConfirmBox.display("Saving changes", "Are you sure you want to update information about " + selectedEmployee.getName() + "?");
        if (result) {
            e.setName(nameField.getText());
            e.setSurname(surnameField.getText());
            e.setAddress(addressField.getText());
            e.setCity(cityField.getText());
            e.setZip(zipField.getText());
            e.setPosition(positionComboBox.getSelectionModel().getSelectedItem());
            e.setEmail(emailField.getText());
            e.setAccessLevel(getAccessLevelBySelectedPosition());
            update(e);
        }
        refreshTable();
    }

    private int getAccessLevelBySelectedPosition() {
        int tmp = 0;
        for (Position p : positions) {
            if (p.getName().equals(positionComboBox.getSelectionModel().getSelectedItem())) {
                tmp = p.getAccessLevel();
            }
        }
        return tmp;
    }

    private void refreshTable() {
        employees.removeAll();
        employees = null;
        employees = FXCollections.observableArrayList();
        if (loggedInUser.getAccessLevel() < 3) {
            employeesTableView.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn);
        } else {
            employeesTableView.getColumns().removeAll(nameColumn,
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
    public void clickClearSearch() {
        searchQueryField.clear();
        printQueryLog("clear_onClick");
        initTableColumns(loggedInUser.getAccessLevel(), employees);
    }

    @FXML
    public void handleSearchQuery() {
        printQueryLog("onKeyReleased");
        searchResults = null;
        searchResults = performSearch(searchQueryField.getText());
        if (loggedInUser.getAccessLevel() < 3) {
            employeesTableView.getColumns().removeAll(nameColumn,
                    surnameColumn,
                    emailColumn,
                    positionColumn,
                    numOfSalesColumn,
                    totalRevenueColumn,
                    accessLevelColumn);
        } else {
            employeesTableView.getColumns().removeAll(nameColumn,
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
        for (String s : searchCriteriaComboBox.getCheckModel().getCheckedItems()) {
            c += s + ", ";
        }
        System.out.printf("%s@[%s]: %s%n", sender, c, searchQueryField.getText());
    }

    private ObservableList<Employee> performSearch(String query) {
        ObservableList<Employee> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return employees;
        }
        for (Employee employee : employees) {
            ObservableList<String> clist;
            if (searchCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
                clist = searchCriteriaComboBox.getCheckModel().getCheckedItems();
            } else {
                clist = FXCollections.observableArrayList(EMPLOYEE_CRITERIA);
            }
            for (String criteria : clist) {
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
    public void clickDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an non-revertible action!");
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the employees list?", selectedEmployee.getName()));
        Button deleteButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        if (deleteResponse.isPresent()) {
            if (ButtonType.OK.equals(deleteResponse.get())) {
                try {
                    controller.delete(selectedEmployee);
                } catch (Error error) {
                    displayMessage(ERROR, "Database error", error.getMessage());
                } finally {
                    refreshTable();
                }
            }
        }
    }

    @Override
    public void persist(Employee employee) {
        controller.persist(employee);
    }

    @Override
    public Employee getById(Integer integer) {
        return controller.getById(integer);
    }

    @Override
    public List<Employee> getAll() {
        return controller.getAll();
    }

    @Override
    public void update(Employee employee) {
        controller.update(employee);
    }

    @Override
    public void delete(Employee employee) {
        controller.delete(employee);
    }

    @Override
    public void deleteAll() {
        controller.deleteAll();
    }
}
