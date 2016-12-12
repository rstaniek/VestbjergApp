package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.teamSuperior.core.connection.DBConnect.*;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.ACCESS_DENIED_INSUFFICIENT_PERMISSIONS;
import static com.teamSuperior.guiApp.enums.ErrorCode.ACCESS_DENIED_NOT_LOGGED_IN;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class ContractorsAddController {
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
        if(validateField(text_address) &&
        validateField(text_city) &&
        validateField(text_email) &&
        validateField(text_name) &&
        validateField(text_phone) &&
        validateField(text_zip)){
            try{
                conn = new DBConnect();
                conn.upload(String.format("INSERT INTO contractors (name, address, city, zip, phone, email) VALUES ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s')",
                        text_name.getText(),
                        text_address.getText(),
                        text_city.getText(),
                        text_zip.getText(),
                        text_phone.getText(),
                        text_email.getText()));
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
    public void btn_manageContractors_onClick(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 2){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/contractorsManage.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Manage contractors");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(this.getClass().getResource("/path/to/css").toString());
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
        text_address.clear();
        text_city.clear();
        text_email.clear();
        text_name.clear();
        text_phone.clear();
        text_zip.clear();
    }
}
