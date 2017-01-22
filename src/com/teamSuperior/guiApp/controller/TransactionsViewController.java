package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.*;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by rajmu on 17.01.10.
 */
public class TransactionsViewController implements Initializable {
    @FXML
    public TextField text_search_query;
    @FXML
    public Button btn_search_clear;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public TableView tableView_offers;
    @FXML
    public TableView tableView_products;

    private TableColumn<Transaction, Integer> idColumn;
    private TableColumn<Transaction, String> productIDsColumn;
    private TableColumn<Transaction, Integer> customerIDColumn;
    private TableColumn<Transaction, Integer> employeeIDColumn;
    private TableColumn<Transaction, Double> priceColumn;
    private TableColumn<Transaction, String> discountIDsColumn;
    private TableColumn<Transaction, String> descriptionColumn;
    private TableColumn<Transaction, Date> dateColumn;
    private TableColumn<Transaction, Time> timeColumn;
    private TableColumn<Product, Integer> idCol;
    private TableColumn<Product, String> nameCol;
    private TableColumn<Product, String> subnameCol;
    private TableColumn<Product, String> barcodeCol;
    private TableColumn<Product, String> categoryCol;
    private TableColumn<Product, Float> priceCol;
    private TableColumn<Product, String> locationCol;
    private TableColumn<Product, Integer> quantityCol;
    private TableColumn<Product, Integer> contractorIdCol;

    private ObservableList<Transaction> transactions;
    private ObservableList<Transaction> searchResults;
    private ObservableList<Product> products;
    private DBConnect conn;
    private Transaction selectedTransaction;

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        tableView_offers.getColumns().removeAll(idColumn, productIDsColumn, customerIDColumn, priceColumn, discountIDsColumn, employeeIDColumn, dateColumn, timeColumn, descriptionColumn);
        initTransactionTableColumns(transactions);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_offers.getColumns().removeAll(idColumn, productIDsColumn, customerIDColumn, priceColumn, discountIDsColumn, employeeIDColumn, dateColumn, timeColumn, descriptionColumn);
        initTransactionTableColumns(searchResults);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactions = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        products = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("ID", "Discount", "Customer ID", "Employee ID", "product ID", "Date", "Description");
        retrieveData();
        initTransactionTableColumns(transactions);
        selectedTransaction = (Transaction)tableView_offers.getFocusModel().getFocusedItem();
        initProductsTableColumns(selectedTransaction);
    }

    private void retrieveData(){
        conn = new DBConnect();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM transactions");
            while (rs.next()){
                if(rs.getInt("id") != 0){
                    ArrayList<Integer> productIDs = stringToArray(rs.getString("productIDs"));
                    ArrayList<Integer> discountIDs = stringToArray(rs.getString("discountIDs"));
                    transactions.add(new Transaction(rs.getInt("id"),
                            rs.getInt("employeeID"),
                            rs.getInt("customerID"),
                            productIDs,
                            discountIDs,
                            rs.getDouble("price"),
                            rs.getString("description"),
                            rs.getDate("date"),
                            rs.getTime("time")));
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "Server connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTransactionTableColumns(ObservableList<Transaction> source){
        idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("id"));

        dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(50);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));

        descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("description"));

        discountIDsColumn = new TableColumn<>("Discount IDs");
        discountIDsColumn.setMinWidth(50);
        discountIDsColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("discountIDs_str"));

        employeeIDColumn = new TableColumn<>("employee ID");
        employeeIDColumn.setMinWidth(30);
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("employeeID"));

        customerIDColumn = new TableColumn<>("Customer ID");
        customerIDColumn.setMinWidth(30);
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("customerID"));


        priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(50);
        priceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("price"));

        productIDsColumn = new TableColumn<>("Product IDs");
        productIDsColumn.setMinWidth(50);
        productIDsColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("productIDs_str"));

        timeColumn = new TableColumn<>("Time");
        timeColumn.setMinWidth(60);
        timeColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Time>("time"));

        tableView_offers.setItems(source);
        tableView_offers.getColumns().addAll(idColumn, productIDsColumn, priceColumn, discountIDsColumn, employeeIDColumn, dateColumn, timeColumn, descriptionColumn);
    }

    public void tableView_offers_onMouseClicked(MouseEvent mouseEvent) {
        selectedTransaction = (Transaction)tableView_offers.getFocusModel().getFocusedItem();
        if(selectedTransaction != null){
            initProductsTableColumns(selectedTransaction);
        }
    }

    private void initProductsTableColumns(Transaction t){
        tableView_products.getColumns().removeAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
        conn = new DBConnect();
        products = null;
        products = FXCollections.observableArrayList();

        try{
            ResultSet rs = conn.getFromDataBase(String.format("SELECT * FROM products WHERE id IN (%1$s)", t.getProductIDs_str()));
            while (rs.next()){
                Product p = new Product(rs.getInt("id"),
                        rs.getInt("quantity"),
                        rs.getString("barcode"),
                        rs.getString("name"),
                        rs.getString("subname"),
                        rs.getString("category"),
                        rs.getString("warehouseLocation"),
                        (float) rs.getDouble("price"),
                        rs.getInt("contractorId"));
                products.add(p);
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "Server connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }

        idCol = new TableColumn<>("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));

        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        subnameCol = new TableColumn<>("Sub name");
        subnameCol.setMinWidth(150);
        subnameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("subname"));

        barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setMinWidth(60);
        barcodeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));

        categoryCol = new TableColumn<>("Category");
        categoryCol.setMinWidth(80);
        categoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));

        priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        locationCol = new TableColumn<>("Warehouse location");
        locationCol.setMinWidth(220);
        locationCol.setCellValueFactory(new PropertyValueFactory<Product, String>("warehouseLocation"));

        quantityCol = new TableColumn<>("Q");
        quantityCol.setMinWidth(30);
        quantityCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));

        contractorIdCol = new TableColumn<>("Contractor");
        contractorIdCol.setMinWidth(50);
        contractorIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("contractorId"));

        tableView_products.setItems(products);
        tableView_products.getColumns().addAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
    }

    private ObservableList<Transaction> performSearch(String query){
        ObservableList<Transaction> results = FXCollections.observableArrayList();
        if(query.isEmpty()){
            return transactions;
        }
        for (Transaction t : transactions){
            for (String criteria : checkComboBox_search_criteria.getCheckModel().getCheckedItems()){
                switch (criteria){
                    case "ID":
                        if(String.valueOf(t.getId()).contains(query)){
                            results.add(t);
                        }
                        break;
                    case "Discount":
                        if(t.getDiscountIDs_str().contains(query)){
                            results.add(t);
                        }
                        break;
                    case "Employee ID":
                        if(String.valueOf(t.getEmployeeID()).contains(query)){
                            results.add(t);
                        }
                        break;

                    case "Customer ID":
                        if(String.valueOf(t.getCustomerID()).contains(query)){
                            results.add(t);
                        }
                        break;

                    case "Product ID":
                        if(t.getProductIDs_str().contains(query)){
                            results.add(t);
                        }
                        break;
                    case "Date":
                        if(String.valueOf(t.getDate()).contains(query)){
                            results.add(t);
                        }
                        break;
                    case "Description":
                        if(t.getDescription().contains(query)){
                            results.add(t);
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
