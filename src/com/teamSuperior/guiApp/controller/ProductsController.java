package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Domestos on 16.12.12.
 */
public class ProductsController implements Initializable {
    @FXML
    public TableView tableView_products;

    private DBConnect conn;
    private ObservableList<Product> products;
    private Employee loggedInUser;

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

        //init columns ad stuff
        retrieveData();
        updateColumns();
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

    private void updateColumns() {
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
        barcodeCol.setMinWidth(150);
        barcodeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));

        categoryCol = new TableColumn<>("Category");
        categoryCol.setMinWidth(80);
        categoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));

        priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        locationCol = new TableColumn<>("Warehouse location");
        locationCol.setMinWidth(150);
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
}
