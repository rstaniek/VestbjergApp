package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.teamSuperior.core.connection.DBConnect.validateField;

/**
 * Created by rajmu on 17.01.06.
 */
public class OfferAddController {
    @FXML
    public TextField text_product;
    @FXML
    public TextField text_price;
    @FXML
    public TextField text_discount;
    @FXML
    public Button btn_ok;
    @FXML
    public Button btn_clear;

    DBConnect conn;

    //TODO: doesn't work apparently ;___;
    @FXML
    public void btn_ok_onClick(ActionEvent actionEvent) {
        if (validateField(text_discount) &&
                validateField(text_price) &&
                validateField(text_product) /*&&
                isNumeric(text_discount.getText()) &&
                isNumeric(text_price.getText()) &&
                isNumeric(text_product.getText())*/) {
            System.out.println("Uploading new offer");
            conn = new DBConnect();
            conn.upload(String.format("INSERT INTO offers (productIDs,price,discount) VALUES ('%1$d','%2$d','%3$d')",
                    text_product.getText(),
                    text_discount.getText(),
                    text_price.getText()));
            btn_clear_onClick(null);
        }
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        text_discount.clear();
        text_price.clear();
        text_product.clear();
    }
}
