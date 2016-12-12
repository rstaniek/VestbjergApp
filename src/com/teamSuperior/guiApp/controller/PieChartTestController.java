package com.teamSuperior.guiApp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 12-Dec-16.
 */
public class PieChartTestController implements Initializable {
    @FXML
    public PieChart pieChart_cancer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("BlueJ", 80),
                        new PieChart.Data("Tortoise SVN", 12),
                        new PieChart.Data("Bad life decisions", 6),
                        new PieChart.Data("Cigarettes", 2));
        pieChart_cancer.getData().addAll(pieChartData);
    }
}
