package com.teamSuperior.guiApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.API.Product;
import com.teamSuperior.core.connection.HttpRequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 20-Feb-17.
 */
public class ApiProductsController {
    @FXML
    public JFXButton btn_get;
    @FXML
    public JFXTextField text_url;

    private String jsonData;
    private ArrayList<Product> products;

    @FXML
    public void handleGetData(ActionEvent actionEvent) {
        try {
            String[] response = HttpRequestHandler.sendGet(text_url.getText());
            jsonData = response[1];
            mapData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapData() {
        ObjectMapper mapper = new ObjectMapper();
        if (!jsonData.isEmpty()) {
            try {
                products.add(mapper.readValue(jsonData, Product.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Objects in array: " + products.size());
        }
    }
}
