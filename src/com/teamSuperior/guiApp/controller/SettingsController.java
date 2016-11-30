package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.ErrorCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.teamSuperior.guiApp.GUI.Error.displayError;

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
        if(validateDatabaseCredentials()){
            if(DBConnect.testConnection(text_settings_connection_hostname.getText(), text_settings_connection_username.getText(), text_settings_connection_password.getText())){
                AlertBox.display("Connection test", "Operation successful");
            }
            else Error.displayError(ErrorCode.CONNECTION_TEST_FAILED);
        }
    }

    private boolean validateDatabaseCredentials(){
        if(text_settings_connection_hostname.getText().isEmpty()){
            displayError(ErrorCode.CONNECTION_HOSTNAME_EMPTY);
            return false;
        }
        else if(text_settings_connection_username.getText().isEmpty()){
            displayError(ErrorCode.CONNECTION_USERNAME_EMPTY);
            return false;
        }
        else if(text_settings_connection_password.getText().isEmpty()){
            displayError(ErrorCode.CONNECTION_PASSWORD_EMPTY);
            return false;
        }
        else return true;
    }

}
