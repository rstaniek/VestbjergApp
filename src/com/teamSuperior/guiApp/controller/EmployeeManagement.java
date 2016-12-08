package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class EmployeeManagement implements Initializable {
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

    private ObservableList<Employee> employees;
    private static Employee loggedInUser;
    private Employee selectedEmployee;
    private DBConnect conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employees = FXCollections.observableArrayList();
        conn = new DBConnect();
        loggedInUser = LogInPopupController.getUser();
        ResultSet rs = conn.getFromDataBase("SELECT * FROM employees");
        try{
            while (rs.next()){
                if(rs.getInt("id") != 0 && rs.getString("name") != null
                        && rs.getString("surname") != null
                        && rs.getString("address") != null
                        && rs.getString("city") != null
                        && rs.getString("zip") != null
                        && rs.getString("email") != null
                        && rs.getString("phone") != null
                        && rs.getString("password") != null
                        && rs.getString("position") != null
                        && rs.getInt("accessLevel") >= 1
                        ){
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
        }
        catch (SQLException sqlException){
            AlertBox.display("SQL exception", sqlException.getMessage());
        }
        catch (Exception ex){
            AlertBox.display("Unexpected exception", ex.getMessage());
        }

        //fill the table with data
        initTableColumns(loggedInUser.getAccessLevel());
        selectedEmployee = (Employee) tableView_employees.getFocusModel().getFocusedItem();
    }

    private void initTableColumns(int accessLevel){
        if(accessLevel >= 2){
            TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setMinWidth(90);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Employee, String> surnameColumn = new TableColumn<>("Surname");
            surnameColumn.setMinWidth(90);
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setMinWidth(150);
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
            positionColumn.setMinWidth(90);
            positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

            TableColumn<Employee, String> numOfSalesColumn = new TableColumn<>("Number of sales");
            numOfSalesColumn.setMinWidth(80);
            numOfSalesColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("numberOfSales_str"));

            TableColumn<Employee, String> totalRevenueColumn = new TableColumn<>("Total revenue");
            totalRevenueColumn.setMinWidth(80);
            totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("totalRevenue_str"));

            TableColumn<Employee, String> accessLevelColumn = new TableColumn<>("Access level");
            accessLevelColumn.setMinWidth(80);
            accessLevelColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessLevel_str"));

            tableView_employees.setItems(employees);
            tableView_employees.getColumns().addAll(nameColumn, surnameColumn, emailColumn, positionColumn, numOfSalesColumn, totalRevenueColumn, accessLevelColumn);
        }if(accessLevel >= 3){
            //stuff
            TableColumn<Employee, String> addressColumn = new TableColumn<>("Address");
            addressColumn.setMinWidth(120);
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

            TableColumn<Employee, String> cityColumn = new TableColumn<>("City");
            cityColumn.setMinWidth(100);
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

            TableColumn<Employee, String> zipColumn = new TableColumn<>("ZIP code");
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

    private void saveChanges(Employee e){
        //TODO: implement saving changes
        Error.displayError(ErrorCode.NOT_IMPLEMENTED);
    }
}
