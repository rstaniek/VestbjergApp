package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static com.teamSuperior.core.connection.DBConnect.*;
import static com.teamSuperior.guiApp.GUI.Error.*;
import static javafx.scene.control.Alert.*;
import static javafx.scene.control.Alert.AlertType.*;

/**
 * Created by rajmu on 17.01.18.
 */
public class CustomerAddController {
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_surname;
    @FXML
    public TextField text_address;
    @FXML
    public TextField text_city;
    @FXML
    public TextField text_zip;
    @FXML
    public TextField text_email;
    @FXML
    public TextField text_phone;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_add;

    private DBConnect conn;

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        text_address.clear();
        text_city.clear();
        text_email.clear();
        text_name.clear();
        text_phone.clear();
        text_surname.clear();
        text_zip.clear();
    }

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
        conn = new DBConnect();
        try{
            if(validateField(text_address) &&
                    validateField(text_city) &&
                    validateField(text_email) &&
                    validateField(text_name) &&
                    validateField(text_phone) &&
                    validateField(text_surname) &&
                    validateField(text_zip)){
                conn.upload(String.format("INSERT INTO customers (name,surname,address,city,zip,email,phone) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s')",
                        text_name.getText(),
                        text_surname.getText(),
                        text_address.getText(),
                        text_city.getText(),
                        text_zip.getText(),
                        text_email.getText(),
                        text_phone.getText()));
            }
        } catch (SQLException sqlEx){
            displayMessage(ERROR, "Server connection error", sqlEx.getMessage());
        } catch (Exception ex){
            displayMessage(ERROR, ex.getMessage());
        } finally {
            btn_clear_onClick(null);
        }
    }
}
