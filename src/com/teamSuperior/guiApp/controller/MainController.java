package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Domestos on 16.11.26.
 */
public class MainController implements Initializable {

    @FXML
    public Label label_name_welcome;
    @FXML
    public Label label_date;
    @FXML
    public Label label_ratioUSDDKK;
    @FXML
    public Label label_ratioEURDKK;
    @FXML
    public MenuItem menu_close;
    @FXML
    public MenuItem menu_settings;

    private Stage settings;

    private Preferences registry;
    private boolean isLoggedIn;

    private Employee em;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registry = Preferences.userRoot();
        isLoggedIn = false;

        //init loginPupup
        if(!isLoggedIn && !registry.get("DATABASE_HOSTNAME", "").equals("")){
            try{
                Parent logInScreen = FXMLLoader.load(getClass().getResource("../layout/loginWindowPopup.fxml"));
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Log in");
                window.setScene(new Scene(logInScreen));
                window.show();

                // testing db access
                em = new Employee();
                AlertBox.display("title", em.getName() + " " + em.getSurname() + " " + em.getEmail());
            }
            catch (Exception ex){
                AlertBox.display("Unexpected exception", ex.getMessage());
            }
            finally {
                //TODO: handle logging in
            }
        }
        else{
            menu_settings_clicked(null);
        }



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

    //Menu strip handling
    public void menu_close_clicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void menu_settings_clicked(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("../layout/settingsWindow.fxml"));
            Stage settingsWnd = new Stage();
            settingsWnd.setOnCloseRequest(e -> {
                e.consume();
                settingsWndClose();
            });
            settingsWnd.setTitle("Settings");
            settingsWnd.setResizable(false);
            settingsWnd.setScene(new Scene(root));
            settings = settingsWnd;
            settingsWnd.show();
        }
        catch (IOException ex){
            AlertBox.display("Java IO Exception", ex.getMessage());
        }
        catch (Exception ex2){
            AlertBox.display("Unexpected exception", ex2.getMessage());
        }
    }

    private void settingsWndClose(){
        //TODO: handle seving the settings before closing
        settings.close();
    }
}
