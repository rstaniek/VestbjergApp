package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.connection.IDataAccessObject;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Product connectionController
 */
public class ProductsController implements IDataAccessObject<Product, Integer>, Initializable {

    static final int MAX_CAP = 250;
    private static final int CAP_THRESHOLD = 15;
    private static final String[] PRODUCTS_CRITERIA = new String[]{"ID", "Barcode", "Name", "Subname", "Category", "Location", "Price", "Contractor"};

    private static ConnectionController<Product, Integer> connectionController = new ConnectionController<>(Product.class);

    @FXML
    public JFXTextField amountToRequestField;
    @FXML
    public JFXTextField searchQueryField;

    @FXML
    public JFXButton clearSearchButton;
    @FXML
    public JFXButton requestResupplyButton;
    @FXML
    public JFXButton showLowQuantityButton;

    @FXML
    public Label nameLabel;
    @FXML
    public Label subnameLabel;

    @FXML
    public Label categoryLabel;

    @FXML
    public Label quantityLabel;

    @FXML
    public Label priceLabel;
    @FXML
    public PieChart storageCapPieChart;
    @FXML
    public CheckComboBox<String> searchCriteriaCheckComboBox;
    @FXML
    public TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> subnameColumn;
    @FXML
    private TableColumn<Product, String> barcodeColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Float> priceColumn;
    @FXML
    private TableColumn<Product, String> locationColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Integer> contractorColumn;

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private Product selectedProduct;
    private boolean showsAll;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchCriteriaCheckComboBox.getItems().addAll(PRODUCTS_CRITERIA);
        showsAll = true;

        setColumns();
        updateTable();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectProduct(newValue)
        );

        tableView.getSelectionModel().selectFirst();

        searchQueryField.textProperty().addListener(
                (observable, oldValue, newValue) -> applyFilter(newValue)
        );

        if (UserController.getUser().getAccessLevel() < 2) {
            requestResupplyButton.setDisable(true);
            amountToRequestField.setDisable(true);
        }

        runWarehouseCheck();
    }

    private ObservableList<Product> getLowQuantity() {
        return products.filtered(product -> product.getQuantity() < CAP_THRESHOLD);
    }

    private void retrieveData() {
        products = FXCollections.observableArrayList(getAll());
    }

    private void updateTable() {
        retrieveData();
        tableView.setItems(products);
    }

    private void setColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subnameColumn.setCellValueFactory(new PropertyValueFactory<>("subname"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("warehouseLocation"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        contractorColumn.setCellValueFactory(new PropertyValueFactory<>("contractor"));
    }

    private void selectProduct(Product selectedProduct) {
        if (selectedProduct != null) {
            this.selectedProduct = selectedProduct;
            System.out.println(selectedProduct.toString());
            runItemCheck();
            showProductDetails();
        } else {
            tableView.getSelectionModel().selectFirst();
        }
    }

    private void showProductDetails() {
        System.out.println(selectedProduct.toString());

        ObservableList<PieChart.Data> quantityChartData = FXCollections.observableArrayList(
                new PieChart.Data("Number of items", calculateCapRatio(selectedProduct, MAX_CAP)),
                new PieChart.Data("Space available", 100 - calculateCapRatio(selectedProduct, MAX_CAP)));

        storageCapPieChart.setData(quantityChartData);

        nameLabel.setText(selectedProduct.getName());
        subnameLabel.setText(selectedProduct.getSubname());
        categoryLabel.setText(selectedProduct.getCategory().toString());
        quantityLabel.setText(String.format("Q: %1$d", selectedProduct.getQuantity()));
        priceLabel.setText(String.format("Price: %1$.2fkr.", selectedProduct.getPrice()));
        quantityLabel.setTextFill((selectedProduct.getQuantity() < CAP_THRESHOLD) ? Color.RED : Color.BLACK);
    }

    private void runItemCheck() {
        if (selectedProduct.getQuantity() < CAP_THRESHOLD) {
            if (showsAll) Error.displayError(ErrorCode.WAREHOUSE_LOW_AMOUNT_OF_PRODUCT);
        }
    }

    private void runWarehouseCheck() {
        int numberOfWarnings = getLowQuantity().size();
        if (numberOfWarnings != 0) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Empty storage detected!");
            a.setContentText(String.format("There were %1$d almost empty storages found during the checkup. Do you want to intervene?", numberOfWarnings));
            Button yesButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
            yesButton.setText("Yes");
            Button noButton = (Button) a.getDialogPane().lookupButton(ButtonType.CANCEL);
            noButton.setText("No");
            Optional<ButtonType> okResponse = a.showAndWait();
            if (okResponse.isPresent() && ButtonType.OK.equals(okResponse.get())) {
                showsAll = false;
                clickShowLowQuantity();
            }
        }
    }

    private float calculateCapRatio(Product p, int maxCapacity) {
        return ((float) p.getQuantity() / (float) maxCapacity) * (float) 100;
    }

    @FXML
    public void clickRequestResupply() {
        if (Utils.isNumeric(amountToRequestField.getText())) {
            WaitingBox.display("Creating request", 5000);
            int itemsTotal = selectedProduct.getQuantity() + Integer.parseInt(amountToRequestField.getText());
            if (itemsTotal > MAX_CAP) {
                itemsTotal = MAX_CAP;
                Error.displayMessage(
                        Alert.AlertType.WARNING, "Warehouse overfill alert!",
                        String.format("Ordering %1$s new items would cause overfill. Ordering %2$d items instead.",
                                amountToRequestField.getText(), MAX_CAP - selectedProduct.getQuantity())
                );
            }
            selectedProduct.setQuantity(itemsTotal);
            update(selectedProduct);
            amountToRequestField.clear();
            updateTable();
        } else Error.displayError(ErrorCode.TEXT_FIELD_NON_NUMERIC);
    }

    @FXML
    public void clickShowLowQuantity() {
        if (showsAll) {
            showsAll = false;
            showLowQuantityButton.setText("Show All products");
        } else {
            showsAll = true;
            showLowQuantityButton.setText("Show only low quantity");
        }
        applyFilter(searchQueryField.getText());
    }

    @FXML
    public void clickSearchClear() {
        searchQueryField.clear();
        if (showsAll) {
            tableView.setItems(products);
        } else {
            tableView.setItems(getLowQuantity());
        }
    }

    private void applyFilter(String query) {
        ObservableList<String> criteriaList;
        if (searchCriteriaCheckComboBox.getCheckModel().getCheckedItems().size() != 0) {
            criteriaList = searchCriteriaCheckComboBox.getCheckModel().getCheckedItems();
        } else {
            criteriaList = FXCollections.observableArrayList(PRODUCTS_CRITERIA);
        }
        tableView.setItems(products.filtered(product -> {

            if (!showsAll) {
                if (product.getQuantity() >= CAP_THRESHOLD) {
                    return false;
                }
            }

            if (query == null || query.isEmpty()) {
                return true;
            } else {
                String lowerCaseFilter = query.toLowerCase();

                for (String criteria : criteriaList) {
                    switch (criteria) {
                        case "ID":
                            if (String.valueOf(product.getId()).toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Barcode":
                            if (product.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Name":
                            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Subname":
                            if (product.getSubname().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Category":
                            if (String.valueOf(product.getCategory()).toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Location":
                            if (product.getWarehouseLocation().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Minimum price":
                            if (product.getPrice() >= Float.parseFloat(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Maximum price":
                            if (product.getPrice() <= Float.parseFloat(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Contractor ID":
                            if (String.valueOf(product.getContractor()).toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return false;
        }));
    }

    @Override
    public void persist(Product product) {
        connectionController.persist(product);
    }

    @Override
    public Product getById(Integer id) {
        return connectionController.getById(id);
    }

    @Override
    public List<Product> getAll() {
        return connectionController.getAll();
    }

    @Override
    public void update(Product product) {
        connectionController.update(product);
    }

    @Override
    public void delete(Product product) {
        connectionController.delete(product);
    }

    @Override
    public void deleteAll() {
        connectionController.deleteAll();
    }

}
