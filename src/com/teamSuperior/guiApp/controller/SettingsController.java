package com.teamSuperior.guiApp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Created by Domestos on 16.11.30.
 */
public class SettingsController {
    @FXML
    public TextField text_settings_connection_hostname;
    @FXML
    public TextField text_settings_connection_username;
    @FXML
    public PasswordField text_settings_connection_password;
    @FXML
    public CheckBox checkbox_database_keepSettings;
    @FXML
    public Button btn_settings_connection_testConn;

    public void btn_settings_connection_testConn_clicked(ActionEvent actionEvent) {
    }
}
