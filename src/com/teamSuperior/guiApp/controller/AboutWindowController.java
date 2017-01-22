package com.teamSuperior.guiApp.controller;

import com.teamSuperior.guiApp.enums.Drawables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Domestos on 16.12.13.
 */
public class AboutWindowController implements Initializable {
    @FXML
    public ImageView imgView_teamLogo;
    @FXML
    public VBox vBox;

    //TODO: needs fix and implementation

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView_teamLogo.setImage(Drawable.getImage(getClass(), Drawables.TEAM_LOGO));
        imgView_teamLogo.fitWidthProperty().bind(vBox.widthProperty());
    }
}
