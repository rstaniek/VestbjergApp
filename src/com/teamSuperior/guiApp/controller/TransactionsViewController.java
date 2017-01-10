package com.teamSuperior.guiApp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rajmu on 17.01.10.
 */
public class TransactionsViewController implements Initializable {
    @FXML
    public TextField text_search_query;
    @FXML
    public Button btn_search_clear;
    @FXML
    public CheckComboBox checkComboBox_search_criteria;
    @FXML
    public TableView tableView_offers;

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        //TODO: to be implemented
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: to be implemented
    }
}
