package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.*;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.*;
import static javafx.scene.control.Alert.AlertType.*;

/**
 * Created by rajmu on 17.01.06.
 */
public class OfferAddController implements Initializable {
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
    @FXML
    public Label label_productName;

    private DBConnect conn;
    private Map<Integer, String> products;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        products = new HashMap<>();
        conn = new DBConnect();
        label_productName.setText("");
        ResultSet rs = conn.getFromDataBase("SELECT id,name FROM products");
        try{
            while (rs.next()){
                products.put(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException sqlException) {
            Error.displayMessage(ERROR, "SQL connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
    }

    @FXML
    public void btn_ok_onClick(ActionEvent actionEvent) {
        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if (validateField(text_discount) &&
                validateField(text_price) &&
                validateField(text_product) &&
                isNumeric(text_discount.getText()) &&
                isNumeric(text_price.getText()) &&
                isNumeric(text_product.getText())) {
            conn = new DBConnect();
            if(products.containsKey(Integer.parseInt(text_product.getText()))){
                conn.upload(String.format("INSERT INTO offers (productIDs,discount,price,date,time) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s')",
                        text_product.getText(),
                        text_discount.getText(),
                        text_price.getText(),
                        dtf_date.format(now),
                        dtf_time.format(now)));
                btn_clear_onClick(null);
                displayMessage(INFORMATION, "Offer added successfully");
            }
            else {
                displayError(DATABASE_PRODUCTS_NOT_FOUND);
            }
        }
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        text_discount.clear();
        text_price.clear();
        text_product.clear();
    }

    @FXML
    public void text_product_onKeyReleased(KeyEvent keyEvent) {
        if(isNumeric(text_product.getText())){
            if(products.containsKey(Integer.parseInt(text_product.getText()))){
                label_productName.setText(products.get(Integer.parseInt(text_product.getText())));
            }
            else{
                displayError(DATABASE_PRODUCTS_NOT_FOUND);
            }
        }
    }
}
