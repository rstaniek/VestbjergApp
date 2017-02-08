package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.Date;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Transactions view controller
 */
public class TransactionsViewController implements Initializable {
    private static final String[] TRANSACTION_CRITERIA = new String[]{"ID", "Discount", "Customer ID", "Employee ID", "product ID", "Date", "Description"};

    private static ConnectionController<Product, Integer> productConnectionController = new ConnectionController<>(Product.class);
    private static ConnectionController<Transaction, Integer> transactionConnectionController = new ConnectionController<>(Transaction.class);

    @FXML
    public TextField searchQueryField;
    @FXML
    public Button clearSearchButton;
    @FXML
    public CheckComboBox<String> searchCriteriaComboBox;
    @FXML
    public TableView<Transaction> transactionsTableView;
    @FXML
    public TableView<Product> productsTableView;
    @FXML
    private TableColumn<Transaction, Integer> idTransactionColumn;
    @FXML
    private TableColumn<Transaction, String> productIDsTransactionColumn;
    @FXML
    private TableColumn<Transaction, Integer> customerTransactionColumn;
    @FXML
    private TableColumn<Transaction, Integer> employeeTransactionColumn;
    @FXML
    private TableColumn<Transaction, Double> priceTransactionColumn;
    @FXML
    private TableColumn<Transaction, String> discountIDsTransactionColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionTransactionColumn;
    @FXML
    private TableColumn<Transaction, Date> dateTransactionColumn;
    @FXML
    private TableColumn<Product, Integer> idProductColumn;
    @FXML
    private TableColumn<Product, String> nameProductColumn;
    @FXML
    private TableColumn<Product, String> subnameProductColumn;
    @FXML
    private TableColumn<Product, String> barcodeProductColumn;
    @FXML
    private TableColumn<Product, String> categoryProductColumn;
    @FXML
    private TableColumn<Product, Double> priceProductColumn;
    @FXML
    private TableColumn<Product, String> locationProductColumn;
    @FXML
    private TableColumn<Product, Integer> quantityProductColumn;
    @FXML
    private TableColumn<Product, Integer> contractorProductColumn;

    private ObservableList<Transaction> transactions;
    private ObservableList<Transaction> searchResults;
    private ObservableList<Product> products;
    private Transaction selectedTransaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactions = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        products = FXCollections.observableArrayList();
        searchCriteriaComboBox.getItems().addAll(TRANSACTION_CRITERIA);
        retrieveData();
        initTransactionTableColumns(transactions);
        selectedTransaction = transactionsTableView.getFocusModel().getFocusedItem();
        initProductsTableColumns();
    }

    @FXML
    public void clickSearchClear() {
        searchQueryField.clear();
        initTransactionTableColumns(transactions);
    }

    @FXML
    public void text_search_query_onKeyReleased() {
        searchResults = null;
        searchResults = performSearch(searchQueryField.getText());
        initTransactionTableColumns(searchResults);
    }

    private void retrieveData() {
        transactions = FXCollections.observableArrayList(transactionConnectionController.getAll());
    }

    private void initTransactionTableColumns(ObservableList<Transaction> source) {
        idTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        descriptionTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        discountIDsTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("discountIDs_str"));
        employeeTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        customerTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        priceTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productIDsTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("productIDs_str"));

        transactionsTableView.setItems(source);
    }

    public void clickOffersTableView() {
        selectedTransaction = transactionsTableView.getFocusModel().getFocusedItem();
        if (selectedTransaction != null) {
            initProductsTableColumns();
        }
    }

    private void initProductsTableColumns() {
        Integer[] ids = Arrays.stream(selectedTransaction.getProductIDs().split(","))
                .mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        products = FXCollections.observableArrayList(productConnectionController.getByArray("id", ids));

        idProductColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subnameProductColumn.setCellValueFactory(new PropertyValueFactory<>("subname"));
        barcodeProductColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        categoryProductColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceProductColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        locationProductColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseLocation"));
        quantityProductColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        contractorProductColumn.setCellValueFactory(new PropertyValueFactory<>("contractor"));

        productsTableView.setItems(products);
    }

    private ObservableList<Transaction> performSearch(String query) {
        ObservableList<Transaction> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return transactions;
        }
        for (Transaction t : transactions) {
            ObservableList<String> clist;
            if (searchCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
                clist = searchCriteriaComboBox.getCheckModel().getCheckedItems();
            } else {
                clist = FXCollections.observableArrayList(TRANSACTION_CRITERIA);
            }
            for (String criteria : clist) {
                switch (criteria) {
                    case "ID":
                        if (String.valueOf(t.getId()).contains(query)) {
                            results.add(t);
                        }
                        break;
                    case "Discount":
                        if (t.getDiscountIDs().contains(query)) {
                            results.add(t);
                        }
                        break;
                    case "Employee ID":
                        if (String.valueOf(t.getEmployee()).contains(query)) {
                            results.add(t);
                        }
                        break;

                    case "Customer ID":
                        if (String.valueOf(t.getCustomer().getId()).contains(query)) {
                            results.add(t);
                        }
                        break;

                    case "Product ID":
                        if (t.getProductIDs().contains(query)) {
                            results.add(t);
                        }
                        break;
                    case "Date":
                        if (String.valueOf(t.getCreateDate()).contains(query)) {
                            results.add(t);
                        }
                        break;
                    case "Description":
                        if (t.getDescription().contains(query)) {
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
