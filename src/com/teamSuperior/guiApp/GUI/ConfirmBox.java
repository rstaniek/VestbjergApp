package com.teamSuperior.guiApp.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Domestos on 16.11.26.
 */
public class ConfirmBox {
    private static boolean answer;
    public static boolean display(String title, String message){
        Stage window = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        answer = false;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(100);

        Label label = new Label(message);
        GridPane.setConstraints(label, 0, 0);
        Button btnYes = new Button("Yes");
        GridPane.setConstraints(btnYes, 0, 1);
        Button btnNo = new Button("No");
        GridPane.setConstraints(btnNo, 1, 1);
        btnYes.setOnAction(e -> {
            answer = true;
            window.close();
        });
        btnNo.setOnAction(e -> {
            answer = false;
            window.close();
        });

        grid.getChildren().addAll(label, btnNo, btnYes);
        //VBox layout = new VBox(10);
        //layout.getChildren().addAll(label, btnYes, btnNo);
        //layout.setAlignment(Pos.CENTER);
        //layout.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }
}
