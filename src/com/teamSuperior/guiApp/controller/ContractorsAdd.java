package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class ContractorsAdd {
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_address;
    @FXML
    public TextField text_city;
    @FXML
    public TextField text_zip;
    @FXML
    public TextField text_phone;
    @FXML
    public TextField text_email;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_clear;
    @FXML
    public Button btn_manageContractors;

    private DBConnect conn;

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        resetTextFields();
    }

    @FXML
    public void btn_manageContractors_onClick(ActionEvent actionEvent) {
    }

    private void resetTextFields(){

    }
}
