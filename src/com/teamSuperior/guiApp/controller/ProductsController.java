package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.exception.ConnectionException;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import com.teamSuperior.core.Utils;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Domestos on 16.12.12.
 */
public class ProductsController implements Initializable {

    @FXML
    public TextField text_amountToRequest;
    @FXML
    public Button btn_requestResupply;
    @FXML
    public TableView tableView_products;
    @FXML
    public PieChart chart_storageCap;
    @FXML
    public Button btn_showLowQuantity;
    @FXML
    public Label label_name;
    @FXML
    public Label label_subname;
    @FXML
    public Label label_category;
    @FXML
    public Label label_quantity;
    @FXML
    public Label label_price;
    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;

    private static final int maxCap = 250;
    private static final int capTreshold = 15;

    private DBConnect conn;
    private ObservableList<Product> products;
    private Employee loggedInUser;
    private Product selectedProduct;
    private ObservableList<Product> almostEmptyStorages;
    private boolean showsAll, initWarehouseCheckDone;
    private ObservableList<Product> searchResults;

    private ObservableList<PieChart.Data> quantityChartData;

    //table columns
    private TableColumn<Product, Integer> idCol;
    private TableColumn<Product, String> nameCol;
    private TableColumn<Product, String> subnameCol;
    private TableColumn<Product, String> barcodeCol;
    private TableColumn<Product, String> categoryCol;
    private TableColumn<Product, Float> priceCol;
    private TableColumn<Product, String> locationCol;
    private TableColumn<Product, Integer> quantityCol;
    private TableColumn<Product, Integer> contractorIdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = new DBConnect();
        products = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("ID", "Barcode", "Name", "Subname", "Category", "Location", "Price", "Contractor ID");
        loggedInUser = UserController.getUser();
        showsAll = true;
        initWarehouseCheckDone = false;

        //init columns ad stuff
        retrieveData();
        if (showsAll) {
            updateColumns(products);
        } else {
            updateColumns(almostEmptyStorages);
        }

        //-------------
        tableView_products.getSelectionModel().selectFirst();
        selectedProduct = (Product) tableView_products.getSelectionModel().getSelectedItem();

        if (loggedInUser.getAccessLevel() < 2) {
            btn_requestResupply.setDisable(true);
            text_amountToRequest.setDisable(true);
        }

        updateStats();
        runWarehouseCheck(true);
        initWarehouseCheckDone = true;
    }

    private void runWarehouseCheck(boolean runForAllItems) {
        Preferences reg = Preferences.userRoot();
        almostEmptyStorages = null;
        almostEmptyStorages = FXCollections.observableArrayList();
        if (runForAllItems) {
            int numberOfWarnings = 0;
            for (Product p : products) {
                if (p.getQuantity() < capTreshold) {
                    numberOfWarnings += 1;
                    almostEmptyStorages.add(p);
                }
            }
            if (numberOfWarnings != 0 && !initWarehouseCheckDone) {
                //TODO: alert box looping, needs fix
                boolean result = ConfirmBox.display("Empty storages detected",
                        String.format("There were %1$d almost empty storages found during the checkup. Do you want to intervene?", numberOfWarnings));
                if (result) {
                    //Error.displayMessage(ErrorCode.NOT_IMPLEMENTED);
                    showsAll = false;
                    updateTable(showsAll);
                    btn_showLowQuantity.setText("Show All products");
                }
            }
        } else {
            if (selectedProduct.getQuantity() < capTreshold) {
                if (showsAll) Error.displayError(ErrorCode.WAREHOUSE_LOW_AMOUNT_OF_PRODUCT);
                //TODO: implement this shit
                /*if(reg.getBoolean("SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS", false)){
                    AlertBox.display("WARNING","Low amount of product, resupply is advised.","SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS");
                }*/
            }
        }
    }

    private float calculateCapRatio(Product p, int maxCapacity) {
        return ((float) p.getQuantity() / (float) maxCapacity) * (float) 100;
    }

    private void retrieveData() {
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

    private void updateColumns(ObservableList<Product> source) {
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

        tableView_products.setItems(source);
        tableView_products.getColumns().addAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
    }

    private void updateStats() {
        chart_storageCap.getData().clear();
        quantityChartData = FXCollections.observableArrayList();
        quantityChartData.addAll(new PieChart.Data("Amount of items", calculateCapRatio(selectedProduct, maxCap)),
                new PieChart.Data("Space available", 100 - calculateCapRatio(selectedProduct, maxCap)));
        chart_storageCap.getData().addAll(quantityChartData);

        //labels
        label_name.setText(selectedProduct.getName());
        label_subname.setText(selectedProduct.getSubname());
        label_category.setText(selectedProduct.getCategory());
        label_quantity.setText(String.format("Q: %1$d", selectedProduct.getQuantity()));
        label_price.setText(String.format("Price: %1$.2fkr.", selectedProduct.getPrice()));
        if (selectedProduct.getQuantity() < capTreshold) label_quantity.setTextFill(Color.RED);
        else label_quantity.setTextFill(Color.BLACK);
    }

    private void updateTable(boolean showAll) {
        products.removeAll();
        products = null;
        products = FXCollections.observableArrayList();
        tableView_products.getColumns().removeAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
        retrieveData();
        runWarehouseCheck(true);
        if (showsAll) {
            updateColumns(products);
        } else {
            updateColumns(almostEmptyStorages);
        }
        updateStats();
    }

    @FXML
    public void tableView_products_onClick(MouseEvent mouseEvent) {
        selectedProduct = (Product) tableView_products.getSelectionModel().getSelectedItem();
        System.out.println(selectedProduct.toString());
        updateStats();
        runWarehouseCheck(false);
    }

    @FXML
    public void btn_requestResupply_onClick(ActionEvent actionEvent) {
        if (Utils.isNumeric(text_amountToRequest.getText())) {
            WaitingBox.display("Creating request", 6000);
            int itemsTotal = selectedProduct.getQuantity() + Integer.parseInt(text_amountToRequest.getText());
            if (itemsTotal > maxCap) {
                itemsTotal = maxCap;
                Error.displayMessage(Alert.AlertType.WARNING, "Warehouse overfill alert!", String.format("Ordering %1$s new items would cause overfill. Ordering %2$d items instead.", text_amountToRequest.getText(), maxCap - selectedProduct.getQuantity()));
            }
            conn = new DBConnect();
            try{
                conn.upload(String.format("UPDATE products SET quantity='%1$d' WHERE id='%2$d'", itemsTotal, selectedProduct.getId()));
            } catch (SQLException sqlEx){
                Error.displayMessage(Alert.AlertType.ERROR, "SQL Exception", sqlEx.getMessage());
            } finally {
                updateTable(true);
            }
            text_amountToRequest.clear();
        } else Error.displayError(ErrorCode.TEXT_FIELD_NON_NUMERIC);
    }

    @FXML
    public void btn_showLowQuantity_onClick(ActionEvent actionEvent) {
        if (showsAll) {
            showsAll = false;
            updateTable(showsAll);
            btn_showLowQuantity.setText("Show All products");
        } else {
            showsAll = true;
            updateTable(showsAll);
            btn_showLowQuantity.setText("Show only low quantity");
        }
    }

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        if (showsAll) {
            updateColumns(products);
        } else {
            updateColumns(almostEmptyStorages);
        }
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_products.getColumns().removeAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
        updateColumns(searchResults);
    }

    private ObservableList<Product> performSearch(String query) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        ObservableList<Product> source = FXCollections.observableArrayList();
        if (showsAll) source = products;
        else source = almostEmptyStorages;
        if (query.isEmpty()) {
            return source;
        }
        for (Product product : source) {
            for (String criteria : checkComboBox_search_criteria.getCheckModel().getCheckedItems()) {
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
}
