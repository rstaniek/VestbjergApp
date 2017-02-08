package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Lease;
import com.teamSuperior.core.model.service.Machine;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

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
    public JFXComboBox list_machines;
    @FXML
    public JFXComboBox list_customers;
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
    @FXML
    public JFXButton btn_show;


    private ObservableList<Lease> searchResults;
    private ObservableList<Lease> leases;
    private ObservableList<Machine> machines;
    private ObservableList<Customer> customers;
    private Lease selectedLease;
    private DBConnect conn;
    private Employee loggedUser;

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
        loggedUser = UserController.getUser();
        searchResults = FXCollections.observableArrayList();
        leases = FXCollections.observableArrayList();
        machines = FXCollections.observableArrayList();
        customers = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll(leasesCriteria);
        text_price.setDisable(true);
        conn = new DBConnect();
        retrieveLeaseData();
        retrieveMachineData();
        retrieveCustomers();
        initTableColumns(leases);
        selectedLease = (Lease) tableView_leases.getFocusModel().getFocusedItem();
        runLeaseExpirationCheck();
    }


    private void retrieveLeaseData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leases");
            while (rs.next()) {
                if (
                        rs.getString("id") != null &&
                                rs.getString("leaseMachineID") != null &&
                                rs.getString("customerID") != null &&
                                rs.getString("borrowDate") != null &&
                                rs.getString("borrowTime") != null &&
                                rs.getString("returnDate") != null &&
                                rs.getString("returnTime") != null &&
                                rs.getString("price") != null &&
                                rs.getString("employeeID") != null)
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

    private void retrieveNotReturnedLeaseData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leases");
            while (rs.next()) {
                if (
                        rs.getString("id") != null &&
                                rs.getString("leaseMachineID") != null &&
                                rs.getString("customerID") != null &&
                                rs.getString("borrowDate") != null &&
                                rs.getString("borrowTime") != null &&
                                rs.getString("returnDate") != null &&
                                rs.getString("returnTime") != null &&
                                rs.getFloat("price") == 0 &&
                                rs.getString("employeeID") != null)
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

    private void retrieveMachineData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM leaseMachines");
            while (rs.next()) {
                list_machines.getItems().add(rs.getString("name"));
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

    private void retrieveCustomers() {
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM customers");
            while (rs.next()) {
                String fullName = rs.getString("name") + " " + rs.getString("surname");
                list_customers.getItems().add(fullName);
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
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    private void runLeaseExpirationCheck() {
        LocalDateTime now = LocalDateTime.now();
        int numberOfExpired = 0;
        for (Lease l: leases) {
            if(l.getReturnDate().toLocalDate().atTime(l.getReturnTime().toLocalTime()).isBefore(now) && l.getPrice() == 0) {
                numberOfExpired++;
            }
        }
        if (numberOfExpired > 0) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Expired leases detected!");
            if (numberOfExpired == 1) a.setContentText("There is 1 lease with expired return date. Do you want to intervene?");
            else a.setContentText(String.format("There are %1$d leases with expired return dates. Do you want to intervene?", numberOfExpired));
            Button yesButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
            yesButton.setText("Yes");
            Button noButton = (Button) a.getDialogPane().lookupButton(ButtonType.CANCEL);
            noButton.setText("No");
            Optional<ButtonType> okResponse = a.showAndWait();
            if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get())) {
                leases.clear();
                retrieveNotReturnedLeaseData();
            }
        }
    }

    private void refreshTable() {
        leases.removeAll();
        leases = null;
        leases = FXCollections.observableArrayList();
        tableView_leases.getColumns().removeAll(leaseMachineIDCOl, customerIDCol, borrowDateCol, borrowTimeCol, returnDateCol, returnTimeCol, priceCol, employeeIDCol);
        retrieveLeaseData();
        initTableColumns(leases);
    }

    @FXML
    public void btn_search_clear_onClick() {
        text_search_query.clear();
        initTableColumns(leases);
    }

    @FXML
    public void text_search_query_onKeyReleased() {
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


    private void initTableColumns(ObservableList<Lease> source) {
        leaseMachineIDCOl = new TableColumn<>("Machine ID");
        leaseMachineIDCOl.setMinWidth(80);
        leaseMachineIDCOl.setCellValueFactory(new PropertyValueFactory<>("leaseMachineID"));

        customerIDCol = new TableColumn<>("Customer ID");
        customerIDCol.setMinWidth(150);
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        borrowDateCol = new TableColumn<>("Borrow date");
        borrowDateCol.setMinWidth(80);
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        borrowTimeCol = new TableColumn<>("Borrow time");
        borrowTimeCol.setMinWidth(80);
        borrowTimeCol.setCellValueFactory(new PropertyValueFactory<>("borrowTime"));

        returnDateCol = new TableColumn<>("Return date");
        returnDateCol.setMinWidth(80);
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        returnTimeCol = new TableColumn<>("Return time");
        returnTimeCol.setMinWidth(80);
        returnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnTime"));

        priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        employeeIDCol = new TableColumn<>("EmployeeID");
        employeeIDCol.setMinWidth(80);
        employeeIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        tableView_leases.setItems(source);
        tableView_leases.getColumns().addAll(leaseMachineIDCOl, customerIDCol, borrowDateCol, borrowTimeCol, returnDateCol, returnTimeCol, priceCol, employeeIDCol);
    }

    @FXML
    public void tableView_leases_onMouseClicked() {


        selectedLease = (Lease) tableView_leases.getFocusModel().getFocusedItem();

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(selectedLease.getBorrowDate().toLocalDate())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };

        datePicker_returnDate.setDayCellFactory(dayCellFactory);

        list_machines.getSelectionModel().select(getMachineName(selectedLease.getLeaseMachineID()));
        list_customers.getSelectionModel().select(getCustomerName(selectedLease.getCustomerID()));
        datePicker_borrowDate.setValue(selectedLease.getBorrowDate().toLocalDate());
        datePicker_returnDate.setValue(selectedLease.getReturnDate().toLocalDate());
        Machine machine = null;
        for(Machine m : machines) if (selectedLease.getLeaseMachineID() == m.getId()) machine = m;
        LocalDate d1 = datePicker_borrowDate.getValue();
        LocalDate d2 = LocalDate.now();
        text_price.setText(String.valueOf(d1.until(d2).getDays() * machine.getPricePerDay()));
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
            if (validateDates()) {
                System.out.println("Validation passed");
                conn = new DBConnect();
                try {
                    conn.upload(String.format("UPDATE leases SET leaseMachineID='%1$s',customerID='%2$s', borrowDate='%3$s', returnDate='%4$s' WHERE id='%5$d'",
                            getMachineID(String.valueOf(list_machines.getSelectionModel().getSelectedItem())),
                            getCustomerID(String.valueOf(list_customers.getSelectionModel().getSelectedItem())),
                            datePicker_borrowDate.getValue(),
                            datePicker_returnDate.getValue(),
                            selectedLease.getId()));
                } catch (Exception ex) {
                    displayMessage(ERROR, ex.getMessage());
                } finally {
                    updateMachineAvailability();
                    displayMessage(INFORMATION, "Lease updated successfully.");
                    refreshTable();

                }
            } else displayMessage(ERROR, "There has been a problem with one of the fields. Please, check that all fields are filled in correctly.");

        }
    }

    @FXML
    public void btn_delete_onClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an irreversible action!");
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
                            updateMachineAvailability();
                            displayMessage(INFORMATION, "Lease deleted successfully.");
                            refreshTable();
                        }

                    }
                }
            }
        }
    }

    @FXML
    public void btn_endLease_onClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an irreversible action!");
        alert.setContentText(String.format("Are you sure you want to end this lease?"));
        Button endButton = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
        endButton.setText("End");
        Optional<ButtonType> endResponse = alert.showAndWait();
        if(endResponse.isPresent()){
            if(ButtonType.OK.equals(endResponse.get())){
                conn = new DBConnect();
                try {
                    conn.upload(String.format("UPDATE leases SET returnDate='%1$s', price='%2$s' WHERE id='%3$d'",
                            LocalDate.now(),
                            text_price.getText(),
                            selectedLease.getId()));
                } catch (Exception ex) {
                    displayMessage(ERROR, ex.getMessage());
                } finally {

                    updateMachineAvailability();
                    updateEmployeeStatistics();
                    updateCustomerStatistics();
                    displayMessage(INFORMATION, "Lease ended successfully.");
                    refreshTable();
                }
            }
        }
    }

    private void updateMachineAvailability() {
        try {
            for (Machine m : machines)
                if (!isMachineInLease(m.getId()) && m.isLeased()) {
                    conn.upload(String.format("UPDATE leaseMachines SET leased='%1$s' WHERE id='%2$d'",
                            0,
                            m.getId()));
                }
            for (Machine m : machines)
                if (isMachineInLease(m.getId()) && !m.isLeased()) {
                    conn.upload(String.format("UPDATE leaseMachines SET leased='%1$s' WHERE id='%2$d'",
                            1,
                            m.getId()));
                }
        } catch (Exception ex){
            displayMessage(ERROR, ex.getMessage());
        }
    }


    private void updateEmployeeStatistics() {
        try {
            conn.upload(String.format("UPDATE employees SET numberOfSales='%1$s',totalRevenue='%2$s' WHERE id='%3$d'",
                    loggedUser.getNumberOfSales() + 1,
                    loggedUser.getTotalRevenue() + Double.valueOf(text_price.getText()),
                    loggedUser.getId()));
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void updateCustomerStatistics() {
        Customer customer = getCustomerWithID(selectedLease.getCustomerID());
        System.out.println(customer.getName() + " " + customer.getSalesMade() + " " + customer.getTotalSpent());
        try {
            conn.upload(String.format("UPDATE customers SET salesMade='%1$d',totalSpent='%2$s' WHERE id='%3$d'",
                    customer.getSalesMade() + 1,
                    customer.getTotalSpent() + Double.valueOf(text_price.getText()),
                    customer.getId()));
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private boolean validateDates() {
        if (datePicker_returnDate.getValue() != null && datePicker_borrowDate.getId() != null) {
            LocalDate d1 = datePicker_returnDate.getValue();
            LocalDate d2 = datePicker_borrowDate.getValue();
            return d1.isAfter(d2);
        }
        else return false;
    }

    private String getMachineName(int id) {
        String name = null;
        for (Machine m : machines) {
            if (m.getId() == id) name = m.getName();
        }
        return name;
    }

    private String getCustomerName(int id) {
        String name = null;
        for (Customer c : customers) {
            if (c.getId() == id) name = c.getName() + " " + c.getSurname();
        }
        return name;
    }

    private int getMachineID (String name) {
        int id = 0;
        for (Machine m : machines) {
            if (m.getName().equals(name)) id = m.getId();
        }
        return id;
    }

    private int getCustomerID (String fullName) {
        int id = 0;
        for (Customer c : customers) {
            if ((c.getName() + " " + c.getSurname()).equals(fullName)) id = c.getId();
        }
        return id;
    }

    private boolean isMachineInLease(int id) {
        for (Lease l : leases) if (l.getLeaseMachineID() == id && l.getPrice() == 0) return true;
        return false;
    }

    private Customer getCustomerWithID (int id) {
        for (Customer c : customers) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    @FXML
    public void btn_show_onClick(){
        switch (btn_show.getText()){
            case "Show leases not returned":
                leases.clear();
                retrieveNotReturnedLeaseData();
                btn_show.setText("Show all leases");
                break;
            case "Show all leases":
                leases.clear();
                retrieveLeaseData();
                btn_show.setText("Show leases not returned");
                break;
        }
    }
}