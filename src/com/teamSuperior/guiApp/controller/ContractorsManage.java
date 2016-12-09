package com.teamSuperior.guiApp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class ContractorsManage implements Initializable {
    @FXML
    public TableView tableView_contractors;

    @FXML
    public void tableView_contractors_onMouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
