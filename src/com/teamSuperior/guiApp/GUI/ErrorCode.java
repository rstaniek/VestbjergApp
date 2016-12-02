package com.teamSuperior.guiApp.GUI;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public enum ErrorCode {
    CONNECTION_HOSTNAME_EMPTY("Database connection ERROR", "No path to host was specified"),
    CONNECTION_USERNAME_EMPTY("Database connection ERROR", "Username cannot be empty"),
    CONNECTION_PASSWORD_EMPTY("Database connection ERROR", "Password cannot be empty"),
    CONNECTION_TEST_FAILED("Database connection ERROR", "Test Failed"),
    VALIDATION_FIELD_EMPTY("Input ERROR", "All fields must be filled properly"),
    VALIDATION_ILLEGAL_CHARS("Input ERROR", "Illegal characters spotted in th input"),
    LOGIN_EMPTY_INPUT("User validation error", "None of the fields can be empty"),
    LOGIN_USERNAME_EMPTY("User validation error", "Employee ID cannot be empty"),
    LOGIN_PASSW_EMPTY("User validation error", "The password field cannot be empty");


    private String errorTitle;
    private String errorMessage;

    ErrorCode(String errorTitle, String errorMessage) {
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorTitle(){
        return errorTitle;
    }
}
