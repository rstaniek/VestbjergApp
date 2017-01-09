package com.teamSuperior.guiApp.enums;

import static javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.*;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public enum ErrorCode {
    CONNECTION_HOSTNAME_EMPTY("Database connection ERROR", "No path to host was specified", ERROR),
    CONNECTION_USERNAME_EMPTY("Database connection ERROR", "Username cannot be empty", ERROR),
    CONNECTION_PASSWORD_EMPTY("Database connection ERROR", "Password cannot be empty", ERROR),
    CONNECTION_TEST_FAILED("Database connection ERROR", "Test Failed", WARNING),
    CONNECTION_REG_EMPTY("Couldn't connect to the server.", "In order to connect please fill out the configuration first!", WARNING),
    DATABASE_PRODUCTS_NOT_FOUND("Product with such ID doesn't exist", "Please check the warehouse of validity of typed ID", ERROR),
    VALIDATION_FIELD_EMPTY("All fields must be filled properly", ERROR),
    VALIDATION_ILLEGAL_CHARS("Illegal characters spotted in th input", ERROR),
    ACCESS_DENIED_NOT_LOGGED_IN("Access denied", "You need to be logged in to access this function!", INFORMATION),
    ACCESS_DENIED_INSUFFICIENT_PERMISSIONS("Access denied", "You don't have permission to access this function!", INFORMATION),
    NOT_IMPLEMENTED("Sorry", "This functionality is not yet implemented."),
    USER_ALREADY_LOGGED_OUT("You are already logged out", INFORMATION),
    DATABASE_UPLOAD_ERROR("Upload ERROR", "Couldn't upload changes to the database!", ERROR),
    WAREHOUSE_LOW_AMOUNT_OF_PRODUCT("Low supply of the current product.", "Please consider restocking the supplies.", WARNING),
    TEXT_FIELD_NON_NUMERIC("Illegal, non numeric characters detected in the text field!", ERROR),
    LOGIN_INCORRECT_CREDENTIALS("Username and/or password are incorrect!", WARNING),
    LOGIN_EMPTY_INPUT("User validation error", "None of the fields can be empty", ERROR),
    LOGIN_USERNAME_EMPTY("User validation error", "Employee ID cannot be empty", WARNING),
    LOGIN_PASSWORD_EMPTY("User validation error", "The password field cannot be empty", WARNING);


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
