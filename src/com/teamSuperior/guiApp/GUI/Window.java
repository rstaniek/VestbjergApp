package com.teamSuperior.guiApp.GUI;

import com.teamSuperior.guiApp.enums.WindowType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Domestos Maximus on 06-Dec-16.
 */
public class Window {
    public void inflate(WindowType wnd) {
        //TODO: inflate window
        try {
            Parent root = FXMLLoader.load(getClass().getResource(wnd.getLayoutPath()));
            Stage window = new Stage();
            window.setTitle(wnd.getWndTitle());
            window.setResizable(wnd.isResizable());
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException ex) {
            AlertBox.display("Java IO Exception", ex.getMessage());
        } catch (Exception ex2) {
            AlertBox.display("Unexpected exception", ex2.getMessage());
        }
    }
}
