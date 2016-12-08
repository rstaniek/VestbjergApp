package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;

/**
 * Created by Domestos Maximus on 28-Nov-16.
 */
public class LogInPopupController {
    public Button btn_logIn;
    public Button btn_cancel;
    public PasswordField txt_empPassw;
    public TextField txt_empID;
    private static Employee loggedUser;
    public static boolean loggedFinal = false;

    @FXML
    public void btn_logIn_click(ActionEvent actionEvent) {
        //TODO: write an enum for error codes
        //removeRed(txt_empID);
        //removeRed(txt_empPassw);

        //validating user input
        //TODO: changing textField outline doesn't work
        validateInput(txt_empID, txt_empPassw);

        //handling the input
        //TODO: log in the user
        boolean isValid = validateUser(txt_empID.getText(), txt_empPassw.getText());
        if (isValid) {
            MainController.loginWindow.close();
        } else {
            AlertBox.display("Wrong credentials!", "Wrong e-mail/password, please try again.");
        }
    }

    @FXML
    public void btn_cancel_click(ActionEvent actionEvent) {
        MainController.loginWindow.close();
    }

    private boolean validateInput(TextField user, TextField pass) {
        if (user.getText().isEmpty() && pass.getText().isEmpty()) {
            displayError(LOGIN_EMPTY_INPUT);
            setRed(user);
            setRed(pass);
            return false;
        } else if (user.getText().isEmpty()) {
            displayError(LOGIN_USERNAME_EMPTY);
            return false;
        } else if (pass.getText().isEmpty()) {
            displayError(LOGIN_PASSWORD_EMPTY);
            return false;
        } else return true;
    }

    private void setRed(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();

        if (!styleClass.contains("tferror")) {
            styleClass.add("tferror");
        }
    }


    private boolean validateUser(String username, String password) {
        DBConnect conn = new DBConnect();
        ResultSet rs = null;
        boolean ret = false;
        String safeUsername = org.apache.commons.codec.digest.DigestUtils.sha256Hex(username);
        String safePassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        rs = conn.getFromDataBase("SELECT * FROM employees WHERE email ='" + safeUsername + "' AND password = '" + safePassword + "'");
        try {
            rs.next();
            if (rs.getInt("id") != 0 && rs.getString("name") != null
                    && rs.getString("surname") != null
                    && rs.getString("address") != null
                    && rs.getString("city") != null
                    && rs.getString("zip") != null
                    && rs.getString("email") != null
                    && rs.getString("phone") != null
                    && rs.getString("password") != null
                    && rs.getString("position") != null
                    && rs.getInt("accessLevel") >= 1
                    ) {
                loggedUser = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("address"), rs.getString("city"), rs.getString("zip"), rs.getString("email"), rs.getString("phone"), rs.getString("password"), rs.getString("position"), rs.getInt("numberOfSales"), rs.getDouble("totalRevenue"), rs.getInt("accessLevel"));
                ret = true;
                loggedFinal = true;
            } else {
                ret = false;
                loggedFinal = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            // Throws an exception if you input wrong login credentials, TODO: fix this, obviously
            // AlertBox.display("Connection Error", ex.getMessage());
        }

        return ret;
    }

    public static Employee getUser() {
        return loggedUser;
    }

    public static boolean isLogged() {
        return loggedFinal;
    }
}
