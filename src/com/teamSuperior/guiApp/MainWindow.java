package com.teamSuperior.guiApp;

import com.teamSuperior.guiApp.controller.Drawable;
import com.teamSuperior.guiApp.enums.Drawables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

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
        window.getIcons().add(Drawable.getImage(this.getClass(), Drawables.ICON));
        Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/mainWindow.fxml"));
        primaryStage.setTitle("Team Superior - representing Silvan Inc.");
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void closeProgram() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Confirm Exit");
        a.setContentText("There might be unsaved changes. Are you sure you want to close the application?");
        Button exitButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeResponse = a.showAndWait();
        if (closeResponse.isPresent()) {
            if (ButtonType.OK.equals(closeResponse.get())) {
                window.close();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
