package com.teamSuperior.guiApp.GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by rajmu on 17.01.22.
 */
public class TextFieldBox {
    private static String answer;

    public static String display(String title, String content){
        Stage window = new Stage();
        VBox box = new VBox();
        box.setPadding(new Insets(10, 10, 10, 10));

        answer = "";

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(100);

        Label label = new Label(content);
        TextField textField = new TextField();
        textField.setPromptText(content);
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            answer = textField.getText();
            window.close();
        });

        box.getChildren().addAll(label, textField, okButton);

        Scene scene = new Scene(box);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }
}
