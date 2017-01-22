package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.BasketItem;
import com.teamSuperior.core.model.Discount;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.TextFieldBox;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ArrayList;

import static com.teamSuperior.core.Utils.*;
import static com.teamSuperior.guiApp.GUI.Error.*;
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
    @FXML
    public Label label_numOfItems;
    @FXML
    public Label label_overallPrice;


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
    private HashMap<String, String> categoryURLs;
    private ObservableList<Offer> offers;
    private ObservableList<Discount> discounts;



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

        categoryURLs = new HashMap<>();
        initCategories();

        offers = getOffersFromDatabase();
        discounts = getDiscountsFromDatabase();

        label_numOfItems.setText(String.format("Number of items in the basket: %d", basketItems.size()));
        label_overallPrice.setText("Total: kr. 0");

        listView_basket.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BasketItem>() {
            @Override
            public void changed(ObservableValue<? extends BasketItem> observable, BasketItem oldValue, BasketItem newValue) {
                selectedBasketItem = newValue;
            }
        });
    }

    private ObservableList<Discount> getDiscountsFromDatabase() {
        ObservableList<Discount> result = FXCollections.observableArrayList();
        conn = new DBConnect();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM discounts");
            while (rs.next()){
                result.add(new Discount(rs.getInt("id"),
                        rs.getDouble("value"),
                        rs.getString("title")));
            }
        } catch (SQLException sqlex) {
            displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
        return result;
    }

    private void initCategories() {
        conn = new DBConnect();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM productPictures");
            while (rs.next()){
                categoryURLs.put(rs.getString("category"), rs.getString("url"));
            }
        } catch (SQLException sqlex) {
            displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
        }
    }

    private ObservableList<Offer> getOffersFromDatabase(){
        conn = new DBConnect();
        ObservableList<Offer> results = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.getFromDataBase("SELECT offers.id,offers.date,offers.time,offers.productIDs,offers.price,offers.discount,offers.expiresDate,offers.expiresTime,products.name FROM offers,products WHERE offers.productIDs = products.id");
            while (rs.next()){
                if(rs.getInt("offers.id") != -1){
                    results.add(new Offer(rs.getDate("offers.date"),
                            rs.getInt("offers.id"),
                            rs.getInt("offers.productIDs"),
                            rs.getDouble("offers.price"),
                            rs.getDouble("offers.discount"),
                            rs.getString("products.name"),
                            rs.getTime("offers.time"),
                            rs.getDate("offers.expiresDate"),
                            rs.getTime("offers.expiresTime"),
                            isExpired(rs.getDate("offers.expiresDate"))));
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
        return results;
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

    private void printQueryLog(String sender, CheckComboBox<String> checkComboBox, TextField tf) {
        String c = "";
        for (String s : checkComboBox.getCheckModel().getCheckedItems()) {
            c += s + ", ";
        }
        System.out.printf("%s@[%s]: %s%n", sender, c, tf.getText());
    }

    @FXML
    public void text_searchProducts_query_onKeyReleased(KeyEvent keyEvent) {
        printQueryLog("searchProducts_query_onKeyReleased", checkComboBox_searchProducts_criteria, text_searchProducts_query);
        searchProductsResults = null;
        searchProductsResults = performProductSearch(text_searchProducts_query.getText());
        initProductTableColumns(searchProductsResults);
    }

    private ObservableList<Product> performProductSearch(String query) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return products;
        }
        for (Product product : products) {
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
            displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
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
        tableView_products.getColumns().addAll(productIdColumn,
                productNameColumn,
                productSubnameColumn,
                productCategoryColumn,
                productPriceColumn,
                productQuantityColumn,
                productBarcodeColumn,
                productLocationColumn,
                productContractorIdColumn);
    }

    @FXML
    public void btn_addToBasket_onClick(ActionEvent actionEvent) {
        String url = "";
        for (String key : categoryURLs.keySet()){
            if(selectedProduct.getCategory().equals(key)){
                url = categoryURLs.get(key);
            }
        }
        float price = selectedProduct.getPrice();
        String discount = "";
        for (Offer offer : offers){
            if(selectedProduct.getId() == offer.getProductID() && isValidOffer(offer.getExpiresDate())){
                price = (float)offer.getPrice();
                discount = String.valueOf(offer.getDiscount());
            }
        }

        basketItems.add(new BasketItem(selectedProduct.getId() ,selectedProduct.getName(), selectedProduct.getSubname(), price, url, discount));
        listView_basket.setItems(basketItems);
        listView_basket.setCellFactory(basketListView -> new BasketListViewCell());

        updateLabels();
    }

    private void updateLabels() {
        int q = 0;
        double tmp = 0;
        for (BasketItem basketItem : listView_basket.getItems()){
            tmp += basketItem.getPrice() * basketItem.getQuantity();
            q += basketItem.getQuantity();
        }
        label_numOfItems.setText(String.format("Number of items in the basket: %d", basketItems.size()));
        label_overallPrice.setText(String.format("Total: kr. %.2f", tmp));
    }

    @FXML
    public void btn_searchCustomers_clear_onClick(ActionEvent actionEvent) {
        text_searchCustomers_query.clear();
        initCustomerTableColumns(customers);
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
    }

    @FXML
    public void tableView_customers_onMouseClicked(MouseEvent mouseEvent) {
        selectedCustomer = (Customer)tableView_customers.getFocusModel().getFocusedItem();
    }

    @FXML
    public void text_searchCustomers_query_onKeyReleased(KeyEvent keyEvent) {
        printQueryLog("text_searchCustomers_query_onKeyReleased", checkComboBox_searchCustomers_criteria, text_searchCustomers_query);
        searchCustomersResults = null;
        searchCustomersResults = performCustomerSearch(text_searchCustomers_query.getText());
        initCustomerTableColumns(searchCustomersResults);
    }

    private ObservableList<Customer> performCustomerSearch(String query){
        ObservableList<Customer> results = FXCollections.observableArrayList();
        if(query.isEmpty()){
            return customers;
        }
        for (Customer c : customers){
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
        tableView_customers.getColumns().removeAll(customerNameColumn,
                customerSurnameColumn,
                customerAddressColumn,
                customerCityColumn,
                customerZipColumn,
                customerEmailColumn,
                customerPhoneColumn);

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
        tableView_customers.getColumns().addAll(customerNameColumn,
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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Assigned registered customer to the transaction.");
        a.setContentText(String.format("Name: %s\nSurname: %s\nEmail: %s\nPhone: %s",
                selectedCustomer.getName(),
                selectedCustomer.getSurname(),
                selectedCustomer.getEmail(),
                selectedCustomer.getPhone()));
        a.show();
    }

    @FXML
    public void btn_registerCustomer_onClick(ActionEvent actionEvent) {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/customersManage.fxml"));
                Stage window = new Stage();
                window.setTitle("View Customers");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void btn_completePurchase_onClick(ActionEvent actionEvent) {
        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //TODO: calculate the final price
        finalPrice = 0;
        for (BasketItem item : listView_basket.getItems()){
            finalPrice = (float)item.getPrice();
            for(Product p : products){
                if(item.getItemID() == p.getId()){
                    productIDs.add(p.getId());
                }
            }
        }




        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to complete the transaction?");
        a.setContentText("You will not be able to revert this action!");
        Optional<ButtonType> okResponse = a.showAndWait();
        if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get())) {
            conn = new DBConnect();
            try {
                conn.upload(String.format("INSERT INTO transactions (productIDs,employeeID,customerID,price,discountIDs,description,date,time) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s')",
                        Utils.arrayToString(productIDs),
                        loggedUser.getId(),
                        customerID,
                        finalPrice,
                        Utils.arrayToString(discountIDs),
                        description,
                        dtf_date.format(now),
                        dtf_time.format(now)));

            } catch (SQLException sqlEx){
                displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
            }
            displayMessage(INFORMATION, "Transaction completed successfully.");
        }
    }

    @FXML
    public void deleteItem_onClick(ActionEvent actionEvent) {
        basketItems.remove(selectedBasketItem);
        System.out.println(basketItems.size());
        listView_basket.refresh();
        updateLabels();
    }

    @FXML
    public void AddToBasket_contextMenu_onClick(ActionEvent actionEvent) {
        btn_addToBasket_onClick(null);
    }

    @FXML
    public void AddToBasketMore_contextMenu_onClick(ActionEvent actionEvent) {
        String ans = TextFieldBox.display("Product quantity", "Quantity");
        if(isInteger(ans)){
            if(Integer.parseInt(ans) <= selectedProduct.getQuantity()){
                String url = "";
                for (String key : categoryURLs.keySet()){
                    if(selectedProduct.getCategory().equals(key)){
                        url = categoryURLs.get(key);
                    }
                }
                float price = selectedProduct.getPrice();
                String discount = "";
                for (Offer offer : offers){
                    if(selectedProduct.getId() == offer.getProductID() && isValidOffer(offer.getExpiresDate())){
                        price = (float)offer.getPrice();
                        discount = String.valueOf(offer.getDiscount());
                    }
                }

                basketItems.add(new BasketItem(selectedProduct.getId(),
                        selectedProduct.getName(),
                        selectedProduct.getSubname(),
                        price,
                        url,
                        discount,
                        Integer.parseInt(ans)));
                listView_basket.setItems(basketItems);
                listView_basket.setCellFactory(basketListView -> new BasketListViewCell());

                updateLabels();
            } else {
                displayError(ErrorCode.NOT_ENOUGH_ITEMS);
            }
        } else {
            displayError(ErrorCode.TEXT_FIELD_NON_NUMERIC);
        }
    }
}



