package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.Drawables;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    @FXML
    public TextField text_settings_discounts_registered;
    @FXML
    public TextField text_settings_discounts_craftsman;
    @FXML
    public TextField text_settings_discounts_quantity;
    @FXML
    public TextField text_settings_discounts_selfPickUp;
    @FXML
    public TextField text_settings_discounts_maxTreshold;
    @FXML
    public ImageView img_testMeme;


    private Preferences registry; //application settings

    @FXML
    public void btn_settings_connection_testConn_clicked(ActionEvent actionEvent) {
        if (validateDatabaseCredentials()) {
            if (DBConnect.testConnection(text_settings_connection_hostname.getText(), text_settings_connection_username.getText(), text_settings_connection_password.getText())) {
                AlertBox.display("Connection test", "Operation successful");
            } else Error.displayError(ErrorCode.CONNECTION_TEST_FAILED);
        }
    }

    private boolean validateDatabaseCredentials() {
        if (text_settings_connection_hostname.getText().isEmpty()) {
            displayError(ErrorCode.CONNECTION_HOSTNAME_EMPTY);
            return false;
        } else if (text_settings_connection_username.getText().isEmpty()) {
            displayError(ErrorCode.CONNECTION_USERNAME_EMPTY);
            return false;
        } else if (text_settings_connection_password.getText().isEmpty()) {
            displayError(ErrorCode.CONNECTION_PASSWORD_EMPTY);
            return false;
        } else return true;
    }

    private boolean validateDiscount(TextField tf) {
        if (tf.getText().isEmpty()) {
            Error.displayError(ErrorCode.VALIDATION_FIELD_EMPTY);
            return false;
        } else if (tf.getText().contains("[a-zA-Z]+")) {
            Error.displayError(ErrorCode.VALIDATION_ILLEGAL_CHARS);
            return false;
        } else return true;
    }

    private void save() {
        //connection
        registry.put("DATABASE_HOSTNAME", text_settings_connection_hostname.getText());
        registry.put("DATABASE_USER", text_settings_connection_username.getText());
        registry.put("DATABASE_PASS", text_settings_connection_password.getText());

        //discounts
        registry.putFloat("DISCOUNT_REGISTERED", Float.parseFloat(text_settings_discounts_registered.getText()));
        registry.putFloat("DISCOUNT_CRAFTSMAN", Float.parseFloat(text_settings_discounts_craftsman.getText()));
        registry.putFloat("DISCOUNT_SELF_PICKUP", Float.parseFloat(text_settings_discounts_selfPickUp.getText()));
        registry.putFloat("DISCOUNT_QUANTITY", Float.parseFloat(text_settings_discounts_quantity.getText()));
        registry.putFloat("DISCOUNT_MAX", Float.parseFloat(text_settings_discounts_maxTreshold.getText()));
    }

    @FXML
    public void btn_save_click(ActionEvent actionEvent) {
        if (validateDiscount(text_settings_discounts_registered) &&
                validateDiscount(text_settings_discounts_craftsman) &&
                validateDiscount(text_settings_discounts_quantity) &&
                validateDiscount(text_settings_discounts_selfPickUp) &&
                validateDiscount(text_settings_discounts_maxTreshold)) {
            save();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        img_testMeme.setImage(Drawable.getImage(this.getClass(), Drawables.TEST_MEME));

        registry = Preferences.userRoot();

        //connection
        text_settings_connection_hostname.setText(registry.get("DATABASE_HOSTNAME", ""));
        text_settings_connection_username.setText(registry.get("DATABASE_USER", ""));
        text_settings_connection_password.setText(registry.get("DATABASE_PASS", ""));


        //discounts
        text_settings_discounts_registered.setText(String.valueOf(registry.getFloat("DISCOUNT_REGISTERED", 0)));
        text_settings_discounts_craftsman.setText(String.valueOf(registry.getFloat("DISCOUNT_CRAFTSMAN", 0)));
        text_settings_discounts_quantity.setText(String.valueOf(registry.getFloat("DISCOUNT_QUANTITY", 0)));
        text_settings_discounts_selfPickUp.setText(String.valueOf(registry.getFloat("DISCOUNT_SELF_PICKUP", 0)));
        text_settings_discounts_maxTreshold.setText(String.valueOf(registry.getFloat("DISCOUNT_MAX", 0)));
    }

    @FXML
    public void btn_saveQuit_clicked(ActionEvent actionEvent) {
        save();
        Stage stage = (Stage) btn_saveQuit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_quit_clicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_quit.getScene().getWindow();
        stage.close();
    }
}
