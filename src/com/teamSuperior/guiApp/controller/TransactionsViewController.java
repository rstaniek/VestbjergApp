package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.core.model.service.Transaction;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.Timestamp;
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
    private TableColumn<Transaction, Timestamp> dateTransactionColumn;

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
    private ObservableList<Product> products;
    private Transaction selectedTransaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WaitingBox waitingBox = new WaitingBox();
        waitingBox.setMessage("Fetching data.");
        transactions = FXCollections.observableArrayList();
        products = FXCollections.observableArrayList();
        searchCriteriaComboBox.getItems().addAll(TRANSACTION_CRITERIA);
        Task<Void> initData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(waitingBox::displayIndefinite);
                transactions = FXCollections.observableArrayList(transactionConnectionController.getAll());
                return null;
            }
        };

        initData.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                initTransactionTableColumns(transactions);
                waitingBox.closeWindow();
                selectedTransaction = transactionsTableView.getFocusModel().getFocusedItem();
                initProductsTableColumns();
            } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                waitingBox.closeWindow();
            }
        });

        Thread thread = new Thread(initData);
        thread.setDaemon(true);
        thread.start();

        searchQueryField.textProperty().addListener(
                (observable, oldValue, newValue) -> applyFilter(newValue)
        );
    }

    @FXML
    public void clickSearchClear() {
        searchQueryField.clear();
        initTransactionTableColumns(transactions);
    }

    private void initTransactionTableColumns(ObservableList<Transaction> source) {
        idTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        dateTransactionColumn.setCellFactory(col -> new TableCell<Transaction, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toLocalDateTime().format(Utils.dateFormatter(Utils.FormatterType.DATETIME)));
            }
        });
        descriptionTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        discountIDsTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("discountIDs"));
        employeeTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        customerTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        priceTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceTransactionColumn.setCellFactory(col -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : Utils.decimalFormat().format(item));
            }
        });
        transactionsTableView.setItems(source);
    }

    public void clickOffersTableView() {
        selectedTransaction = transactionsTableView.getFocusModel().getFocusedItem();
        if (selectedTransaction != null) {
            initProductsTableColumns();
        }
    }

    private void initProductsTableColumns() {
        WaitingBox waitingBox = new WaitingBox("Fetching products data.");
        Task<Void> initProducts = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Integer[] ids = Arrays.stream(selectedTransaction.getProductIDs().split(","))
                        .mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
                products = FXCollections.observableArrayList(productConnectionController.listByArray("id", ids));
                return null;
            }
        };

        initProducts.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                idProductColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                subnameProductColumn.setCellValueFactory(new PropertyValueFactory<>("subname"));
                barcodeProductColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                categoryProductColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
                priceProductColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                priceProductColumn.setCellFactory(col -> new TableCell<Product, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : Utils.decimalFormat().format(item));
                    }
                });
                locationProductColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseLocation"));
                quantityProductColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                contractorProductColumn.setCellValueFactory(new PropertyValueFactory<>("contractor"));

                productsTableView.setItems(products);
                waitingBox.closeWindow();
            } else if (newValue.equals(Worker.State.CANCELLED) || newValue.equals(Worker.State.FAILED)) {
                waitingBox.closeWindow();
            }
        });

        Thread th = new Thread(initProducts);
        th.setDaemon(true);
        th.start();
    }

    private void applyFilter(String query) {
        ObservableList<String> clist;
        if (searchCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
            clist = searchCriteriaComboBox.getCheckModel().getCheckedItems();
        } else {
            clist = FXCollections.observableArrayList(TRANSACTION_CRITERIA);
        }
        transactionsTableView.setItems(transactions.filtered(transaction -> {
            if (query == null || query.isEmpty()) {
                return true;
            } else {
                String lowerCaseFilter = query.toLowerCase();

                for (String criteria : clist) {
                    switch (criteria) {
                        case "ID":
                            if (String.valueOf(transaction.getId()).contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Discount":
                            if (transaction.getDiscountIDs().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Employee":
                            if (transaction.getEmployee().toString().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;

                        case "Customer":
                            if (transaction.getCustomer().toString().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;

                        case "Product ID":
                            if (transaction.getProductIDs().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Date":
                            if (String.valueOf(transaction.getCreateDate()).contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Description":
                            if (transaction.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        }));
    }
}
