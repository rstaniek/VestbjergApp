package com.teamSuperior.guiApp.GUI;

/**
 * Created by Domestos Maximus on 29-Nov-16.
 */
public class Error {
    public static void displayError(ErrorCode code){
        String errMsg = code.getErrorMessage();
        String errTtl = code.getErrorTitle();
        if(code.getErrorMessage().isEmpty() || code.getErrorMessage() == null){
            errMsg = "Unknown Error";
            errTtl = "ALERT!";
        }
        AlertBox.display(errTtl, errMsg);
    }
}
