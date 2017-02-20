package com.teamSuperior.guiApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.teamSuperior.core.connection.HttpRequestHandler;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Created by Domestos Maximus on 20-Feb-17.
 */
public class HttpRequestTestController {
    @FXML
    public JFXTextField text_getURL;
    @FXML
    public JFXButton btn_sendGET;
    @FXML
    public JFXTextArea textArea_getOutput;
    @FXML
    public JFXButton btn_sendPOST;
    @FXML
    public JFXTextField text_username;
    @FXML
    public JFXTextField text_pass;
    @FXML
    public JFXTextField text_description;
    public JFXButton btn_sedDelete;

    @FXML
    public void handleSendGetRequest(ActionEvent actionEvent) {
        try {
            String[] response = HttpRequestHandler.sendGet(text_getURL.getText());
            textArea_getOutput.setText(response[1]);
            Error.displayMessage(Alert.AlertType.INFORMATION, "Response code: " + response[0], response[1]);
        } catch (Exception e) {
            e.printStackTrace();
            Error.displayMessage(Alert.AlertType.ERROR, "HTTP protocol error", e.getMessage());
        }
    }

    @FXML
    public void handleSendPostRequest(ActionEvent actionEvent) {
        String data = String.format("username=%s&password=%s&description=%s", text_username.getText(), text_pass.getText(), text_description.getText());
        try {
            if (!text_username.getText().isEmpty() &&
                    !text_pass.getText().isEmpty() &&
                    !text_description.getText().isEmpty()) {
                int response = HttpRequestHandler.sendPost(text_getURL.getText(), data);
                Error.displayMessage(Alert.AlertType.INFORMATION, String.format("Response code: %d", response));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Error.displayMessage(Alert.AlertType.ERROR, "HTTP protocol error", ex.getMessage());
        } finally {
            text_username.clear();
            text_pass.clear();
            text_description.clear();
        }
    }

    @FXML
    public void handleHttpDeleteRequest(ActionEvent actionEvent) {
        try {
            int response = HttpRequestHandler.sendDelete(text_getURL.getText());
            Error.displayMessage(Alert.AlertType.INFORMATION, "Response code: " + response, "HTTP DELETE request");
        } catch (Exception e) {
            e.printStackTrace();
            Error.displayMessage(Alert.AlertType.ERROR, "HTTP protocol error", e.getMessage());
        }
    }
}
