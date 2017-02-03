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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @FXML
    public AreaChart efficiency;
    @FXML
    public PieChart sales_chart;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public LineChart currency_eurChart;
    @FXML
    public NumberAxis xAxisCurrencyEur;
    @FXML
    public NumberAxis yAxisCurrencyEur;
    @FXML
    public Hyperlink button_copyEur;
    @FXML
    public Hyperlink button_copyUsd;





    private Stage settings;
    static Stage loginWindow;
    private Window window;

    private StringProperty welcomeMessage;
    private StringProperty currentTime;
    private StringProperty currentDate;
    private StringProperty USDRatio;
    private StringProperty EURRatio;
    private Parent transactionsFXMLLoader;


    private Preferences registry;

    // just database things
    private DBConnect conn;
    private ArrayList<Employee> employees;
    private boolean effChartInitialized;
    private boolean salesPercentageInitialized;
    private boolean currencyChartsInitialized;
    private ArrayList<String> eurData;
    private ArrayList<String> usdData;
    private XYChart.Series eurSeries;
    private XYChart.Series usdSeries;
    private int chartCounter;
    private ObservableList<PieChart.Data> contributionData;

    public MainController() {
        welcomeMessage = new SimpleStringProperty("");
        currentTime = new SimpleStringProperty();
        currentDate = new SimpleStringProperty();
        USDRatio = new SimpleStringProperty("Loading");
        EURRatio = new SimpleStringProperty("Loading");
        effChartInitialized = false;
        salesPercentageInitialized = false;
        currencyChartsInitialized = false;
        eurData = new ArrayList<>();
        usdData = new ArrayList<>();
        eurSeries = new XYChart.Series();
        usdSeries = new XYChart.Series();
        chartCounter = 0;
        button_copyUsd = new Hyperlink();
        button_copyUsd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("This link is clicked");
            }
        });
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
                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                    LocalDateTime now = LocalDateTime.now();
                    Platform.runLater(() -> setCurrentDateTime(timeFormat.format(now), dateFormat.format(now)));
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
                        if(usdData.size() < 30){
                            usdData.add(ratioUSD);
                        }
                        else{
                            usdData.remove(0);
                            usdData.add(ratioUSD);
                        }
                        setEURRatio(ratioEUR);
                        if(eurData.size() < 30){
                            eurData.add(ratioEUR);
                        }
                        else{
                            eurData.remove(0);
                            eurData.add(ratioUSD);
                        }
                        displayCurrencyCharts();
                        if(!effChartInitialized)
                        {
                            sortEmployees();
                            displayEfficiencyChart();
                            displaySalesPercentageChart();
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
                        if (!welcome()) {
                            welcome();
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };

        Task updateCharts = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        efficiency.setAnimated(false);
                        sales_chart.setAnimated(false);
                        efficiency.getData().clear();
                        effChartInitialized = false;
                        displayEfficiencyChart();
                        sales_chart.getData().clear();
                        displaySalesPercentageChart();
                        efficiency.setAnimated(true);
                        sales_chart.setAnimated(true);
                    });
                    Thread.sleep(6000);
                }
            }
        };

        Thread th = new Thread(getDateTime);
        Thread th2 = new Thread(getCurrencyRatios);
        Thread th3 = new Thread(waitForLogin);
        //Thread th4 = new Thread(updateCharts);
        th.setDaemon(true);
        th2.setDaemon(true);
        th3.setDaemon(true);
        //th4.setDaemon(true);
        th.start();
        th2.start();
        th3.start();
        //th4.start();
        if(eurSeries.getData() != null){
            currency_eurChart.getData().addAll(eurSeries, usdSeries);
        }
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

    public void setCurrentDateTime(String currentTime, String currentDate)
    {
        this.currentTime.set(currentTime);
        this.currentDate.set(currentDate);
    }

    public String getCurrentTime() { return currentTime.get(); }

    public String getCurrentDate() { return currentDate.get(); }

    public StringProperty currentDateProperty() { return currentDate; }

    public StringProperty currentTimeProperty()  {
        return currentTime;
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
            Platform.runLater(() -> setWelcomeMessage(String.format("%s %s!", UserController.getUser().getName(), UserController.getUser().getSurname())));
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/settingsWindow.fxml"));
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
                Parent logInScreen = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/loginWindowPopup.fxml"));
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
            efficiency.getData().clear();
            sales_chart.getData().clear();
            effChartInitialized = false;
            salesPercentageInitialized = false;
            btn_logIn.setDisable(false);
        } else {
            displayError(USER_ALREADY_LOGGED_OUT);
        }
    }

    @FXML
    public void handleEmployeesStatistics() {
        if (UserController.isLoggedIn()) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/empStatistics.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/contractorsAdd.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/contractorsManage.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/productsWindow.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/empAdd.fxml"));
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
    private void displayEfficiencyChart(){
        if(UserController.isLoggedIn()){
            XYChart.Series effSeries = new XYChart.Series();
            XYChart.Series effSeriesPersonal = new XYChart.Series();
            xAxis.setTickLabelsVisible(false);
            effSeriesPersonal.setName("Your record");
            efficiency.setTitle("Efficiency chart");
            double eff;
            int zeroEffCounter = 0;
            for(int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId() == UserController.getUser().getId()) {
                    if((employees.get(i).getNumberOfSales() != 0) || (employees.get(i).getTotalRevenue() != 0)){
                        eff = (employees.get(i).getTotalRevenue()) / (employees.get(i).getNumberOfSales());
                        effSeriesPersonal.getData().add(new XYChart.Data(i, eff));
                        effSeries.getData().add(new XYChart.Data(i, eff));
                    }
                    else {
                        eff = 0;
                        zeroEffCounter++;
                    }
                } else {
                    if((employees.get(i).getNumberOfSales() != 0) || (employees.get(i).getTotalRevenue() != 0)){
                        eff = (employees.get(i).getTotalRevenue()) / (employees.get(i).getNumberOfSales());
                        effSeries.getData().add(new XYChart.Data(i, eff));
                    }
                    else{
                        eff = 0;
                        zeroEffCounter++;
                    }
                }
            }
            efficiency.setCreateSymbols(true);
            yAxis.setAutoRanging(true);
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(employees.size() - zeroEffCounter);
            efficiency.getData().addAll(effSeries, effSeriesPersonal);
            effChartInitialized = true;
        }
    }

    private void displaySalesPercentageChart() {
        if(UserController.isLoggedIn()){
            contributionData = FXCollections.observableArrayList();
            int totalSales = 0;
            for(Employee e : employees){
                totalSales += e.getNumberOfSales();
            }
            double salesPercentageLabel = (((double)UserController.getUser().getNumberOfSales()*100)/(double)totalSales);
            DecimalFormat percentageFormat = new DecimalFormat("###.##");
            contributionData.addAll(new PieChart.Data("Total sales\n" + totalSales, totalSales), new PieChart.Data("Your sales \n"+ percentageFormat.format(salesPercentageLabel)+"%", UserController.getUser().getNumberOfSales()));
            sales_chart.getData().addAll(contributionData);
        }
    }

    @FXML
    private void displayCurrencyCharts(){
        if(UserController.isLoggedIn()){
            currency_eurChart.setAnimated(false);
            double eurValue = 0;
            double usdValue = 0;
            xAxisCurrencyEur.setTickMarkVisible(false);
            currency_eurChart.setLegendVisible(true);
            currency_eurChart.setLegendSide(Side.RIGHT);
            eurSeries.setName("EUR/DKK \n("+getEURRatio()+")");
            usdSeries.setName("USD/DKK \n("+getUSDRatio()+")");
            button_copyEur.setVisible(true);
            button_copyUsd.setVisible(true);
            button_copyEur.toFront();
            button_copyUsd.toFront();
            currency_eurChart.setTitle("Currency rates tracker");
            chartCounter++;
            try{
                eurValue = Double.parseDouble(getEURRatio());
                usdValue = Double.parseDouble(getUSDRatio());
                yAxisCurrencyEur.setAutoRanging(false);
                yAxisCurrencyEur.setLowerBound(((eurValue+usdValue)/2)-1);
                yAxisCurrencyEur.setUpperBound(((eurValue+usdValue)/2)+1);
                yAxisCurrencyEur.setTickUnit(1);
            }
            catch(NumberFormatException ex){
                eurValue = 0;
                usdValue = 0;
            }
            if(chartCounter < 43200){
                eurSeries.getData().addAll(new XYChart.Data(chartCounter-1, eurValue));
                usdSeries.getData().addAll(new XYChart.Data(chartCounter-1, usdValue));
            }
            else{
                eurSeries.getData().clear();
                chartCounter = 0;
                /* Code that supposedly should work but it doesn't - make the chart dynamic again
                eurSeries.getData().remove(0);
                for(int i=0; i< eurSeries.getData().size(); i++){
                    XYChart.Data data = (XYChart.Data)eurSeries.getData().get(i);
                    int x = (int)data.getXValue();
                    data.setXValue(x-1);
                    eurSeries.getData().add(new XYChart.Data(eurSeries.getData().size()+1, eurValue));
                }
                */
            }
        }
    }

    @FXML
    public void handleCopyEur(ActionEvent actionEvent)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection str = new StringSelection(getEURRatio());
        clipboard.setContents(str, null);


    }
    @FXML
    public void handleCopyUsd(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection str = new StringSelection(getUSDRatio());
        clipboard.setContents(str, null);
    }
    @FXML
    public void handleAddOffer(ActionEvent actionEvent) {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/offerAdd.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/offersManage.fxml"));
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/transactionsAdd.fxml"));
            Stage window = new Stage();
            window.setTitle("Add a new transaction");
            window.setResizable(false);
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (IOException ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
    }

    @FXML
    public void handleViewTransactions(ActionEvent actionEvent) {
        if (UserController.isAllowed(1)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/transactionsView.fxml"));
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
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/customersManage.fxml"));
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

    //sorting stuff
    public void sortEmployees(){
        Collections.sort(employees, new Comparator<Employee>() {
            public int compare(Employee e1, Employee e2) {
                return Double.compare(e1.getTotalRevenue() / e1.getNumberOfSales(), e2.getTotalRevenue() / e2.getNumberOfSales());
            }
        });
    }

    @FXML
    public void handleAddNewProduct(ActionEvent actionEvent) {
        if (UserController.isAllowed(2)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/teamSuperior/guiApp/layout/productAdd.fxml"));
                Stage window = new Stage();
                window.setTitle("Add new product");
                window.setResizable(false);
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                Error.displayMessage(ERROR, ex.getMessage());
            }
        }
    }
}
