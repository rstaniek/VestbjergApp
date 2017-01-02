package com.teamSuperior.guiApp.GUI;

import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.scene.control.Alert;

/**
 * Error Render
 */
public class Error {
    public static void displayError(ErrorCode code) {
        String errorTitle = code.getErrorTitle();
        String errorMessage = code.getErrorMessage();
        Alert.AlertType t = code.getT();
        if (t == null) {
            t = Alert.AlertType.WARNING;
        }
        if (code.getErrorMessage().isEmpty() || code.getErrorMessage() == null) {
            errorTitle = "Unknown Error";
            errorMessage = "An unknown error occurred.";
        }
        Alert alert = new Alert(t);
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
