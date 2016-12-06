package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.*;
import com.teamSuperior.guiApp.GUI.Error;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @FXML
    public Button btn_logIn;
    @FXML
    public ImageView imgView_logo;
    @FXML
    public MenuItem menu_connection_connect;
    @FXML
    public MenuItem menu_connection_logIn;
    @FXML
    public MenuItem menu_connection_logOut;
    @FXML
    public MenuItem menu_employees_statistics;
    @FXML
    public MenuItem menu_employees_manage;

    private Stage settings;
    public static Stage loginWindow;
    private Window wnd;

    private Preferences registry;
    private boolean isLoggedIn;

    private Employee em;
    private Thread th3;

    // just database things
    DBConnect conn;
    private Employee emp;
    private ArrayList<Employee> employees;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registry = Preferences.userRoot();
        isLoggedIn = false;
        wnd = new Window();

        imgView_logo.setImage(new Image("http://www.gmkfreelogos.com/logos/S/img/Silvan.gif"));

        conn = new DBConnect();
        // generating array list and users
        employees = new ArrayList<>();
        if (credentialsSaved()) {
            connectClient();
        } else {
            Error.displayError(ErrorCode.CONNECTION_REG_EMPTY);
        }


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
                while (true) {
                    String ratioUSD = WebsiteCrawler.getExchangeRatio("https://finance.yahoo.com/quote/USDDKK=X?ltr=1");
                    String ratioEUR = WebsiteCrawler.getExchangeRatio("https://finance.yahoo.com/quote/EURDKK=X?ltr=1");
                    Platform.runLater(() -> {
                        label_ratioUSDDKK.setText(ratioUSD);
                        label_ratioEURDKK.setText(ratioEUR);
                        if (welcome() != true) {
                            welcome();
                        }
                    });
                    Thread.sleep(2000);
                }
            }
        };

        Task waitForLogin = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        if (welcome() != true) {
                            welcome();
                        } else {
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };

        Thread th = new Thread(getDateTime);
        Thread th2 = new Thread(getCurrencyRatios);
        th3 = new Thread(waitForLogin);
        th.setDaemon(true);
        th2.setDaemon(true);
        th3.setDaemon(true);
        th.start();
        th2.start();
        th3.start();
    }

    private void connectClient() {
        try {
            ResultSet rsCount = conn.getFromDataBase("SELECT COUNT(*) FROM employees");
            rsCount.next();
            int count = rsCount.getInt(1);
            rsCount.close();
            ResultSet rs = conn.getFromDataBase("SELECT * FROM employees");
            for (int i = 0; i < count - 1; i++) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String address = rs.getString("address");
                    String city = rs.getString("city");
                    String zip = rs.getString("zip");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String password = rs.getString("password");
                    String position = rs.getString("position");
                    int numberOfSales = rs.getInt("numberOfSales");
                    double totalRevenue = rs.getDouble("totalRevenue");
                    int accessLevel = rs.getInt("accessLevel");
                    emp = new Employee(id, name, surname, address, city, zip, email, phone, password, position, numberOfSales, totalRevenue, accessLevel);
                    employees.add(emp);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
    }

    private boolean credentialsSaved() {
        return !registry.get("DATABASE_HOSTNAME", "").isEmpty() &&
                !registry.get("DATABASE_USER", "").isEmpty() &&
                !registry.get("DATABASE_PASS", "").isEmpty();
    }

    //Menu strip handling
    public void menu_close_clicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void menu_settings_clicked(ActionEvent actionEvent) {
        try {
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
        } catch (IOException ex) {
            AlertBox.display("Java IO Exception", ex.getMessage());
        } catch (Exception ex2) {
            AlertBox.display("Unexpected exception", ex2.getMessage());
        }
    }

    private void settingsWndClose() {
        //TODO: handle seving the settings before closing
        settings.close();
    }

    public void btn_logIn_cicked(ActionEvent actionEvent) {
        if (!registry.get("DATABASE_HOSTNAME", "").equals("") && !registry.get("DATABASE_USER", "").equals("") && !registry.get("DATABASE_PASS", "").equals("")) {
            try {
                Parent logInScreen = FXMLLoader.load(getClass().getResource("../layout/loginWindowPopup.fxml"));
                loginWindow = new Stage();
                loginWindow.initModality(Modality.APPLICATION_MODAL);
                loginWindow.setTitle("Log in");
                loginWindow.setResizable(false);
                loginWindow.setScene(new Scene(logInScreen));
                loginWindow.show();


            } catch (Exception ex) {
                AlertBox.display("Unexpected exception", ex.getMessage());
            }
        } else {
            AlertBox.display("Log in ERROR", "Please set up the configuration first!");
        }
    }

    public void menu_connection_connect_clicked(ActionEvent actionEvent) {
        connectClient();
    }

    public void menu_connection_logIn_clicked(ActionEvent actionEvent) {
        btn_logIn_cicked(actionEvent);
    }

    public void menu_connection_logOut_clicked(ActionEvent actionEvent) {
        //TODO: implement logOut action
    }

    public boolean welcome() {
        boolean ret = false;
        if (LogInPopupController.isLogged()) {
            Platform.runLater(() -> label_name_welcome.setText("Welcome " + LogInPopupController.getUser().getName() + " " + LogInPopupController.getUser().getSurname() + "!"));
            Platform.runLater(() -> btn_logIn.setDisable(true));
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    public void menu_employees_statistics_clicked(ActionEvent actionEvent) {
        wnd.inflate(WindowType.EMP_STATS);
    }

    public void menu_employees_manage_clicked(ActionEvent actionEvent) {
        wnd.inflate(WindowType.EMP_MANAGEMENT);
    }
}
