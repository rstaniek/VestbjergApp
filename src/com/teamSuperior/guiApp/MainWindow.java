package com.teamSuperior.guiApp;

import com.teamSuperior.guiApp.GUI.ConfirmBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Vestbjerg App GUI Version
 *
 * @author Team Superiør
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
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void closeProgram() {
        boolean answer = ConfirmBox.display("Closing the program", "There might be unsaved changes. Are you sure you want to close the application?");
        if (answer) {
            window.close();
        }
    }

}
