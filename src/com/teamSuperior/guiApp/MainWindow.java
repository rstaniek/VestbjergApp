package com.teamSuperior.guiApp;

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
    private boolean isLoggedIn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        isLoggedIn = false;
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layout/mainWindow.fxml"));
        primaryStage.setTitle("Team Superior - representing Silvan Inc.");

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        if(!isLoggedIn){
            try{
                Parent logInScreen = FXMLLoader.load(getClass().getResource("layout/loginWindowPopup.fxml"));
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Log in");
                window.setScene(new Scene(logInScreen));
                window.show();
            }
            catch (Exception ex){
                AlertBox.display("Unexpected exception", ex.getMessage());
            }
            finally {
                //TODO: handle logging in
            }
        }
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Closing the program", "There might be unsaved changes. Are you sure you want to close the application?");
        if(answer){
            window.close();
        }
    }

}
