package com.teamSuperior.guiApp.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.teamSuperior.guiApp.GUI.AlertBox.*;

/**
 * Created by Domestos Maximus on 28-Nov-16.
 */
public class LogInPopupController {
    public Button btn_logIn;
    public Button btn_cancel;
    public PasswordField txt_empPassw;
    public TextField txt_empID;

    public void btn_logIn_click(ActionEvent actionEvent) {
        String userID = txt_empID.getText();
        String passw = txt_empPassw.getText();
        //TODO: write an enum for error codes

        //validating user input
        if(userID.isEmpty()) display("Warning", "The employee ID can not be empty!");
        if(passw.isEmpty()) display("Warning", "Password can not be empty");

        //handling the input
        //TODO: log in the user
    }

    public void btn_cancel_click(ActionEvent actionEvent) {
        //TODO: close the window
    }
}
