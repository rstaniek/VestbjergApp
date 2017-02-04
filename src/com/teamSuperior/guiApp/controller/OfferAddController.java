package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.service.Product;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

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
    @FXML
    public DatePicker datePicker_expires;

    private DBConnect conn;
    private ObservableList<Product> products;
    private final String datePattern = "yyyy-MM-dd";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        products = FXCollections.observableArrayList();
        conn = new DBConnect();
        label_productName.setText("");
        try{
            ResultSet rs = conn.getFromDataBase("SELECT * FROM products");
            while (rs.next()){
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        (float) rs.getDouble("price"));
                products.add(p);
            }
        } catch (SQLException sqlException) {
            Error.displayMessage(ERROR, "SQL connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
            @Override
            public String toString(LocalDate object) {
                if(object != null){
                    return dateTimeFormatter.format(object);
                } else{
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()){
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        };
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        if(item.isBefore(LocalDate.now())){
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        if(item.isAfter(LocalDate.now()) && item.isBefore(datePicker.getValue())){
                            setStyle("-fx-background-color: #67aaf4");
                        }
                    }
                };
            }
        };
        datePicker_expires.setConverter(converter);
        datePicker_expires.setDayCellFactory(dayCellFactory);
        datePicker_expires.setValue(LocalDate.now());
        datePicker_expires.setPromptText(datePattern.toLowerCase());
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
            if(Double.parseDouble(text_discount.getText()) < 100.0){
                if(findProduct(Integer.parseInt(text_product.getText())) != null){
                    try {
                        conn.upload(String.format("INSERT INTO offers (productIDs,discount,price,date,time,expiresDate,expiresTime) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','23:59:59')",
                                text_product.getText(),
                                text_discount.getText(),
                                text_price.getText(),
                                dtf_date.format(now),
                                dtf_time.format(now),
                                datePicker_expires.getValue().format(DateTimeFormatter.ofPattern(datePattern))));
                    } catch (SQLException sqlEx){
                        displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
                    }
                    btn_clear_onClick(null);
                    displayMessage(INFORMATION, "Offer added successfully");
                }
                else {
                    displayError(DATABASE_PRODUCTS_NOT_FOUND);
                }
            }else {
                displayError(OFFER_DISCOUNT_OUT_OF_BOUND);
            }
        }
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        text_discount.clear();
        text_price.clear();
        text_product.clear();
        datePicker_expires.getEditor().clear();
    }

    @FXML
    public void text_product_onKeyReleased(KeyEvent keyEvent) {
        if(isNumeric(text_product.getText())){
            if(findProduct(Integer.parseInt(text_product.getText())) != null){
                label_productName.setText(findProduct(Integer.parseInt(text_product.getText())).getName());
                text_price.setText(Float.toString(findProduct(Integer.parseInt(text_product.getText())).getPrice()).replace(",", "."));
            }
            else {
                displayError(DATABASE_PRODUCTS_NOT_FOUND);
            }
        }
    }

    @FXML
    public void text_discount_onKeyReleased(KeyEvent keyEvent) {
        if (text_discount.getText().isEmpty()) { //after the last character is deleted, it resets the price
            text_price.setText(Float.toString(findProduct(Integer.parseInt(text_product.getText())).getPrice()).replace(",", "."));
        }
        if(isNumeric(text_discount.getText())){
            float currentPrice = findProduct(Integer.parseInt(text_product.getText())).getPrice();
            float currentDiscount = Float.parseFloat(text_discount.getText());
            float newPrice = currentPrice/100 * (100-currentDiscount);
            if (newPrice >= 0){ //checks that the price never goes under 0 (discount doesn't go higher than 100)
                text_price.setText(String.format("%.2f", newPrice).replace(",", "."));
            }
            else { //should the price go under 0, error pops up, discount field is cleared and price is updated to original value
                text_discount.clear();
                text_price.setText(String.format("%.2f", currentPrice).replace(",", "."));
                displayError(OFFER_DISCOUNT_OUT_OF_BOUND);
            }
        }
        else { //same as in previous case, only different error pops up
            text_discount.clear();
            text_price.setText(String.format("%.2f", findProduct(Integer.parseInt(text_product.getText())).getPrice()).replace(",", "."));
            displayError(TEXT_FIELD_NON_NUMERIC);
        }

    }

    @FXML
    public void text_price_onKeyReleased(KeyEvent keyEvent) {
        if (text_price.getText().isEmpty()) text_discount.clear();
        if(isNumeric(text_price.getText())){
            float originalPrice = findProduct(Integer.parseInt(text_product.getText())).getPrice();
            float currentPrice = Float.parseFloat(text_price.getText());
            float newDiscount = 100-currentPrice/originalPrice*100;
            if (newDiscount <= 100 && newDiscount >= 0) text_discount.setText(String.format("%.2f",newDiscount).replace(",","."));
            else if (newDiscount <= 100) {
                text_discount.clear();
                text_price.setText(Float.toString(originalPrice).replace(",", "."));
                displayError(OFFER_DISCOUNT_LESS_THEN_ZERO);
            }
            else {
                text_discount.clear();
                text_price.setText(Float.toString(originalPrice).replace(",", "."));
                displayError(VALUE_LESS_THAN_ZERO);
            }
        }
        else {
            text_discount.clear();
            text_price.setText(Float.toString(findProduct(Integer.parseInt(text_product.getText())).getPrice()).replace(",", "."));
            displayError(TEXT_FIELD_NON_NUMERIC);
        }
    }

    private Product findProduct(int id) {
        Product foundProduct = null;
        for (Product p : products) { if (p.getId() == id) foundProduct = p; }
        return foundProduct;
    }
}
