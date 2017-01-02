package com.teamSuperior.guiApp.enums;

import javafx.scene.control.Alert;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public enum ErrorCode {
    CONNECTION_HOSTNAME_EMPTY("Database connection ERROR", "No path to host was specified", Alert.AlertType.ERROR),
    CONNECTION_USERNAME_EMPTY("Database connection ERROR", "Username cannot be empty", Alert.AlertType.ERROR),
    CONNECTION_PASSWORD_EMPTY("Database connection ERROR", "Password cannot be empty", Alert.AlertType.ERROR),
    CONNECTION_TEST_FAILED("Database connection ERROR", "Test Failed", Alert.AlertType.WARNING),
    CONNECTION_REG_EMPTY("Connection ERROR", "In order to connect please fill out the configuration first!", Alert.AlertType.WARNING),
    VALIDATION_FIELD_EMPTY("All fields must be filled properly", Alert.AlertType.ERROR),
    VALIDATION_ILLEGAL_CHARS("Illegal characters spotted in th input", Alert.AlertType.ERROR),
    ACCESS_DENIED_NOT_LOGGED_IN("Access denied", "You need to be logged in to access this function!", Alert.AlertType.INFORMATION),
    ACCESS_DENIED_INSUFFICIENT_PERMISSIONS("Access denied", "You don't have permission to access this function!", Alert.AlertType.INFORMATION),
    NOT_IMPLEMENTED("Sorry", "This functionality is not yet implemented."),
    USER_ALREADY_LOGGED_OUT("You are already logged out", Alert.AlertType.INFORMATION),
    DATABASE_UPLOAD_ERROR("Upload ERROR", "Couldn't upload changes to the database!", Alert.AlertType.ERROR),
    WAREHOUSE_LOW_AMOUNT_OF_PRODUCT("Low supply of the current product. Please consider restocking the supplies", Alert.AlertType.WARNING),
    TEXT_FIELD_NON_NUMERIC("Illegal, non numeric characters detected in the text field!", Alert.AlertType.ERROR),
    LOGIN_EMPTY_INPUT("User validation error", "None of the fields can be empty", Alert.AlertType.ERROR),
    LOGIN_USERNAME_EMPTY("User validation error", "Employee ID cannot be empty", Alert.AlertType.WARNING),
    LOGIN_PASSWORD_EMPTY("User validation error", "The password field cannot be empty", Alert.AlertType.WARNING);


    private String errorTitle;
    private String errorMessage;
    private Alert.AlertType t;

    ErrorCode(String errorTitle, String errorMessage) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    ErrorCode(String errorTitle, String errorMessage, Alert.AlertType t) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.t = t;
    }

    ErrorCode(String errorTitle, Alert.AlertType t) {
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

    public Alert.AlertType getT() {
        return t;
    }
}
