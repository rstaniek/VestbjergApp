package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.BasketItem;
import com.teamSuperior.core.model.Discount;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.TextFieldBox;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.teamSuperior.core.Utils.*;
import static com.teamSuperior.core.controlLayer.WebsiteCrawler.getExchangeRatioBloomberg;
import static com.teamSuperior.core.enums.Currency.DKKEUR;
import static com.teamSuperior.core.enums.Currency.DKKUSD;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Transactions add controller
 */
public class TransactionsAddController implements Initializable {
    private static final int QUANTITY_DISCOUNT_THRESHOLD = 20000;
    private static final String[] CURRENCIES = new String[]{"kr.", "$", "€"};
    private static final String[] PRODUCTS_CRITERIA = new String[]{"ID", "Barcode", "Name", "Subname", "Category", "Location", "Price", "Contractor ID"};
    private static final String[] CUSTOMERS_CRITERIA = new String[]{"ID", "Name", "Surname", "Address", "City", "ZIP"};

    private static Controller<Product, Integer> productController = new Controller<>(Product.class);
    private static Controller<Customer, Integer> customerController = new Controller<>(Customer.class);

    private static Employee loggedUser;
    @FXML
    public Button clearSearchProductsButton;
    @FXML
    public TextField searchProductsField;
    @FXML
    public CheckComboBox<String> searchProductsCriteriaComboBox;
    @FXML
    public TableView<Product> productsTableView;
    @FXML
    public Button addToBasketButton;
    @FXML
    public TableView<Customer> customersTableView;
    @FXML
    public Button assignCustomerButton;
    @FXML
    public Button clearSearchCustomersButton;
    @FXML
    public TextField searchCustomersQueryField;
    @FXML
    public CheckComboBox<String> searchCustomersCriteriaComboBox;
    @FXML
    public Button registerCustomerButton;
    @FXML
    public Button completePurchaseButton;
    @FXML
    public ListView<BasketItem> basketListView;
    @FXML
    public Label numOfItemsLabel;
    @FXML
    public Label overallPriceLabel;
    @FXML
    public Label finalPriceLabel;
    @FXML
    public Label discountLabel;
    @FXML
    public Label assignedCustomerLabel;
    @FXML
    public TextField descriptionField;
    @FXML
    public Button clearBasketButton;
    @FXML
    public CheckBox selfPickupCheckBox;
    @FXML
    public CheckBox craftsmanCheckBox;
    @FXML
    public ComboBox<String> currencyComboBox;
    //products table columns
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> productSubnameColumn;
    @FXML
    private TableColumn<Product, String> productCategoryColumn;
    @FXML
    private TableColumn<Product, Float> productPriceColumn;
    @FXML
    private TableColumn<Product, String> productLocationColumn;
    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;
    //customer table columns
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerSurnameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerCityColumn;
    @FXML
    private TableColumn<Customer, String> customerZipColumn;
    @FXML
    private TableColumn<Customer, String> customerEmailColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    //fields for the new transaction
    private double finalPrice;
    private double finalPriceOnLabels;
    private double noDiscountPrice;
    private double noDiscountPriceOnLabels;
    private ArrayList<Integer> discountIDs;
    private int customerID;
    private double discountThreshold;
    private ObservableList<Product> products;
    private ObservableList<Customer> customers;
    private ObservableList<Product> searchProductsResults;
    private ObservableList<Customer> searchCustomersResults;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private DBConnect conn;
    private ObservableList<BasketItem> basketItems;
    private BasketItem selectedBasketItem;
    private ObservableList<Offer> offers;
    private ObservableList<Discount> discounts;
    private String currency;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCustomer = customersTableView.getFocusModel().getFocusedItem();
        conn = new DBConnect();
        loggedUser = UserController.getUser();
        basketItems = FXCollections.observableArrayList();

        products = FXCollections.observableArrayList();
        searchProductsResults = FXCollections.observableArrayList();
        searchProductsCriteriaComboBox.getItems().addAll(PRODUCTS_CRITERIA);
        retrieveProductsData();
        initProductTableColumns(products);
        productsTableView.getSelectionModel().selectFirst();
        selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

        customers = FXCollections.observableArrayList();
        searchCustomersResults = FXCollections.observableArrayList();
        searchCustomersCriteriaComboBox.getItems().addAll(CUSTOMERS_CRITERIA);
        searchCustomersCriteriaComboBox.getCheckModel().checkAll();
        retrieveCustomerData();
        initCustomerTableColumns(customers);
        customersTableView.getSelectionModel().selectFirst();
        selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        offers = getOffersFromDatabase();
        discounts = getDiscountsFromDatabase();

        numOfItemsLabel.setText(String.format("Number of items in the basket: %d", basketItems.size()));
        overallPriceLabel.setText("Total: kr. 0");

        customerID = -1;
        discountIDs = new ArrayList<>();
        discountIDs.add(-1);

        currencyComboBox.setItems(FXCollections.observableArrayList("DKK", "USD", "EUR"));
        currency = CURRENCIES[0];
        currencyComboBox.getSelectionModel().select(0);

        for (Discount d : discounts) {
            if (d.getId() == 5) {
                discountThreshold = d.getValue();
            }
        }

        basketListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedBasketItem = newValue);

        selfPickupCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateLabels());

        craftsmanCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateLabels());

        currencyComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            currency = CURRENCIES[newValue.intValue()];
            updateLabels();
        });
    }

    private Employee retrieveEmploeeData() {
        conn = new DBConnect();
        Employee e;
        try {
            ResultSet rs = conn.getFromDataBase(String.format("SELECT * FROM employees WHERE id='%s'", loggedUser.getId()));
            rs.next();
            e = new Employee(rs.getInt("id"),
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
                    rs.getInt("accessLevel"));
        } catch (SQLException sqlex) {
            displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlex.getMessage());
            e = null;
        } catch (Exception ex) {
            displayMessage(Alert.AlertType.ERROR, "Unexpected Exception", ex.getMessage());
            e = null;
        }
        return e;
    }

    private ObservableList<Discount> getDiscountsFromDatabase() {
        ObservableList<Discount> result = FXCollections.observableArrayList();
        conn = new DBConnect();
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM discounts");
            while (rs.next()) {
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

    private ObservableList<Offer> getOffersFromDatabase() {
        conn = new DBConnect();
        ObservableList<Offer> results = FXCollections.observableArrayList();
        try {
            ResultSet rs = conn.getFromDataBase("SELECT offers.id,offers.date,offers.time,offers.productIDs,offers.price,offers.discount,offers.expiresDate,offers.expiresTime,products.name FROM offers,products WHERE offers.productIDs = products.id");
            while (rs.next()) {
                if (rs.getInt("offers.id") != -1) {
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
    public void clickClearSearchProducts() {
        searchProductsField.clear();
        initProductTableColumns(products);
        selectedProduct = productsTableView.getFocusModel().getFocusedItem();
    }

    @FXML
    public void clickProductsTableView() {
        selectedProduct = productsTableView.getFocusModel().getFocusedItem();
    }

    private void printQueryLog(String sender, CheckComboBox<String> checkComboBox, TextField tf) {
        String c = "";
        for (String s : checkComboBox.getCheckModel().getCheckedItems()) {
            c += s + ", ";
        }
        System.out.printf("%s@[%s]: %s%n", sender, c, tf.getText());
    }

    @FXML
    public void text_searchProducts_query_onKeyReleased() {
        printQueryLog("searchProducts_query_onKeyReleased", searchProductsCriteriaComboBox, searchProductsField);
        searchProductsResults = null;
        searchProductsResults = performProductSearch(searchProductsField.getText());
        initProductTableColumns(searchProductsResults);
    }

    private ObservableList<Product> performProductSearch(String query) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return products;
        }
        for (Product product : products) {
            ObservableList<String> criteriaList;
            if (searchProductsCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
                criteriaList = searchProductsCriteriaComboBox.getCheckModel().getCheckedItems();
            } else {
                criteriaList = FXCollections.observableArrayList(PRODUCTS_CRITERIA);
            }
            for (String criteria : criteriaList) {
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
                        if (String.valueOf(product.getCategory()).contains(query)) {
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
                        if (String.valueOf(product.getContractor()).contains(query)) {
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
        products = FXCollections.observableArrayList(productController.getAll());
    }

    private void initProductTableColumns(ObservableList<Product> source) {
        productCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productLocationColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseLocation"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productSubnameColumn.setCellValueFactory(new PropertyValueFactory<>("subname"));

        productsTableView.setItems(source);
    }

    @FXML
    public void clickAddToBasket() {
        if (selectedProduct.getQuantity() > 0) {
            double price = selectedProduct.getPrice();
            String discount = "";
            for (Offer offer : offers) {
                if (selectedProduct.getId() == offer.getProductID() && isValidOffer(offer.getExpiresDate())) {
                    price = offer.getPrice();
                    discount = String.valueOf(offer.getDiscount());
                }
            }

            basketItems.add(new BasketItem(selectedProduct.getId(), selectedProduct.getName(), selectedProduct.getSubname(), price, selectedProduct.getCategory().getUrl(), discount));
            basketListView.setItems(basketItems);
            basketListView.setCellFactory(basketListView -> new BasketListViewCell());
            updateLabels();
        } else {
            displayError(ErrorCode.NOT_ENOUGH_ITEMS);
        }
    }

    private void updateLabels() {
        Locale loc = new Locale("da", "DK");
        NumberFormat formatter = NumberFormat.getInstance(loc);
        int q = basketItems.size();
        calculatePrice();
        calculateDiscount();
        numOfItemsLabel.setText(String.format("Number of items in the basket: %d", q));
        if (currencyComboBox.getSelectionModel().getSelectedIndex() != 2) {
            overallPriceLabel.setText(String.format("Price without discount: %1$s %2$.2f", currency, noDiscountPriceOnLabels));
            finalPriceLabel.setText(String.format("Final price: %2$s %1$s", formatter.format(finalPriceOnLabels), currency));
        } else {
            overallPriceLabel.setText(String.format("Price without discount: %2$.2f%1$s", currency, noDiscountPriceOnLabels));
            finalPriceLabel.setText(String.format("Final price: %s%s", formatter.format(finalPriceOnLabels), currency));
        }
    }

    private void calculatePrice() {
        double tmp = 0;
        for (BasketItem basketItem : basketListView.getItems()) {
            tmp += basketItem.getPrice() * basketItem.getQuantity();
        }
        noDiscountPrice = (float) tmp;
        String dkkusd = getExchangeRatioBloomberg(DKKUSD);
        String dkkeur = getExchangeRatioBloomberg(DKKEUR);
        while (!isNumeric(dkkusd)) {
            System.out.println("Retrieving DKK-USD currency ratio failed, trying once again...");
            dkkusd = getExchangeRatioBloomberg(DKKUSD);
        }
        while (!isNumeric(dkkeur)) {
            System.out.println("Retrieving DKK-EUR currency ratio failed, trying once again...");
            dkkeur = getExchangeRatioBloomberg(DKKEUR);
        }
        System.out.println("Selected currency: " + currency);
        System.out.println("DKKUSD: " + dkkusd);
        System.out.println("DKKEUR: " + dkkeur);
        switch (currency) {
            case "kr.":
                finalPriceOnLabels = finalPrice;
                noDiscountPriceOnLabels = noDiscountPrice;
                break;
            case "$":
                finalPriceOnLabels = finalPrice * (float) Double.parseDouble(dkkusd);
                noDiscountPriceOnLabels = noDiscountPrice * (float) Double.parseDouble(dkkusd);
                break;
            case "€":
                finalPriceOnLabels = finalPrice * (float) Double.parseDouble(dkkeur);
                noDiscountPriceOnLabels = noDiscountPrice * (float) Double.parseDouble(dkkeur);
                break;
        }
        System.out.println("finalPrice: " + finalPrice);
        System.out.println("noDiscountPrice: " + noDiscountPrice);
    }

    private void calculateDiscount() {
        //first it checks if the quantity discount is applicable and applies it if so (right now it's set at 20 000 kr.)
        if (noDiscountPrice > QUANTITY_DISCOUNT_THRESHOLD) {
            if (!discountIDs.contains(3)) discountIDs.add(3);
        } else if (discountIDs.contains(3)) discountIDs.remove(discountIDs.indexOf(3));
        if (selfPickupCheckBox.isSelected() && !discountIDs.contains(4)) discountIDs.add(4);
        else if (discountIDs.contains(4) && !selfPickupCheckBox.isSelected())
            discountIDs.remove(discountIDs.indexOf(4));
        if (craftsmanCheckBox.isSelected() && !discountIDs.contains(2)) discountIDs.add(2);
        else if (discountIDs.contains(2) && !craftsmanCheckBox.isSelected())
            discountIDs.remove(discountIDs.indexOf(2));
        double discount = 0.0;
        for (int id : discountIDs) {
            for (Discount d : discounts) {
                if (d.getId() == id) {
                    discount += d.getValue();
                }
            }
        }
        if (discount > discountThreshold) discount = discountThreshold;
        discountLabel.setText(String.format("Total discount: %.1f", discount));
        finalPrice = noDiscountPrice * ((100 - (float) discount) / 100);
        finalPriceOnLabels = noDiscountPriceOnLabels * ((100 - (float) discount) / 100);
    }

    @FXML
    public void clickClearBasket() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to clear the basket?");
        a.setContentText("You will not be able to revert this action.");
        Optional<ButtonType> okResponse = a.showAndWait();
        if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get())) {
            basketItems = null;
            basketItems = FXCollections.observableArrayList();
            System.out.println("Number of items in the basket: " + basketItems.size());
            basketListView.setItems(basketItems);
            basketListView.refresh();
            updateLabels();
        }
    }

    @FXML
    public void btn_searchCustomers_clear_onClick() {
        searchCustomersQueryField.clear();
        initCustomerTableColumns(customers);
        selectedCustomer = customersTableView.getFocusModel().getFocusedItem();
    }

    @FXML
    public void clickCustomersTableView() {
        selectedCustomer = customersTableView.getFocusModel().getFocusedItem();
    }

    @FXML
    public void text_searchCustomers_query_onKeyReleased() {
        printQueryLog("text_searchCustomers_query_onKeyReleased", searchCustomersCriteriaComboBox, searchCustomersQueryField);
        searchCustomersResults = null;
        searchCustomersResults = performCustomerSearch(searchCustomersQueryField.getText());
        initCustomerTableColumns(searchCustomersResults);
    }

    private ObservableList<Customer> performCustomerSearch(String query) {
        ObservableList<Customer> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return customers;
        }
        for (Customer c : customers) {
            ObservableList<String> searchCriteriaList;
            if (searchCustomersCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
                searchCriteriaList = searchCustomersCriteriaComboBox.getCheckModel().getCheckedItems();
            } else {
                searchCriteriaList = FXCollections.observableArrayList(CUSTOMERS_CRITERIA);
            }
            for (String criteria : searchCriteriaList) {
                switch (criteria) {
                    case "ID":
                        if (String.valueOf(c.getId()).contains(query)) {
                            results.add(c);
                        }
                        break;
                    case "name":
                        if (c.getName().contains(query)) {
                            results.add(c);
                        }
                        break;
                    case "Surname":
                        if (c.getSurname().contains(query)) {
                            results.add(c);
                        }
                        break;
                    case "Address":
                        if (c.getAddress().contains(query)) {
                            results.add(c);
                        }
                        break;
                    case "City":
                        if (c.getCity().contains(query)) {
                            results.add(c);
                        }
                        break;
                    case "ZIP":
                        if (c.getZip().contains(query)) {
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

    private void retrieveCustomerData() {
        customers = FXCollections.observableArrayList(customerController.getAll());
    }

    private void initCustomerTableColumns(ObservableList<Customer> source) {
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerZipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        customersTableView.setItems(source);
    }

    @FXML
    public void clickAssignCustomer() {
        customerID = selectedCustomer.getId();
        assignedCustomerLabel.setText(String.format("Assigned customer: %1$s %2$s", selectedCustomer.getName(), selectedCustomer.getSurname()));
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Assigned registered customer to the transaction.");
        a.setContentText(String.format("Name: %s\nSurname: %s\nEmail: %s\nPhone: %s",
                selectedCustomer.getName(),
                selectedCustomer.getSurname(),
                selectedCustomer.getEmail(),
                selectedCustomer.getPhone()));
        a.show();
        if (!discountIDs.contains(1)) {
            discountIDs.add(1);
        }
        updateLabels();
    }

    @FXML
    public void clickRegisterCustomer() {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/customersManage.fxml"));
                Stage window = new Stage();
                window.setTitle("View Customers");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.showAndWait();
                refreshCustomerTable();
            } catch (IOException ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    private void refreshCustomerTable() {
        customers = FXCollections.observableArrayList();
        retrieveCustomerData();
        initCustomerTableColumns(customers);
    }

    private ArrayList<Integer> getProductIDs() {
        ArrayList<Integer> productIDs = new ArrayList<>();
        for (BasketItem b : basketItems) {
            productIDs.add(b.getItemID());
        }
        return productIDs;
    }

    private boolean verifyFields() {
        if (descriptionField.getText().isEmpty()) descriptionField.setText("no description");
        if (discountIDs.size() >= 2) discountIDs.remove(discountIDs.indexOf(-1));
        descriptionField.setText(descriptionField.getText().replace("'", "''"));
        discountIDs.sort(Comparator.naturalOrder());
        return !basketItems.isEmpty();
    }

    @FXML
    public void btn_completePurchase_onClick() {
        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        loggedUser = retrieveEmploeeData(); //retrieves most current employee data
        if (loggedUser == null) loggedUser = retrieveEmploeeData(); //if failure tries once again
        if (loggedUser == null)
            loggedUser = UserController.getUser(); //if second failure gets the employee from userConreoller

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to complete the transaction?");
        a.setContentText("You will not be able to revert this action!");
        Optional<ButtonType> okResponse = a.showAndWait();
        if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get()) && verifyFields()) {
            conn = new DBConnect();
            try {
                conn.upload(String.format("INSERT INTO transactions (productIDs,employeeID,customerID,price,discountIDs,description,date,time) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s')",
                        Utils.arrayToString(getProductIDs()),
                        loggedUser.getId(),
                        customerID,
                        finalPrice,
                        Utils.arrayToString(discountIDs),
                        descriptionField.getText(),
                        dtf_date.format(now),
                        dtf_time.format(now)));
                if (customerID != -1) {
                    conn.upload(String.format("UPDATE customers SET salesMade='%1$d',totalSpent='%2$s' WHERE id='%3$d'",
                            selectedCustomer.getSalesMade() + 1,
                            selectedCustomer.getTotalSpent() + finalPrice,
                            customerID));
                }
                conn.upload(String.format("UPDATE employees SET numberOfSales='%1$s',totalRevenue='%2$s' WHERE id='%3$d'",
                        loggedUser.getNumberOfSales() + 1,
                        loggedUser.getTotalRevenue() + finalPrice,
                        loggedUser.getId()));
                String productsQuery = "";
                for (BasketItem item : basketItems) {
                    int qLeft = -1;
                    for (Product p : products) {
                        if (p.getId() == item.getItemID()) {
                            qLeft = p.getQuantity() - item.getQuantity();
                        }
                    }
                    productsQuery += String.format("UPDATE products SET quantity=%d WHERE id=%d;\n",
                            qLeft, item.getItemID());
                }
                System.out.println(productsQuery);
                conn.upload(productsQuery);
                clearAll();
                displayMessage(INFORMATION, "Transaction completed successfully.");
            } catch (SQLException sqlEx) {
                displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                products = null;
                products = FXCollections.observableArrayList();
                retrieveCustomerData();
                refreshCustomerTable();
                retrieveProductsData();
                initProductTableColumns(products);
            }
        } else displayMessage(ERROR, "Transaction could not have been completed.");
    }

    private void clearAll() {
        basketItems = null;
        basketItems = FXCollections.observableArrayList();
        System.out.println("Number of items in the basket: " + basketItems.size());
        basketListView.setItems(basketItems);
        basketListView.refresh();
        assignedCustomerLabel.setText("Assigned customer: ");
        customerID = -1;
        discountIDs.clear();
        discountIDs.add(-1);
        descriptionField.clear();
        updateLabels();

    }

    @FXML
    public void deleteItem_onClick() {
        basketItems.remove(selectedBasketItem);
        System.out.println(basketItems.size());
        basketListView.refresh();
        updateLabels();
    }

    @FXML
    public void AddToBasketMore_contextMenu_onClick() {
        String ans = TextFieldBox.display("Product quantity", "Quantity");
        if (isInteger(ans)) {
            if (Integer.parseInt(ans) <= selectedProduct.getQuantity()) {
                double price = selectedProduct.getPrice();
                String discount = "";
                for (Offer offer : offers) {
                    if (selectedProduct.getId() == offer.getProductID() && isValidOffer(offer.getExpiresDate())) {
                        price = (float) offer.getPrice();
                        discount = String.valueOf(offer.getDiscount());
                    }
                }

                basketItems.add(new BasketItem(selectedProduct.getId(),
                        selectedProduct.getName(),
                        selectedProduct.getSubname(),
                        price,
                        selectedProduct.getCategory().getUrl(),
                        discount,
                        Integer.parseInt(ans)));
                basketListView.setItems(basketItems);
                basketListView.setCellFactory(basketListView -> new BasketListViewCell());

                updateLabels();
            } else {
                displayError(ErrorCode.NOT_ENOUGH_ITEMS);
            }
        } else {
            displayError(ErrorCode.TEXT_FIELD_NON_NUMERIC);
        }
    }

}



