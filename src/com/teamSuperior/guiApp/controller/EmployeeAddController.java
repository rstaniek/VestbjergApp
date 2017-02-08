package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.Position;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Domestos on 16.12.12.
 */
public class EmployeeAddController implements Initializable, DAO<Employee, Integer> {
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
    public TextField text_phone;
    @FXML
    public TextField text_email;
    @FXML
    public TextField text_email_confirm;
    @FXML
    public PasswordField text_password;
    @FXML
    public PasswordField text_password_confirm;
    @FXML
    public ComboBox choiceBox_position;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_editEmployees;
    @FXML
    public Button btn_clear;

    private DBConnect conn;
    private ObservableList<Position> positions;
    private static Controller<Employee, Integer> controller;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positions = FXCollections.observableArrayList();
        conn = new DBConnect();
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM positions");
            while (rs.next()) {
                if (!rs.getString("name").isEmpty() && rs.getInt("id") != 0) {
                    positions.add(new Position(rs.getInt("id"), rs.getInt("accessLevel"), rs.getString("name")));
                }
            }
            System.out.print(positions.toString());
        } catch (SQLException ex) {
            displayMessage(ERROR, "SQL connection error", ex.getMessage());
        } catch (Exception exception) {
            displayMessage(ERROR, exception.getMessage());
        } finally {
            choiceBox_position.getItems().addAll(positions.stream().map(Position::getName).collect(Collectors.toList()));
            choiceBox_position.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
        if (validateField(text_name) &&
                validateField(text_address) &&
                validateField(text_surname) &&
                validateField(text_city) &&
                validateField(text_zip) &&
                validateField(text_email) &&
                validateField(text_email_confirm) &&
                validateField(text_phone) &&
                validateField(text_password) &&
                validateField(text_password_confirm) &&
                emailMatch() &&
                passwordMatch() &&
                newEmail()) {
            try {
                conn = new DBConnect();
                Position selectedPosition = null;
                for (Position position : positions) {
                    if (position.getName().equals((String) choiceBox_position.getSelectionModel().getSelectedItem())) {
                        selectedPosition = position;
                    }
                }
                String emailSafe = org.apache.commons.codec.digest.DigestUtils.sha256Hex(text_email.getText());
                String passwordSafe = org.apache.commons.codec.digest.DigestUtils.sha256Hex(text_password.getText());
                conn.upload(String.format("INSERT INTO employees (name,surname,address,city,zip,email,phone,position,password,accessLevel) VALUES('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s','%9$s','%10$s')",
                        text_name.getText(),
                        text_surname.getText(),
                        text_address.getText(),
                        text_city.getText(),
                        text_zip.getText(),
                        emailSafe,
                        text_phone.getText(),
                        selectedPosition.getName(),
                        passwordSafe,
                        selectedPosition.getAccessLevel()));
                displayMessage(INFORMATION, "Success", "Employee successfully added");

            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            } finally {
                resetTextFields();
            }
        }
    }

    @FXML
    public void btn_clear_onClick(ActionEvent actionEvent) {
        resetTextFields();
    }

    @FXML
    public void btn_editEmployees_onClick(ActionEvent actionEvent) {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/empManagement.fxml"));
                Stage window = new Stage();
                window.setTitle("Manage Employees");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ioex) {
                displayMessage(ERROR, "This page couldn't be loaded!", ioex.getMessage());
            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    private void resetTextFields() {
        text_name.clear();
        text_surname.clear();
        text_address.clear();
        text_city.clear();
        text_zip.clear();
        text_email.clear();
        text_phone.clear();
        text_email_confirm.clear();
        text_password.clear();
        text_password_confirm.clear();
    }

    @Override
    public void persist(Employee employee) {
        controller.persist(employee);
    }

    @Override
    public Employee getById(Integer integer) {
        return controller.getById(integer);
    }

    @Override
    public List<Employee> getAll() {
        return controller.getAll();
    }

    @Override
    public void update(Employee employee) {
        controller.update(employee);
    }

    @Override
    public void delete(Employee employee) {
        controller.delete(employee);
    }

    @Override
    public void deleteAll() {
        controller.deleteAll();
    }


    public boolean emailMatch() {
        if (!text_email.getText().equals(text_email_confirm.getText()))
            displayMessage(ERROR, "Emails don't match", "Please try again");
        return (text_email.getText().equals(text_email_confirm.getText()));
    }

    public boolean passwordMatch() {
        if (!text_password.getText().equals(text_password_confirm.getText()))
            displayMessage(ERROR, "Passwords don't match", "Please try again");
        return (text_password.getText().equals(text_password_confirm.getText()));
    }

    public boolean newEmail() {
        boolean isNew = true;
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM employees");
            while (rs.next()) {
                if (rs.getString("email").equals(org.apache.commons.codec.digest.DigestUtils.sha256Hex(text_email.getText()))) {
                    isNew = false;
                }
            }
        } catch (SQLException ex) {
            displayMessage(ERROR, "SQL connection error", ex.getMessage());
        } catch (Exception exception) {
            displayMessage(ERROR, exception.getMessage());
        }
        if(!isNew)
            displayMessage(ERROR, "Email already exists");
        return isNew;
    }
}