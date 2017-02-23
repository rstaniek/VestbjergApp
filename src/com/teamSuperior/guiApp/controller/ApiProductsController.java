package com.teamSuperior.guiApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.API.Product;
import com.teamSuperior.core.connection.HttpRequestHandler;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 20-Feb-17.
 */
public class ApiProductsController implements Initializable {
    @FXML
    public JFXButton btn_get;
    @FXML
    public JFXTextField text_url;
    @FXML
    public TableView tableView_products;

    private String jsonData;
    private ObservableList<Product> products;

    private TableColumn<String, Product> nameCol;
    private TableColumn<Float, Product> priceCol;
    private TableColumn<Integer, Product> qCol;
    private TableColumn<Integer, Product> idCol;

    @FXML
    public void handleGetData(ActionEvent actionEvent) {
        products = FXCollections.observableArrayList();
        try {
            String[] response = HttpRequestHandler.sendGet(text_url.getText());
            jsonData = response[1];
            Error.displayMessage(Alert.AlertType.INFORMATION, "Response code: " + response[0], response[1]);
            mapData();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initTableData(products);
        }
    }

    private void initTableData(ObservableList<Product> source) {
        tableView_products.getColumns().removeAll(idCol, nameCol, priceCol, qCol);

        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(120);
        nameCol.setCellValueFactory(new PropertyValueFactory<String, Product>("name"));

        priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<Float, Product>("price"));

        qCol = new TableColumn<>("Quantity");
        qCol.setMinWidth(60);
        qCol.setCellValueFactory(new PropertyValueFactory<Integer, Product>("quantity"));

        idCol = new TableColumn<>("ID");
        idCol.setMinWidth(20);
        idCol.setCellValueFactory(new PropertyValueFactory<Integer, Product>("Id"));

        tableView_products.setItems(source);
        tableView_products.getColumns().addAll(idCol, nameCol, priceCol, qCol);
    }

    private void mapData() {
        jsonData = jsonData.substring(1, jsonData.length() - 2);
        String[] splitJson = jsonData.split("}");
        ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();

        int index = 0;
        for (String classData : splitJson) {
            if (index != 0) {
                classData = classData.substring(1, classData.length() - 1);
            }
            index++;
            classData += "}";
            System.err.println(classData);
            products.add(gson.fromJson(classData, Product.class));
            System.out.println("Objects in array: " + products.size());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        products = FXCollections.observableArrayList();
        ArrayList<String> jsonStrings = new ArrayList<>();
    }
}
