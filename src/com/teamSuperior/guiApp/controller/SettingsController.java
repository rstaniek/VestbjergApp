package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.exception.ConnectionException;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.Drawables;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

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
    @FXML
    public Tab tab_discounts;
    @FXML
    public CheckBox checkBox_showNotifications_lowAmountOfProducts;


    private Preferences registry; //application settings
    private DBConnect conn;
    private Employee loggedUser;

    @FXML
    public void btn_settings_connection_testConn_clicked(ActionEvent actionEvent) {
        if (validateDatabaseCredentials()) {
            if (DBConnect.testConnection(text_settings_connection_hostname.getText(), text_settings_connection_username.getText(), text_settings_connection_password.getText())) {
                Error.displayMessage(INFORMATION, "Connection test finished successfully", String.format("Connection with the server %1$s is established. Now you can log in", text_settings_connection_hostname.getText()));
            } else displayError(CONNECTION_TEST_FAILED);
        }
    }

    private boolean validateDatabaseCredentials() {
        if (text_settings_connection_hostname.getText().isEmpty()) {
            displayError(CONNECTION_HOSTNAME_EMPTY);
            return false;
        } else if (text_settings_connection_username.getText().isEmpty()) {
            displayError(CONNECTION_USERNAME_EMPTY);
            return false;
        } else if (text_settings_connection_password.getText().isEmpty()) {
            displayError(CONNECTION_PASSWORD_EMPTY);
            return false;
        } else return true;
    }

    private boolean validateDiscount(TextField tf) {
        if (tf.getText().isEmpty()) {
            displayError(VALIDATION_FIELD_EMPTY);
            return false;
        } else if (tf.getText().contains("[a-zA-Z]+")) {
            displayError(VALIDATION_ILLEGAL_CHARS);
            return false;
        } else return true;
    }

    private void save() {
        //general
        registry.putBoolean("SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS", checkBox_showNotifications_lowAmountOfProducts.isSelected());

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

        conn = new DBConnect();
        //TODO: Sth fucked up here: dear SQL gods, halp plox
        try{
            conn.upload(String.format("UPDATE discounts SET value='%1$.2f' WHERE id=1;", registry.getFloat("DISCOUNT_REGISTERED", 0)));
            conn.upload(String.format("UPDATE discounts SET value='%1$.2f' WHERE id=2;", registry.getFloat("DISCOUNT_CRAFTSMAN", 0)));
            conn.upload(String.format("UPDATE discounts SET value='%1$.2f' WHERE id=3;", registry.getFloat("DISCOUNT_QUANTITY", 0)));
            conn.upload(String.format("UPDATE discounts SET value='%1$.2f' WHERE id=4;", registry.getFloat("DISCOUNT_SELF_PICKUP", 0)));
            conn.upload(String.format("UPDATE discounts SET value='%1$.2f' WHERE id=5;", registry.getFloat("DISCOUNT_MAX", 0)));
        } catch (SQLException sqlEx){
            displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
        } catch (ConnectionException connEx){
            displayError(DATABASE_UPLOAD_ERROR);
        }
    }

    @FXML
    public void btn_save_click(ActionEvent actionEvent) {
        if (validateDiscount(text_settings_discounts_registered) &&
                validateDiscount(text_settings_discounts_craftsman) &&
                validateDiscount(text_settings_discounts_quantity) &&
                validateDiscount(text_settings_discounts_selfPickUp) &&
                validateDiscount(text_settings_discounts_maxTreshold)) {
            System.out.println(Float.parseFloat(text_settings_discounts_registered.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_craftsman.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_selfPickUp.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_quantity.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_maxTreshold.getText()));
            save();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        img_testMeme.setImage(Drawable.getImage(this.getClass(), Drawables.TEST_MEME));

        registry = Preferences.userRoot();
        loggedUser = UserController.getUser();
        if (!UserController.isLoggedIn()) {
            tab_discounts.setDisable(true);
        } else if (loggedUser.getAccessLevel() < 2) {
            tab_discounts.setDisable(true);
        }

        //general
        checkBox_showNotifications_lowAmountOfProducts.setSelected(registry.getBoolean("SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS", false));

        //connection
        text_settings_connection_hostname.setText(registry.get("DATABASE_HOSTNAME", ""));
        text_settings_connection_username.setText(registry.get("DATABASE_USER", ""));
        text_settings_connection_password.setText(registry.get("DATABASE_PASS", ""));

        if (UserController.isLoggedIn() && !registry.get("DATABASE_HOSTNAME", "").isEmpty()) {
            conn = new DBConnect();
            ResultSet rs;
            try {
                rs = conn.getFromDataBase("SELECT * FROM discounts");
                rs.next();
                registry.putFloat("DISCOUNT_REGISTERED", rs.getFloat("value"));
                rs.next();
                registry.putFloat("DISCOUNT_CRAFTSMAN", rs.getFloat("value"));
                rs.next();
                registry.putFloat("DISCOUNT_QUANTITY", rs.getFloat("value"));
                rs.next();
                registry.putFloat("DISCOUNT_SELF_PICKUP", rs.getFloat("value"));
                rs.next();
                registry.putFloat("DISCOUNT_MAX", rs.getFloat("value"));

            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }

            //CEO only features
            if (loggedUser.getAccessLevel() < 3) {
                text_settings_discounts_maxTreshold.setDisable(true);
            }
        }


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
