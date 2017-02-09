package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.BasketItem;
import com.teamSuperior.core.model.Discount;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import com.teamSuperior.guiApp.GUI.TextFieldBox;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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
import java.text.NumberFormat;
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

    private static ConnectionController<Product, Integer> productConnectionController = new ConnectionController<>(Product.class);
    private static ConnectionController<Customer, Integer> customerConnectionController = new ConnectionController<>(Customer.class);
    private static ConnectionController<Discount, Integer> discountConnectionController = new ConnectionController<>(Discount.class);
    private static ConnectionController<Offer, Integer> offerConnectionController = new ConnectionController<>(Offer.class);
    private static ConnectionController<Transaction, Integer> transactionConnectionController = new ConnectionController<>(Transaction.class);
    private static ConnectionController<Employee, Integer> employeeConnectionController = new ConnectionController<>(Employee.class);

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
    private TableColumn<Product, Integer> idProductColumn;
    @FXML
    private TableColumn<Product, String> nameProductColumn;
    @FXML
    private TableColumn<Product, String> subnameProductColumn;
    @FXML
    private TableColumn<Product, String> categoryProductColumn;
    @FXML
    private TableColumn<Product, Double> priceProductColumn;
    @FXML
    private TableColumn<Product, String> locationProductColumn;
    @FXML
    private TableColumn<Product, Integer> quantityProductColumn;
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
    private Customer customer;
    private double discountThreshold;
    private ObservableList<Product> products;
    private ObservableList<Customer> customers;
    private ObservableList<Product> searchProductsResults;
    private ObservableList<Customer> searchCustomersResults;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private ObservableList<BasketItem> basketItems;
    private BasketItem selectedBasketItem;
    private ObservableList<Offer> offers;
    private ObservableList<Discount> discounts;
    private String currency;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedUser = UserController.getUser();
        basketItems = FXCollections.observableArrayList();
        WaitingBox initWaitingBox = new WaitingBox();
        initWaitingBox.setMessage("Retrieving data from the database.");

        products = FXCollections.observableArrayList();
        searchProductsResults = FXCollections.observableArrayList();
        searchProductsCriteriaComboBox.getItems().addAll(PRODUCTS_CRITERIA);

        Task<Void> retrieve = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(initWaitingBox::displayIndefinite);
                products = FXCollections.observableArrayList(productConnectionController.getAll());
                customers = FXCollections.observableArrayList(customerConnectionController.getAll());
                offers = FXCollections.observableArrayList(offerConnectionController.getAll());
                discounts = FXCollections.observableArrayList(discountConnectionController.getAll());
                return null;
            }
        };

        retrieve.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                initWaitingBox.closeWindow();
                System.out.println("[Retrieve]: Thread finished");

                //products
                initProductTableColumns(products);
                productsTableView.getSelectionModel().selectFirst();
                selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

                //customers
                initCustomerTableColumns(customers);
                customersTableView.getSelectionModel().selectFirst();
                selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

                for (Discount d : discounts) {
                    if (d.getId() == 5) {
                        discountThreshold = d.getValue();
                    }
                }
            } else if (newValue == Worker.State.FAILED || newValue == Worker.State.CANCELLED) {
                initWaitingBox.closeWindow();
            }
        });

        /*retrieveProductsData();
        initProductTableColumns(products);
        productsTableView.getSelectionModel().selectFirst();
        selectedProduct = productsTableView.getSelectionModel().getSelectedItem();*/


        customers = FXCollections.observableArrayList();
        searchCustomersResults = FXCollections.observableArrayList();
        searchCustomersCriteriaComboBox.getItems().addAll(CUSTOMERS_CRITERIA);

        /*retrieveCustomerData();
        initCustomerTableColumns(customers);
        customersTableView.getSelectionModel().selectFirst();
        selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();*/

        Thread th1 = new Thread(retrieve);
        th1.setDaemon(true);
        th1.start();

        numOfItemsLabel.setText(String.format("Number of items in the basket: %d", basketItems.size()));
        overallPriceLabel.setText("Total: kr. 0");

        customer = null;
        discountIDs = new ArrayList<>();
        discountIDs.add(-1);

        currencyComboBox.setItems(FXCollections.observableArrayList("DKK", "USD", "EUR"));
        currency = CURRENCIES[0];
        currencyComboBox.getSelectionModel().select(0);

        basketListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedBasketItem = newValue);

        selfPickupCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateLabels());

        craftsmanCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateLabels());

        currencyComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            currency = CURRENCIES[newValue.intValue()];
            updateLabels();
        });
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
    public void handleSearchProductQuery() {
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

    private void initProductTableColumns(ObservableList<Product> source) {
        categoryProductColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        idProductColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationProductColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseLocation"));
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceProductColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceProductColumn.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : Utils.decimalFormat().format(item));
            }
        });
        quantityProductColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        subnameProductColumn.setCellValueFactory(new PropertyValueFactory<>("subname"));

        productsTableView.setItems(source);
    }

    @FXML
    public void clickAddToBasket() {
        if (selectedProduct.getQuantity() > 0) {
            double price = selectedProduct.getPrice();
            String discount = "";
            for (Offer offer : offers) {
                if (selectedProduct.getId() == offer.getProduct().getId() && isValidOffer(offer.getExpiresDate())) {
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
        noDiscountPrice = tmp;
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
                finalPriceOnLabels = finalPrice * Double.parseDouble(dkkusd);
                noDiscountPriceOnLabels = noDiscountPrice * Double.parseDouble(dkkusd);
                break;
            case "€":
                finalPriceOnLabels = finalPrice * Double.parseDouble(dkkeur);
                noDiscountPriceOnLabels = noDiscountPrice * Double.parseDouble(dkkeur);
                break;
        }
        System.out.println("finalPrice: " + finalPrice);
        System.out.println("noDiscountPrice: " + noDiscountPrice);
    }

    private void calculateDiscount() {
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
        finalPrice = noDiscountPrice * ((100 - discount) / 100);
        finalPriceOnLabels = noDiscountPriceOnLabels * ((100 - discount) / 100);
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
        customer = selectedCustomer;
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
        WaitingBox wb = new WaitingBox("Retrieving customer data");
        Task<Void> retrieve = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                customers = FXCollections.observableArrayList(customerConnectionController.getAll());
                return null;
            }
        };

        retrieve.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                wb.closeWindow();
                initCustomerTableColumns(customers);
            } else if (newValue == Worker.State.FAILED || newValue == Worker.State.CANCELLED) {
                wb.closeWindow();
            }
        });

        Thread th = new Thread(retrieve);
        th.setDaemon(true);
        th.start();
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
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to complete the transaction?");
        a.setContentText("You will not be able to revert this action!");
        Optional<ButtonType> okResponse = a.showAndWait();
        if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get()) && verifyFields()) {
            try {
                transactionConnectionController.persist(new Transaction(
                        loggedUser,
                        customer,
                        Utils.arrayToString(getProductIDs()),
                        Utils.arrayToString(discountIDs),
                        descriptionField.getText(),
                        finalPrice
                ));
                if (customer != null) {
                    selectedCustomer.setSalesMade(selectedCustomer.getSalesMade() + 1);
                    selectedCustomer.setTotalSpent(selectedCustomer.getTotalSpent() + finalPrice);
                    WaitingBox waitingBoxUpdateCustomer = new WaitingBox("Updating customer data");
                    Task<Void> taskUpdateCustomer = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            customerConnectionController.update(selectedCustomer);
                            return null;
                        }
                    };

                    taskUpdateCustomer.stateProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.equals(Worker.State.SUCCEEDED) || newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                            waitingBoxUpdateCustomer.closeWindow();
                        }
                    });

                    Thread th = new Thread(taskUpdateCustomer);
                    th.setDaemon(true);
                    th.start();
                }
                loggedUser.setNumberOfSales(loggedUser.getNumberOfSales() + 1);
                loggedUser.setTotalRevenue(loggedUser.getTotalRevenue() + finalPrice);
                WaitingBox wb1 = new WaitingBox("Finalizing the transaction");
                Task<Void> updateFinal = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        employeeConnectionController.update(loggedUser);
                        for (BasketItem item : basketItems) {
                            for (Product p : products) {
                                if (p.getId() == item.getItemID()) {
                                    p.setQuantity(p.getQuantity() - item.getQuantity());
                                    productConnectionController.update(p);
                                }
                            }
                        }
                        return null;
                    }
                };

                updateFinal.stateProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.equals(Worker.State.SUCCEEDED)) {
                        wb1.closeWindow();
                        displayMessage(INFORMATION, "Transaction completed successfully.");
                    } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                        wb1.closeWindow();
                    }
                });

                Thread thread = new Thread(updateFinal);
                thread.setDaemon(true);
                thread.start();

                clearAll();
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                products = null;
                products = FXCollections.observableArrayList();

                WaitingBox wb = new WaitingBox("Retrieving products data");
                Task<Void> retrieve = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        products = FXCollections.observableArrayList(productConnectionController.getAll());
                        return null;
                    }
                };

                retrieve.stateProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        initProductTableColumns(products);
                        wb.closeWindow();
                    } else if (newValue == Worker.State.FAILED || newValue == Worker.State.CANCELLED) {
                        wb.closeWindow();
                    }
                });

                Thread thread = new Thread(retrieve);
                thread.setDaemon(true);
                thread.start();
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
        customer = null;
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
                    if (selectedProduct.getId() == offer.getProduct().getId() && isValidOffer(offer.getExpiresDate())) {
                        price = offer.getPrice();
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



