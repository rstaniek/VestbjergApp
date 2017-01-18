package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.controlLayer.WebsiteCrawler;
import com.teamSuperior.core.enums.Currency;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.GUI.Window;
import com.teamSuperior.guiApp.enums.Drawables;
import com.teamSuperior.guiApp.enums.WindowType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;


/**
 * Main controller.
 */
public class MainController implements Initializable {

    @FXML
    public Button btn_logIn;

    private Stage settings;
    static Stage loginWindow;
    private Window window;

    private StringProperty welcomeMessage;
    private StringProperty currentDateTime;
    private StringProperty USDRatio;
    private StringProperty EURRatio;

    private Preferences registry;

    // just database things
    private DBConnect conn;
    private ArrayList<Employee> employees;

    public MainController() {
        welcomeMessage = new SimpleStringProperty("Please log in first.");
        currentDateTime = new SimpleStringProperty();
        USDRatio = new SimpleStringProperty("Please wait");
        EURRatio = new SimpleStringProperty("Please wait");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //christmas easter egg only
        //displayXmasWnd();
        registry = Preferences.userRoot();
        window = new Window();

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
                    Platform.runLater(() -> setCurrentDateTime(dtf.format(now)));
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
                        setUSDRatio(ratioUSD);
                        setEURRatio(ratioEUR);
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

    public String getWelcomeMessage() {
        return welcomeMessage.get();
    }

    private void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage.set(welcomeMessage);
    }

    public StringProperty welcomeMessageProperty() {
        return welcomeMessage;
    }

    public String getCurrentDateTime() {
        return currentDateTime.get();
    }

    public StringProperty currentDateTimeProperty() {
        return currentDateTime;
    }

    private void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime.set(currentDateTime);
    }

    public String getUSDRatio() {
        return USDRatio.get();
    }

    public StringProperty USDRatioProperty() {
        return USDRatio;
    }

    private void setUSDRatio(String USDRatio) {
        this.USDRatio.set(USDRatio);
    }

    public String getEURRatio() {
        return EURRatio.get();
    }

    public StringProperty EURRatioProperty() {
        return EURRatio;
    }

    private void setEURRatio(String EURRatio) {
        this.EURRatio.set(EURRatio);
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
                    Employee employee = new Employee(id, name, surname, address, city, zip, email, phone, password, position, numberOfSales, totalRevenue, accessLevel);
                    employees.add(employee);
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

    private boolean welcome() {
        if (UserController.isLoggedIn()) {
            Platform.runLater(() -> setWelcomeMessage(String.format("Welcome %s %s!", UserController.getUser().getName(), UserController.getUser().getSurname())));
            Platform.runLater(() -> btn_logIn.setDisable(true));
            return true;
        } else {
            return false;
        }
    }

    //Menu strip handling
    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void handleSettings() {
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
    public void handleLogIn() {
        if (!registry.get("DATABASE_HOSTNAME", "").equals("") && !registry.get("DATABASE_USER", "").equals("") && !registry.get("DATABASE_PASS", "").equals("")) {
            if (employees.size() < 1) connectClient();
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
            displayError(CONNECTION_REG_EMPTY);
        }
    }

    @FXML
    public void handleConnect() {
        connectClient();
    }

    @FXML
    public void handleLogOut() {
        if (UserController.logout()) {
            setWelcomeMessage("Please log in first.");
            btn_logIn.setDisable(false);
        } else {
            displayError(USER_ALREADY_LOGGED_OUT);
        }
    }

    @FXML
    public void handleEmployeesStatistics() {
        if (UserController.isLoggedIn()) {
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
        } else displayError(ACCESS_DENIED_NOT_LOGGED_IN);
    }

    @FXML
    public void handleEmployeesEdit() {
        if (UserController.isAllowed(2)) {
            window.inflate(WindowType.EMP_MANAGEMENT);
        }
    }

    @FXML
    public void handleContractorsAdd() {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/contractorsAdd.fxml"));
                Stage window = new Stage();
                window.setTitle("Add a new contractor");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ioex) {
                AlertBox.display("IO Exception", ioex.getMessage());
            } catch (Exception ex) {
                AlertBox.display("Unexpected Exception", ex.getMessage());
            }
        }
    }

    @FXML
    public void handleContractorsManage() {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/contractorsManage.fxml"));
                Stage window = new Stage();
                window.setTitle("Manage contractors");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ioex) {
                Error.displayMessage(ERROR, "This page couldn't be loaded", ioex.getMessage());
            } catch (Exception ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleProductsView() {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/productsWindow.fxml"));
                Stage window = new Stage();
                window.setTitle("Products");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleEmployeesAdd() {
        if (UserController.isAllowed(3)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/empAdd.fxml"));
                Stage window = new Stage();
                window.setTitle("Add a new employee");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
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

    @FXML
    public void handleAddOffer(ActionEvent actionEvent) {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/offerAdd.fxml"));
                Stage window = new Stage();
                window.setTitle("Add a new offer");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleViewOffers(ActionEvent actionEvent) {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/offersManage.fxml"));
                Stage window = new Stage();
                window.setTitle("manage Offers");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleNewTransaction(ActionEvent actionEvent) {
        //TODO: to be implemented
    }

    @FXML
    public void handleViewTransactions(ActionEvent actionEvent) {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/transactionsView.fxml"));
                Stage window = new Stage();
                window.setTitle("View Transactions");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleCustomers(ActionEvent actionEvent) {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/customersManage.fxml"));
                Stage window = new Stage();
                window.setTitle("View Customers");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    public void handleEmployeesShowRevenueTracker(ActionEvent actionEvent) {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../layout/revenueTrackingView.fxml"));
                Stage window = new Stage();
                window.setTitle("View Revenues");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }

    @FXML
    public void handleAboutWindow(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../layout/about.fxml"));
            Stage window = new Stage();
            window.setTitle("About");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (IOException ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
    }
}
