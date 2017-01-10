package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.guiApp.GUI.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by rajmu on 17.01.09.
 */
public class OffersManageController implements Initializable {
    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox checkComboBox_search_criteria;
    @FXML
    public TableView tableView_offers;

    private TableColumn<Offer, Integer> idColumn;
    private TableColumn<Offer, String> nameColumn;
    private TableColumn<Offer, Integer> productIdColumn;
    private TableColumn<Offer, Double> priceColumn;
    private TableColumn<Offer, LocalDateTime> dateColumn;
    private TableColumn<Offer, Double> discountColumn;
    private TableColumn<Offer, Time> timeColumn;

    private ObservableList<Offer> offers;
    private ObservableList<Offer> searchResults;
    private static Employee loggedUser;
    private Offer selectedOffer;
    private DBConnect conn;

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        //TODO: to be implemented
    }

    @FXML
    public void tableView_employees_onMouseClicked(MouseEvent mouseEvent) {
        //TODO: to be implemented
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        //TODO: to be implemented
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        offers = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll("Name", "Product ID", "Price", "Discount", "Date", "Time");
        conn = new DBConnect();
        loggedUser = UserController.getUser();

        retrieveData();
        initTableColumns(offers);
        selectedOffer = (Offer)tableView_offers.getFocusModel().getFocusedItem();
    }

    private void retrieveData(){
        offers = FXCollections.observableArrayList();
        conn = new DBConnect();
        ResultSet rs = conn.getFromDataBase("SELECT offers.id,offers.date,offers.time,offers.productIDs,offers.price,offers.discount,products.name FROM offers,products WHERE offers.productIDs = products.id");
        try{
            while (rs.next()){
                if(rs.getInt("offers.id") != -1){
                    offers.add(new Offer(rs.getDate("offers.date"),
                            rs.getInt("offers.id"),
                            rs.getInt("offers.productIDs"),
                            rs.getDouble("offers.price"),
                            rs.getDouble("offers.discount"),
                            rs.getString("products.name"),
                            rs.getTime("offers.time")));
                }
            }
        } catch (SQLException sqlException) {
            Error.displayMessage(ERROR, "SQL connection error.", sqlException.getMessage());
        } catch (Exception ex) {
            Error.displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<Offer> source){
        idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<Offer, Integer>("id"));

        nameColumn = new TableColumn<>("Product name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Offer, String>("productName"));

        dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Offer, LocalDateTime>("date"));

        discountColumn = new TableColumn<>("Discount");
        discountColumn.setMinWidth(60);
        discountColumn.setCellValueFactory(new PropertyValueFactory<Offer, Double>("discount"));

        priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(60);
        priceColumn.setCellValueFactory(new PropertyValueFactory<Offer, Double>("price"));

        productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setMinWidth(80);
        productIdColumn.setCellValueFactory(new PropertyValueFactory<Offer, Integer>("productID"));

        timeColumn = new TableColumn<>("Time");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory(new PropertyValueFactory<Offer, Time>("time"));

        tableView_offers.setItems(source);
        tableView_offers.getColumns().addAll(idColumn, nameColumn, productIdColumn, priceColumn, discountColumn, dateColumn, timeColumn);
    }

    //TODO: refreshing, editing, all the shit
}
