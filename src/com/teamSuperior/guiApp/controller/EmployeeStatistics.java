package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class EmployeeStatistics implements Initializable {

    @FXML
    public TableView tableView_employees;

    private ObservableList<Employee> employees;
    private static Employee loggedInUser;
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
    }

    private void initTableColumns(int accessLevel){
        if(accessLevel >= 1){
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

            tableView_employees.setItems(employees);
            tableView_employees.getColumns().addAll(nameColumn, surnameColumn, emailColumn, positionColumn);
        }if(accessLevel >= 2){
            //TODO: all this commented shit apparently doesn't work even tho it should
            /*TableColumn<Employee, String> numOfSalesColumn = new TableColumn<>("Number of sales");
            numOfSalesColumn.setMinWidth(75);
            numOfSalesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSales_str"));

            TableColumn<Employee, String> totalRevenueColumn = new TableColumn<>("Total Revenue");
            totalRevenueColumn.setMinWidth(75);
            totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue_str"));

            TableColumn<Employee, String> accessLevelColumn = new TableColumn<>("Access Level");
            accessLevelColumn.setMinWidth(75);
            accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel_str"));

            tableView_employees.getColumns().addAll(numOfSalesColumn, totalRevenueColumn, accessLevel);*/
        }if(accessLevel == 3){
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
}
