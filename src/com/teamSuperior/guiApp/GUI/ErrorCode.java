package com.teamSuperior.guiApp.GUI;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public enum ErrorCode {
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
