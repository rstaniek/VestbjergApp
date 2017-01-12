package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class EmployeeStatisticsController implements Initializable {

    @FXML
    public TableView tableView_employees;
    @FXML
    public BarChart chart_numberOfSales;
    @FXML
    public BarChart chart_revenue;
    @FXML
    public Label label_productivity;
    @FXML
    public PieChart chart_contribution;
    @FXML
    public Label label_efficiency;
    @FXML
    public TextField text_search_query;
    @FXML
    public Button btn_search_clear;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;

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

    private ObservableList<Employee> employees;
    private ObservableList<Employee> searchResults;
    private static Employee loggedInUser;
    private Employee selectedEmployee;
    private DBConnect conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employees = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("Name", "Surname", "Address", "City", "ZIP", "Phone", "Position");
        conn = new DBConnect();
        loggedInUser = UserController.getUser();
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
            Error.displayMessage(ERROR, "SQL connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }

        //fill the table with data
        initTableColumns(loggedInUser.getAccessLevel(), employees);
        selectedEmployee = (Employee) tableView_employees.getFocusModel().getFocusedItem();
        System.out.println(String.format("Currently selected Employee: Name %1$s, Surname %2$s, NOS %3$s, R %4$s", selectedEmployee.getName(), selectedEmployee.getSurname(), selectedEmployee.getNumberOfSales_str(), selectedEmployee.getTotalRevenue_str()));
        updateStatsView();
        updateLabels();
    }

    private void initTableColumns(int accessLevel, ObservableList<Employee> source) {
        if (accessLevel >= 1) {
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

            tableView_employees.setItems(source);
            tableView_employees.getColumns().addAll(nameColumn, surnameColumn, emailColumn, positionColumn);
        }
        if (accessLevel >= 2) {
            numOfSalesColumn = new TableColumn<>("Number of sales");
            numOfSalesColumn.setMinWidth(80);
            numOfSalesColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("numberOfSales_str"));

            totalRevenueColumn = new TableColumn<>("Total revenue");
            totalRevenueColumn.setMinWidth(80);
            totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("totalRevenue_str"));

            accessLevelColumn = new TableColumn<>("Access level");
            accessLevelColumn.setMinWidth(80);
            accessLevelColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("accessLevel_str"));

            tableView_employees.getColumns().addAll(numOfSalesColumn, totalRevenueColumn, accessLevelColumn);
        }
        if (accessLevel >= 3) {
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
        updateStatsView();
        updateLabels();
        System.out.println(String.format("Currently selected Employee: Name %1$s, Surname %2$s, NOS %3$s, R %4$s", selectedEmployee.getName(), selectedEmployee.getSurname(), selectedEmployee.getNumberOfSales_str(), selectedEmployee.getTotalRevenue_str()));
    }

    private void updateStatsView() {
        //TODO: add colors to the bars depending on their value (from empStats.css) for some reason it doesn't work
        chart_numberOfSales.getData().clear();
        chart_revenue.getData().clear();
        chart_contribution.getData().clear();
        XYChart.Series sales = new XYChart.Series<>();
        XYChart.Series revenue = new XYChart.Series<>();
        ObservableList<PieChart.Data> contributionData = FXCollections.observableArrayList();
        XYChart.Data numOfSalesBar;
        XYChart.Data revenueBar;
        if (loggedInUser.getAccessLevel() == 1) {
            numOfSalesBar = new XYChart.Data<>("You", loggedInUser.getNumberOfSales());
            revenueBar = new XYChart.Data<>("You", loggedInUser.getTotalRevenue());
            contributionData.addAll(new PieChart.Data("You", loggedInUser.getTotalRevenue()), new PieChart.Data("Total company revenue", calculateAvgRevenue() * employees.size()));
            /*if (loggedInUser.getNumberOfSales() < calculateAvgSales()) {
                numOfSalesBar.getNode().getStyleClass().add("less-than-avg");
            } else {
                numOfSalesBar.getNode().getStyleClass().add("greater-than-avg");
            }
            if (loggedInUser.getTotalRevenue() < calculateAvgRevenue()) {
                revenueBar.getNode().getStyleClass().add("less-than-avg");
            } else {
                revenueBar.getNode().getStyleClass().add("greater-than-avg");
            }*/
        } else {
            numOfSalesBar = new XYChart.Data<>(selectedEmployee.getName(), selectedEmployee.getNumberOfSales());
            revenueBar = new XYChart.Data<>(selectedEmployee.getName(), selectedEmployee.getTotalRevenue());
            contributionData.addAll(new PieChart.Data(selectedEmployee.getName(), selectedEmployee.getTotalRevenue()), new PieChart.Data("Total company revenue", calculateAvgRevenue() * employees.size()));
            /*if (selectedEmployee.getNumberOfSales() < calculateAvgSales()) {
                numOfSalesBar.getNode().getStyleClass().add("less-than-avg");
            } else {
                numOfSalesBar.getNode().getStyleClass().add("greater-than-avg");
            }
            if (selectedEmployee.getTotalRevenue() < calculateAvgRevenue()) {
                revenueBar.getNode().getStyleClass().add("less-than-avg");
            } else {
                revenueBar.getNode().getStyleClass().add("greater-than-avg");
            }*/
        }

        sales.getData().addAll(numOfSalesBar, new XYChart.Data<>("Average", calculateAvgSales()));
        revenue.getData().addAll(revenueBar, new XYChart.Data<>("Average", calculateAvgRevenue()));
        chart_numberOfSales.getData().addAll(sales);
        chart_revenue.getData().addAll(revenue);
        chart_contribution.getData().addAll(contributionData);
    }

    private void updateLabels() {
        String productivity, efficiency;
        if (loggedInUser.getAccessLevel() == 1) {
            if (loggedInUser.getNumberOfSales() >= calculateAvgSales()) {
                productivity = String.format("The productivity of %1$s is %2$.1f%3$s better than the average.", loggedInUser.getName(), getProductivityPercentage(loggedInUser, calculateAvgSales()), "%");
            } else {
                productivity = String.format("The productivity of %1$s is %2$.1f%3$s worse than the average.", loggedInUser.getName(), getProductivityPercentage(loggedInUser, calculateAvgSales()), "%");
            }
            efficiency = String.format("Efficiency of %1$s is %2$.2f DKK/sale", loggedInUser.getName(), getEfficiency(loggedInUser));
        } else {
            if (selectedEmployee.getNumberOfSales() >= calculateAvgSales()) {
                productivity = String.format("The productivity of %1$s is %2$.1f%3$s better than the average.", selectedEmployee.getName(), getProductivityPercentage(selectedEmployee, calculateAvgSales()), "%");
            } else {
                productivity = String.format("The productivity of %1$s is %2$.1f%3$s worse than the average.", selectedEmployee.getName(), getProductivityPercentage(selectedEmployee, calculateAvgSales()), "%");
            }
            efficiency = String.format("Efficiency of %1$s is %2$.2f DKK/sale", selectedEmployee.getName(), getEfficiency(selectedEmployee));
        }
        label_productivity.setText(productivity);
        label_efficiency.setText(efficiency);
    }

    private float getProductivityPercentage(Employee e, float avg) {
        return (Math.abs(e.getNumberOfSales() - avg) / avg) * 100;
    }

    private double getEfficiency(Employee e) {
        return e.getTotalRevenue() / e.getNumberOfSales();
    }

    private float calculateAvgSales() {
        float avg = 0;
        for (Employee e : employees) {
            avg += e.getNumberOfSales();
        }
        avg /= employees.size();
        return avg;
    }

    private float calculateAvgRevenue() {
        float avg = 0;
        for (Employee e : employees) {
            avg += e.getTotalRevenue();
        }
        avg /= employees.size();
        return avg;
    }

    //searching component
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
}
