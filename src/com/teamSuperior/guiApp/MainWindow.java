package com.teamSuperior.guiApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout/mainWindow.fxml"));
        //TODO: fix the FXMLLoader as it doesn't read the layout file properly!!!
        primaryStage.setTitle("Team Superior - representing Silvan Inc.");

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
