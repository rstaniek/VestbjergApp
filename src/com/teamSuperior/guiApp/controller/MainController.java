package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainController implements Initializable {

    public Label label_name_welcome;
    public Label label_date;
    public Label label_currencyRatio;
    public Button btn_main_updateCurrencyRatio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int i = 0;
                while (true) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    Platform.runLater(() -> label_date.setText(dtf.format(now)));
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void btn_main_updateCurrencyRatio_click(ActionEvent actionEvent) {
        label_currencyRatio.setText(WebsiteCrawler.retrieveData("http://www.investing.com/currencies/eur-dkk", "last_last"));
    }
}
