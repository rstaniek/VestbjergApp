package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainController implements Initializable {

    public Label label_name_welcome;
    public Label label_date;
    public Label label_ratioUSDDKK;
    public Label label_ratioEURDKK;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Date and time
        Task getDateTime = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    Platform.runLater(() -> label_date.setText(dtf.format(now)));
                    Thread.sleep(1000);
                }
            }
        };


        //Currency exchange update
        Task getCurrencyRatios = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true){
                    String ratioUSD = WebsiteCrawler.getExchangeRatio("https://finance.yahoo.com/quote/USDDKK=X?ltr=1");
                    String ratioEUR = WebsiteCrawler.getExchangeRatio("https://finance.yahoo.com/quote/EURDKK=X?ltr=1");
                    Platform.runLater(()->{
                        label_ratioUSDDKK.setText(ratioUSD);
                        label_ratioEURDKK.setText(ratioEUR);
                    });
                    Thread.sleep(2000);
                }
            }
        };

        Thread th = new Thread(getDateTime);
        Thread th2 = new Thread(getCurrencyRatios);
        th.setDaemon(true);
        th2.setDaemon(true);
        th.start();
        th2.start();
    }
}
