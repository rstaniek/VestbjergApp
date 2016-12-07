package com.teamSuperior.guiApp;

import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.ConfirmBox;
import com.teamSuperior.guiApp.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainWindow extends Application {
    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layout/mainWindow.fxml"));
        primaryStage.setTitle("Team Superior - representing Silvan Inc.");
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/style/mainWindow.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Closing the program", "There might be unsaved changes. Are you sure you want to close the application?");
        if(answer){
            window.close();
        }
    }

}
