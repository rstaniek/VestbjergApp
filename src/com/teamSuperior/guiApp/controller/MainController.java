package com.teamSuperior.guiApp.controller;

import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainController {
    public Button btn_mainTab_blueJ;

    public void btn_mainTab_blueJ_click(ActionEvent actionEvent) {
        AlertBox.display("Alert!", "You're a faggot");
    }
}
