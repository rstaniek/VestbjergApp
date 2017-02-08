package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.connection.IDataAccessObject;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;

/**
 * Login ConnectionController.
 */
public class UserController implements IDataAccessObject<Employee, Integer>, Initializable {
    @FXML
    public Button btn_logIn;
    @FXML
    public Button btn_cancel;
    @FXML
    public PasswordField txt_empPassw;
    @FXML
    public TextField txt_empID;

    private static ConnectionController<Employee, Integer> connectionController = new ConnectionController<>(Employee.class);

    private static Employee loggedUser;
    private static boolean loggedFinal = false;

    @FXML
    public void btn_logIn_click(ActionEvent actionEvent) {
        removeRed(txt_empID);
        removeRed(txt_empPassw);

        //validating user input
        validateInput(txt_empID, txt_empPassw);

        //handling the input
        boolean isValid = validateUser(txt_empID.getText(), txt_empPassw.getText());
        if (isValid) {
            MainController.loginWindow.close();
            WaitingBox.display("Logging in", 1500);
        } else {
            displayError(LOGIN_INCORRECT_CREDENTIALS);
        }
    }

    private void removeRed(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();

        if (styleClass.contains("tferror")) {
            styleClass.remove("tferror");
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
            setRed(user);
            return false;
        } else if (pass.getText().isEmpty()) {
            displayError(LOGIN_PASSWORD_EMPTY);
            setRed(pass);
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
        try {
            rs = conn.getFromDataBase("SELECT * FROM employees WHERE email ='" + safeUsername + "' AND password = '" + safePassword + "'");
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
                removeRed(txt_empID);
                removeRed(txt_empPassw);
            } else {
                ret = false;
                loggedFinal = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ret;
    }

    static Employee getUser() {
        return loggedUser;
    }

    static boolean isLoggedIn() {
        return loggedFinal;
    }

    static boolean isAllowed(int minAccessLevel) {
        if (loggedFinal && loggedUser.getAccessLevel() >= minAccessLevel) {
            return true;
        } else if (!loggedFinal) {
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        } else {
            displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
        }
        return false;
    }

    static boolean logout() {
        if (loggedUser != null && loggedFinal) {
            loggedUser = null;
            loggedFinal = false;
            return true;
        } else return false;
    }

    @Override
    public void persist(Employee employee) {
        connectionController.persist(employee);
    }

    @Override
    public Employee getById(Integer integer) {
        return connectionController.getById(integer);
    }

    @Override
    public List<Employee> getAll() {
        return connectionController.getAll();
    }

    @Override
    public void update(Employee employee) {
        connectionController.update(employee);
    }

    @Override
    public void delete(Employee employee) {
        connectionController.delete(employee);
    }

    @Override
    public void deleteAll() {
        connectionController.deleteAll();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
