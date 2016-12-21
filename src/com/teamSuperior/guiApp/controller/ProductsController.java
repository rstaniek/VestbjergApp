package com.teamSuperior.guiApp.controller;

import com.mysql.jdbc.StringUtils;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import com.teamSuperior.guiApp.Utils;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private int maxCap = 250;

    private DBConnect conn;
    private ObservableList<Product> products;
    private Employee loggedInUser;
    private Product selectedProduct;
    private ObservableList<Product> almostEmptyStorages;
    private boolean showsAll, initWarehouseCheckDone;

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
        loggedInUser = LogInPopupController.getUser();
        showsAll = true;
        initWarehouseCheckDone = false;

        //init columns ad stuff
        retrieveData();
        updateColumns(showsAll);
        //-------------
        tableView_products.getSelectionModel().selectFirst();
        selectedProduct = (Product) tableView_products.getSelectionModel().getSelectedItem();
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
                if (p.getQuantity() < 15) {
                    numberOfWarnings += 1;
                    almostEmptyStorages.add(p);
                }
            }
            if (numberOfWarnings != 0 && !initWarehouseCheckDone) {
                //TODO: alert box looping, needs fix
                boolean result = ConfirmBox.display("Empty storages detected",
                        String.format("There were %1$d almost empty storages found during the checkup. Do you want to intervene?", numberOfWarnings));
                if (result) {
                    //Error.displayError(ErrorCode.NOT_IMPLEMENTED);
                    showsAll = false;
                    updateTable(showsAll);
                    btn_showLowQuantity.setText("Show All products");
                }
            }
        } else {
            if (selectedProduct.getQuantity() < 15) {
                if(showsAll) Error.displayError(ErrorCode.WAREHOUSE_LOW_AMOUNT_OF_PRODUCT);
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
        ResultSet rs = conn.getFromDataBase("SELECT * FROM products");
        try {
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
            AlertBox.display("SQL Exception", sqlex.getMessage());
        } catch (Exception ex) {
            AlertBox.display("Unexpected Exception", ex.getMessage());
        }
    }

    private void updateColumns(boolean showAll) {
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

        if(showAll){
            tableView_products.setItems(products);
        }else{
            tableView_products.setItems(almostEmptyStorages);
        }

        tableView_products.getColumns().addAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
    }

    private void updateStats() {
        chart_storageCap.getData().clear();
        quantityChartData = FXCollections.observableArrayList();
        quantityChartData.addAll(new PieChart.Data("Amount of items", calculateCapRatio(selectedProduct, maxCap)),
                new PieChart.Data("Space available", 100 - calculateCapRatio(selectedProduct, maxCap)));
        chart_storageCap.getData().addAll(quantityChartData);
    }

    private void updateTable(boolean showAll) {
        products.removeAll();
        products = null;
        products = FXCollections.observableArrayList();
        tableView_products.getColumns().removeAll(idCol, nameCol, subnameCol, barcodeCol, categoryCol, priceCol, locationCol, quantityCol, contractorIdCol);
        retrieveData();
        runWarehouseCheck(true);
        updateColumns(showAll);
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
        if(Utils.isNumeric(text_amountToRequest.getText())){
            WaitingBox.display("Creating request", 6000);
            int itemsTotal = selectedProduct.getQuantity() + Integer.parseInt(text_amountToRequest.getText());
            if(itemsTotal > maxCap){
                itemsTotal = maxCap;
                AlertBox.display("Capacity error", String.format("Ordering %1$s new items would cause overfill. Ordering %2$d items instead.", text_amountToRequest.getText(), maxCap - selectedProduct.getQuantity()));
            }
            conn = new DBConnect();
            conn.upload(String.format("UPDATE products SET quantity='%1$d' WHERE id='%2$d'", itemsTotal, selectedProduct.getId()));
            updateTable(true);
            text_amountToRequest.clear();
        }else Error.displayError(ErrorCode.TEXT_FIELD_NON_NUMERIC);
    }

    @FXML
    public void btn_showLowQuantity_onClick(ActionEvent actionEvent) {
        if(showsAll){
            showsAll = false;
            updateTable(showsAll);
            btn_showLowQuantity.setText("Show All products");
        }else{
            showsAll = true;
            updateTable(showsAll);
            btn_showLowQuantity.setText("Show only low quantity");
        }
    }
}
