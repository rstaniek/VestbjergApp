package com.teamSuperior.guiApp.enums;

import javafx.scene.control.Alert;

import static javafx.scene.control.Alert.*;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public enum ErrorCode {
    CONNECTION_HOSTNAME_EMPTY("Database connection ERROR", "No path to host was specified", AlertType.ERROR),
    CONNECTION_USERNAME_EMPTY("Database connection ERROR", "Username cannot be empty", AlertType.ERROR),
    CONNECTION_PASSWORD_EMPTY("Database connection ERROR", "Password cannot be empty", AlertType.ERROR),
    CONNECTION_TEST_FAILED("Database connection ERROR", "Test Failed", AlertType.WARNING),
    CONNECTION_REG_EMPTY("Couldn't connect to the server.", "In order to connect please fill out the configuration first!", AlertType.WARNING),
    VALIDATION_FIELD_EMPTY("All fields must be filled properly", AlertType.ERROR),
    VALIDATION_ILLEGAL_CHARS("Illegal characters spotted in th input", AlertType.ERROR),
    ACCESS_DENIED_NOT_LOGGED_IN("Access denied", "You need to be logged in to access this function!", AlertType.INFORMATION),
    ACCESS_DENIED_INSUFFICIENT_PERMISSIONS("Access denied", "You don't have permission to access this function!", AlertType.INFORMATION),
    NOT_IMPLEMENTED("Sorry", "This functionality is not yet implemented."),
    USER_ALREADY_LOGGED_OUT("You are already logged out", AlertType.INFORMATION),
    DATABASE_UPLOAD_ERROR("Upload ERROR", "Couldn't upload changes to the database!", AlertType.ERROR),
    WAREHOUSE_LOW_AMOUNT_OF_PRODUCT("Low supply of the current product.", "Please consider restocking the supplies.", AlertType.WARNING),
    TEXT_FIELD_NON_NUMERIC("Illegal, non numeric characters detected in the text field!", AlertType.ERROR),
    LOGIN_EMPTY_INPUT("User validation error", "None of the fields can be empty", AlertType.ERROR),
    LOGIN_USERNAME_EMPTY("User validation error", "Employee ID cannot be empty", AlertType.WARNING),
    LOGIN_PASSWORD_EMPTY("User validation error", "The password field cannot be empty", AlertType.WARNING);


    private String errorTitle;
    private String errorMessage;
    private AlertType t;

    ErrorCode(String errorTitle, String errorMessage) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    ErrorCode(String errorTitle, String errorMessage, AlertType t) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.t = t;
    }

    ErrorCode(String errorTitle, AlertType t) {
        this.errorTitle = errorTitle;
        errorMessage = "";
        this.t = t;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public AlertType getT() {
        return t;
    }
}
