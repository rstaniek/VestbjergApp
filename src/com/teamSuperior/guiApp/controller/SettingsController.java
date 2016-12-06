package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.ErrorCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.teamSuperior.guiApp.GUI.Error.displayError;

/**
 * Created by Domestos on 16.11.30.
 */
public class SettingsController implements Initializable {
    @FXML
    public TextField text_settings_connection_hostname;
    @FXML
    public TextField text_settings_connection_username;
    @FXML
    public PasswordField text_settings_connection_password;
    @FXML
    public Button btn_settings_connection_testConn;
    @FXML
    public Button btn_save;
    @FXML
    public Button btn_saveQuit;
    @FXML
    public Button btn_quit;

    private Preferences registry; //application settings

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

    private void save(){
        registry.put("DATABASE_HOSTNAME", text_settings_connection_hostname.getText());
        registry.put("DATABASE_USER", text_settings_connection_username.getText());
        registry.put("DATABASE_PASS", text_settings_connection_password.getText());
    }

    public void btn_save_click(ActionEvent actionEvent) {
        save();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registry = Preferences.userRoot();
        text_settings_connection_hostname.setText(registry.get("DATABASE_HOSTNAME", ""));
        text_settings_connection_username.setText(registry.get("DATABASE_USER", ""));
        text_settings_connection_password.setText(registry.get("DATABASE_PASS", ""));
    }

    public void btn_saveQuit_clicked(ActionEvent actionEvent) {
        save();
        Stage stage = (Stage) btn_saveQuit.getScene().getWindow();
        stage.close();
    }

    public void btn_quit_clicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_quit.getScene().getWindow();
        stage.close();
    }
}
