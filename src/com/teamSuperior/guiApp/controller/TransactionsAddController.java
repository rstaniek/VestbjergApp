package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.BasketItem;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.controller.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.ArrayList;

import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Jakub on 19.01.2017.
 */
public class TransactionsAddController implements Initializable {
    @FXML
    public Button btn_searchProducts_clear;
    @FXML
    public TextField text_searchProducts_query;
    @FXML
    public CheckComboBox<String> checkComboBox_searchProducts_criteria;
    @FXML
    public TableView tableView_products;
    @FXML
    public Button btn_addToBasket;
    @FXML
    public TableView tableView_customers;
    @FXML
    public Button btn_assignCustomer;
    @FXML
    public Button btn_searchCustomers_clear;
    @FXML
    public TextField text_searchCustomers_query;
    @FXML
    public CheckComboBox<String> checkComboBox_searchCustomers_criteria;
    @FXML
    public Button btn_registerCustomer;
    @FXML
    public Button btn_completePurchase;
    @FXML
    public ListView<BasketItem> listView_basket;


    //products table columns
    private TableColumn<Product, Integer> productIdColumn;
    private TableColumn<Product, String> productNameColumn;
    private TableColumn<Product, String> productSubnameColumn;
    private TableColumn<Product, String> productBarcodeColumn;
    private TableColumn<Product, String> productCategoryColumn;
    private TableColumn<Product, Float> productPriceColumn;
    private TableColumn<Product, String> productLocationColumn;
    private TableColumn<Product, Integer> productQuantityColumn;
    private TableColumn<Product, Integer> productContractorIdColumn;

    //customer table columns
    private TableColumn<Customer, Integer> customerIdColumn;
    private TableColumn<Customer, Integer> customerSalesColumn;
    private TableColumn<Customer, Double> customerTotalSpentColumn;
    private TableColumn<Customer, String> customerNameColumn;
    private TableColumn<Customer, String> customerSurnameColumn;
    private TableColumn<Customer, String> customerAddressColumn;
    private TableColumn<Customer, String> customerCityColumn;
    private TableColumn<Customer, String> customerZipColumn;
    private TableColumn<Customer, String> customerEmailColumn;
    private TableColumn<Customer, String> customerPhoneColumn;

    //fields for the new transaction
    private ArrayList<Integer> productIDs;
    private float finalPrice;
    private ArrayList<Integer> discountIDs;
    private String description;
    private int customerID;

    private Transaction transaction;
    private ObservableList<Product> products;
    private ObservableList<Customer> customers;
    private ObservableList<Product> searchProductsResults;
    private ObservableList<Customer> searchCustomersResults;
    private static Employee loggedUser;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private DBConnect conn;
    private ObservableList<BasketItem> basketItems;
    private BasketItem selectedBasketItem;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = new DBConnect();
        loggedUser = UserController.getUser();
        basketItems = FXCollections.observableArrayList();

        products = FXCollections.observableArrayList();
        searchProductsResults = FXCollections.observableArrayList();
        checkComboBox_searchProducts_criteria.getItems().addAll("ID", "Barcode", "Name", "Subname", "Category", "Location", "Price", "Contractor ID");
        retrieveProductsData();
        initProductTableColumns(products);
        tableView_products.getSelectionModel().selectFirst();
        selectedProduct = (Product) tableView_products.getSelectionModel().getSelectedItem();

        customers = FXCollections.observableArrayList();
        searchCustomersResults = FXCollections.observableArrayList();
        checkComboBox_searchCustomers_criteria.getItems().addAll("ID", "Name", "Surname", "Address", "City", "ZIP");
        retrieveCustomerData();
        initCustomerTableColumns(customers);
        tableView_customers.getSelectionModel().selectFirst();
        selectedCustomer = (Customer) tableView_customers.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void btn_searchProducts_clear_onClick(ActionEvent actionEvent) {
        text_searchProducts_query.clear();
        initProductTableColumns(products);
        selectedProduct = (Product) tableView_products.getFocusModel().getFocusedItem();
    }

    @FXML
    public void tableView_products_onMouseClicked(MouseEvent mouseEvent) {
        selectedProduct = (Product) tableView_products.getFocusModel().getFocusedItem();
    }

    @FXML
    public void text_searchProducts_query_onKeyReleased(KeyEvent keyEvent) {
        searchProductsResults = null;
        searchProductsResults = performProductSearch(text_searchProducts_query.getText());
        tableView_products.getColumns().removeAll(productIdColumn,
                productNameColumn,
                productSubnameColumn,
                productBarcodeColumn,
                productCategoryColumn,
                productPriceColumn,
                productLocationColumn,
                productQuantityColumn,
                productContractorIdColumn);
        initProductTableColumns(searchProductsResults);
    }

    private ObservableList<Product> performProductSearch(String query) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        ObservableList<Product> source = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return source;
        }
        for (Product product : source) {
            for (String criteria : checkComboBox_searchProducts_criteria.getCheckModel().getCheckedItems()) {
                switch (criteria) {
                    case "ID":
                        if (String.valueOf(product.getId()).contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Barcode":
                        if (product.getBarcode().contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Name":
                        if (product.getName().contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Subname":
                        if (product.getSubname().contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Category":
                        if (product.getCategory().contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Location":
                        if (product.getWarehouseLocation().contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Price":
                        if (String.valueOf(product.getPrice()).contains(query)) {
                            results.add(product);
                        }
                        break;
                    case "Contractor ID":
                        if (String.valueOf(product.getContractorId()).contains(query)) {
                            results.add(product);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }

    private void retrieveProductsData() {
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM products");
            while (rs.next()) {
                if (rs.getInt("id") != 0 &&
                        rs.getInt("quantity") != -1 &&
                        rs.getString("barcode") != null &&
                        rs.getString("name") != null &&
                        rs.getString("subname") != null &&
                        rs.getString("category") != null &&
                        rs.getString("warehouseLocation") != null &&
                        rs.getDouble("price") > -1 &&
                        rs.getInt("contractorId") != -1) {
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
            }
        } catch (SQLException sqlex) {
            Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    private void initProductTableColumns(ObservableList<Product> source){
        tableView_products.getColumns().removeAll(productBarcodeColumn,
                productCategoryColumn,
                productContractorIdColumn,
                productIdColumn,
                productLocationColumn,
                productNameColumn,
                productPriceColumn,
                productQuantityColumn,
                productSubnameColumn);

        productBarcodeColumn = new TableColumn<>("Barcode");
        productBarcodeColumn.setMinWidth(80);
        productBarcodeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));

        productCategoryColumn = new TableColumn<>("Category");
        productCategoryColumn.setMinWidth(50);
        productCategoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));

        productContractorIdColumn = new TableColumn<>("Contractor ID");
        productContractorIdColumn.setMinWidth(30);
        productContractorIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("contractorId"));

        productIdColumn = new TableColumn<>("product ID");
        productIdColumn.setMinWidth(30);
        productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));

        productLocationColumn = new TableColumn<>("Location");
        productLocationColumn.setMinWidth(60);
        productLocationColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("warehouseLocation"));

        productNameColumn = new TableColumn<>("Name");
        productNameColumn.setMinWidth(50);
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        productPriceColumn = new TableColumn<>("Price");
        productPriceColumn.setMinWidth(50);
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        productQuantityColumn = new TableColumn<>("Q");
        productQuantityColumn.setMinWidth(30);
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));

        productSubnameColumn = new TableColumn<>("Sub name");
        productSubnameColumn.setMinWidth(50);
        productSubnameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("subname"));


        tableView_products.setItems(source);
        tableView_products.getColumns().addAll(productBarcodeColumn,
                productCategoryColumn,
                productContractorIdColumn,
                productIdColumn,
                productLocationColumn,
                productNameColumn,
                productPriceColumn,
                productQuantityColumn,
                productSubnameColumn);
    }

    @FXML
    public void btn_addToBasket_onClick(ActionEvent actionEvent) {
        basketItems.add(new BasketItem(selectedProduct.getName(), selectedProduct.getSubname(), selectedProduct.getPrice()));
        listView_basket.setItems(basketItems);
        listView_basket.setCellFactory(basketListView -> new BasketListViewCell());

        listView_basket.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BasketItem>() {
            @Override
            public void changed(ObservableValue<? extends BasketItem> observable, BasketItem oldValue, BasketItem newValue) {
                selectedBasketItem = newValue;
            }
        });
    }

    @FXML
    public void btn_searchCustomers_clear_onClick(ActionEvent actionEvent) {
        text_searchCustomers_query.clear();
        initCustomerTableColumns(searchCustomersResults);
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
    }

    @FXML
    public void tableView_customers_onMouseClicked(MouseEvent mouseEvent) {
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
    }

    @FXML
    public void text_searchCustomers_query_onKeyReleased(KeyEvent keyEvent) {
        searchCustomersResults = null;
        searchCustomersResults = performCustomerSearch(text_searchProducts_query.getText());
        tableView_customers.getColumns().removeAll(customerIdColumn,
                customerSalesColumn,
                customerTotalSpentColumn,
                customerNameColumn,
                customerSurnameColumn,
                customerAddressColumn,
                customerCityColumn,
                customerZipColumn,
                customerEmailColumn,
                customerPhoneColumn);
        initCustomerTableColumns(searchCustomersResults);
    }

    private ObservableList<Customer> performCustomerSearch(String query){
        ObservableList<Customer> results = FXCollections.observableArrayList();
        if(query.isEmpty()){
            return searchCustomersResults;
        }
        for (Customer c : searchCustomersResults){
            for (String criteria : checkComboBox_searchCustomers_criteria.getCheckModel().getCheckedItems()){
                switch (criteria){
                    case "ID":
                        if (String.valueOf(c.getId()).contains(query)){
                            results.add(c);
                        }
                        break;
                    case "name":
                        if(c.getName().contains(query)){
                            results.add(c);
                        }
                        break;
                    case "Surname":
                        if(c.getSurname().contains(query)){
                            results.add(c);
                        }
                        break;
                    case "Address":
                        if(c.getAddress().contains(query)){
                            results.add(c);
                        }
                        break;
                    case "City":
                        if(c.getCity().contains(query)){
                            results.add(c);
                        }
                        break;
                    case "ZIP":
                        if(c.getZip().contains(query)){
                            results.add(c);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }

    private void retrieveCustomerData(){
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
                for (Customer c : customers){
                    System.out.println(c.toString());
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initCustomerTableColumns(ObservableList<Customer> source){
        tableView_customers.getColumns().removeAll(customerIdColumn,
                customerSalesColumn,
                customerTotalSpentColumn,
                customerNameColumn,
                customerSurnameColumn,
                customerAddressColumn,
                customerCityColumn,
                customerZipColumn,
                customerEmailColumn,
                customerPhoneColumn);

        customerIdColumn = new TableColumn<>("ID");
        customerIdColumn.setMinWidth(30);
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        customerSalesColumn = new TableColumn<>("Sales");
        customerSalesColumn.setMinWidth(30);
        customerSalesColumn.setCellValueFactory(new PropertyValueFactory<>("salesMade"));

        customerTotalSpentColumn = new TableColumn<>("Total spent");
        customerTotalSpentColumn.setMinWidth(60);
        customerTotalSpentColumn.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));

        customerNameColumn = new TableColumn<>("Name");
        customerNameColumn.setMinWidth(50);
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        customerSurnameColumn = new TableColumn<>("Surname");
        customerSurnameColumn.setMinWidth(50);
        customerSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        customerAddressColumn = new TableColumn<>("Address");
        customerAddressColumn.setMinWidth(60);
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        customerCityColumn = new TableColumn<>("City");
        customerCityColumn.setMinWidth(60);
        customerCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        customerZipColumn = new TableColumn<>("ZIP");
        customerZipColumn.setMinWidth(40);
        customerZipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));

        customerEmailColumn = new TableColumn<>("email");
        customerEmailColumn.setMinWidth(80);
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        customerPhoneColumn = new TableColumn<>("Phone");
        customerPhoneColumn.setMinWidth(60);
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tableView_customers.setItems(source);
        tableView_customers.getColumns().addAll(customerIdColumn,
                customerSalesColumn,
                customerTotalSpentColumn,
                customerNameColumn,
                customerSurnameColumn,
                customerAddressColumn,
                customerCityColumn,
                customerZipColumn,
                customerEmailColumn,
                customerPhoneColumn);
    }

    @FXML
    public void btn_assignCustomer_onClick(ActionEvent actionEvent) {
        customerID = selectedCustomer.getId();
    }

    @FXML
    public void btn_registerCustomer_onClick(ActionEvent actionEvent) {
        //TODO: open the window for registering customer
    }

    @FXML
    public void btn_completePurchase_onClick(ActionEvent actionEvent) {
        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if (true) { //TODO perhaps here should be some validation of fields
            conn = new DBConnect();
            try {
                conn.upload(String.format("INSERT INTO transactions (productIDs,employeeID,customerID,price,discountIDs,description,date,time) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s')",
                        productIDs, //TODO: solve this in the basket
                        loggedUser.getId(),
                        customerID,
                        finalPrice, //TODO: solve this in the basket
                        discountIDs, //TODO: solve this in the basket
                        description,
                        dtf_date.format(now),
                        dtf_time.format(now)));

            } catch (SQLException sqlEx){
                displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
            }
            displayMessage(INFORMATION, "Transaction completed successfully.");
        }
    }
}



