package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.teamSuperior.core.connection.DBConnect.*;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.ACCESS_DENIED_INSUFFICIENT_PERMISSIONS;
import static com.teamSuperior.guiApp.enums.ErrorCode.ACCESS_DENIED_NOT_LOGGED_IN;
import static com.teamSuperior.guiApp.enums.ErrorCode.NOT_IMPLEMENTED;

/**
 * Created by Domestos on 16.12.12.
 */
public class EmployeeAddController implements Initializable {
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
    public PasswordField text_password;
    @FXML
    public ChoiceBox choiceBox_position;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_editEmployees;
    @FXML
    public Button btn_clear;

    private DBConnect conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btn_add_onClick(ActionEvent actionEvent) {
        if(validateField(text_name) &&
                validateField(text_address) &&
                validateField(text_surname) &&
                validateField(text_city) &&
                validateField(text_zip) &&
                validateField(text_email) &&
                validateField(text_phone) &&
                validateField(text_password)){
            try{
                conn = new DBConnect();
                //TODO: finish implementation
                //conn.upload(String.format("INSERT INTO employees (name,surname,address,city,zip,email,phone,position,password) VALUES(/*add values*/)"));
                displayError(NOT_IMPLEMENTED);
            }
            catch (Exception ex){
                AlertBox.display("Unexpected exception", ex.getMessage());
            }
            finally {
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
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 2){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/empManagement.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Manage Employees");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    window.setScene(scene);
                    window.show();
                }
                catch (IOException ioex){
                    AlertBox.display("IO Exception", ioex.getMessage());
                }
                catch (Exception ex){
                    AlertBox.display("Unexpected Exception", ex.getMessage());
                }
            }
            else{
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }
        else{
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        }
    }

    private void resetTextFields(){
        text_name.clear();
        text_surname.clear();
        text_address.clear();
        text_city.clear();
        text_zip.clear();
        text_email.clear();
        text_phone.clear();
        text_password.clear();
    }
}
