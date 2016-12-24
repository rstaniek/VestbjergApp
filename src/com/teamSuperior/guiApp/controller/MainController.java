package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import com.teamSuperior.core.enums.Currency;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Window;
import com.teamSuperior.guiApp.enums.Drawables;
import com.teamSuperior.guiApp.enums.WindowType;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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

import static com.teamSuperior.guiApp.GUI.Error.*;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;


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
    public MenuItem menu_connection_connect;
    @FXML
    public MenuItem menu_connection_logIn;
    @FXML
    public MenuItem menu_connection_logOut;
    @FXML
    public MenuItem menu_employees_statistics;
    @FXML
    public MenuItem menu_employees_edit;
    @FXML
    public ImageView imgView_logo;
    @FXML
    public AnchorPane anchorPane_center;
    @FXML
    public Label label_ratioDesc1;
    @FXML
    public Label label_ratioDesc2;
    @FXML
    public MenuItem menu_contractors_add;
    @FXML
    public MenuItem menu_contractors_manage;
    @FXML
    public MenuItem menu_file_test;
    @FXML
    public MenuItem menu_products_view;
    @FXML
    public MenuItem menu_employees_add;

    private Stage settings;
    static Stage loginWindow;
    private Window wnd;

    private Preferences registry;
    private boolean isLoggedIn;

    private Employee em;

    // just database things
    DBConnect conn;
    private Employee emp;
    private ArrayList<Employee> employees;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayXmasWnd();
        registry = Preferences.userRoot();
        isLoggedIn = false;
        wnd = new Window();

        /*label_ratioEURDKK.getStyleClass().add("fontWhite");
        label_ratioUSDDKK.getStyleClass().add("fontWhite");
        label_ratioDesc1.getStyleClass().add("fontWhite");
        label_ratioDesc2.getStyleClass().add("fontWhite");*/ //doesn't fucking work
        Color white = Color.web("#eeeeee");
        label_ratioEURDKK.setTextFill(white);
        label_ratioUSDDKK.setTextFill(white);
        label_ratioDesc1.setTextFill(white);
        label_ratioDesc2.setTextFill(white);
        anchorPane_center.getStyleClass().add("backgroundBlue");

        imgView_logo.setImage(Drawable.getImage(this.getClass(), Drawables.APP_LOGO));

        conn = new DBConnect();
        // generating array list and users
        employees = new ArrayList<>();
        if (credentialsSaved()) {
            //connectClient();
        } else {
            displayError(CONNECTION_REG_EMPTY);
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
                    String ratioUSD = WebsiteCrawler.getExchangeRatioBloomberg(Currency.USDDKK);
                    String ratioEUR = WebsiteCrawler.getExchangeRatioBloomberg(Currency.EURDKK);
                    Platform.runLater(() -> {
                        label_ratioUSDDKK.setText(ratioUSD);
                        label_ratioEURDKK.setText(ratioEUR);
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
                        if (!welcome()) {
                            welcome();
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };

        Thread th = new Thread(getDateTime);
        Thread th2 = new Thread(getCurrencyRatios);
        Thread th3 = new Thread(waitForLogin);
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

    private void settingsWndClose() {
        settings.close();
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

    //Menu strip handling
    @FXML
    public void menu_close_clicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
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

    @FXML
    public void btn_logIn_clicked(ActionEvent actionEvent) {
        if (!registry.get("DATABASE_HOSTNAME", "").equals("") && !registry.get("DATABASE_USER", "").equals("") && !registry.get("DATABASE_PASS", "").equals("")) {
            if(employees.size() < 1) connectClient();
            try {
                Parent logInScreen = FXMLLoader.load(getClass().getResource("../layout/loginWindowPopup.fxml"));
                loginWindow = new Stage();
                loginWindow.initModality(Modality.APPLICATION_MODAL);
                loginWindow.setTitle("Log in");
                loginWindow.setResizable(false);
                Scene scene = new Scene(logInScreen);
                scene.getStylesheets().add(this.getClass().getResource("/style/textField-error.css").toString());
                loginWindow.setScene(scene);
                loginWindow.show();


            } catch (Exception ex) {
                AlertBox.display("Unexpected exception", ex.getMessage());
            }
        } else {
            AlertBox.display("Log in ERROR", "Please set up the configuration first!");
        }
    }

    @FXML
    public void menu_connection_connect_clicked(ActionEvent actionEvent) {
        connectClient();
    }

    @FXML
    public void menu_connection_logIn_clicked(ActionEvent actionEvent) {
        btn_logIn_clicked(actionEvent);
    }

    @FXML
    public void menu_connection_logOut_clicked(ActionEvent actionEvent) {
        if(LogInPopupController.logOut()){
            label_name_welcome.setText("Please log in first");
            btn_logIn.setDisable(false);
        }else displayError(USER_ALREADY_LOGGED_OUT);
    }

    @FXML
    public void menu_employees_statistics_clicked(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/empStatistics.fxml"));
                Stage window = new Stage();
                window.setTitle("Employee statistics");
                window.setResizable(false);
                Scene scene = new Scene(root);
                scene.getStylesheets().add(this.getClass().getResource("/style/empStats.css").toString());
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                AlertBox.display("Java IO Exception", ex.getMessage());
            } catch (Exception ex2) {
                AlertBox.display("Unexpected exception", ex2.getMessage());
            }
        }else displayError(ACCESS_DENIED_NOT_LOGGED_IN);
    }

    @FXML
    public void menu_employees_edit_clicked(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()) {
            if(LogInPopupController.getUser().getAccessLevel() >= 2){
                wnd.inflate(WindowType.EMP_MANAGEMENT);
            }else{
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }else displayError(ACCESS_DENIED_NOT_LOGGED_IN);
    }

    @FXML
    public void menu_contractors_add_onClick(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 2){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/contractorsAdd.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Add a new contractor");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(this.getClass().getResource("/path/to/css").toString());
                    window.setScene(scene);
                    window.show();
                }
                catch (IOException ioex){
                    AlertBox.display("IO Exception", ioex.getMessage());
                }
                catch (Exception ex){
                    AlertBox.display("Unexpected Exception", ex.getMessage());
                }
            }
            else{
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }
        else{
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        }
    }

    @FXML
    public void menu_contractors_manage_onClick(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 2){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/contractorsManage.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Manage contractors");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(this.getClass().getResource("/path/to/css").toString());
                    window.setScene(scene);
                    window.show();
                }
                catch (IOException ioex){
                    AlertBox.display("IO Exception", ioex.getMessage());
                }
                catch (Exception ex){
                    AlertBox.display("Unexpected Exception", ex.getMessage());
                }
            }
            else{
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }
        else{
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        }
    }

    @FXML
    public void menu_products_view_onClick(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 1){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/productsWindow.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Products");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    window.setScene(scene);
                    window.show();
                }
                catch (IOException ex){
                    AlertBox.display("IO Exception", ex.getMessage());
                }
            }
            else {
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }
        else {
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        }
    }

    @FXML
    public void menu_employees_add_onClick(ActionEvent actionEvent) {
        if(LogInPopupController.isLogged()){
            if(LogInPopupController.getUser().getAccessLevel() >= 3){
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("../layout/empAdd.fxml"));
                    Stage window = new Stage();
                    window.setTitle("Add a new employee");
                    window.setResizable(false);
                    Scene scene = new Scene(root);
                    window.setScene(scene);
                    window.show();
                }
                catch (IOException ex){
                    AlertBox.display("IO Exception", ex.getMessage());
                }
            }
            else {
                displayError(ACCESS_DENIED_INSUFFICIENT_PERMISSIONS);
            }
        }
        else {
            displayError(ACCESS_DENIED_NOT_LOGGED_IN);
        }
    }

    private void displayXmasWnd() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(666);
        window.setMinHeight(563);
        window.setResizable(false);
        window.setTitle("Merry X-mas mofos");
        AnchorPane anchorPane = new AnchorPane();
        ImageView imageView = new ImageView();
        anchorPane.getChildren().addAll(imageView);
        imageView.setImage(Drawable.getImage(this.getClass(), Drawables.X_MAS_IMAGE));
        Scene scene = new Scene(anchorPane);
        window.setScene(scene);
        window.showAndWait();
    }
}
