package com.teamSuperior.core.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Domestos on 16.11.26.
 */
public class AlertBox {

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, btnClose);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}