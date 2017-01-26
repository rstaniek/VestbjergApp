package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.RevenueLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Created by rajmu on 17.01.16.
 */
public class RevenueTrackingView implements Initializable {
    @FXML
    public TableView tableView_revenueTracker;
    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;

    private TableColumn<RevenueLog, Integer> idColumn;
    private TableColumn<RevenueLog, Integer> salesColumn;
    private TableColumn<RevenueLog, Integer> employeeIDColumn;
    private TableColumn<RevenueLog, String> quarterColumn;
    private TableColumn<RevenueLog, Float> revenueColumn;

    private ObservableList<RevenueLog> revenues;
    private ObservableList<RevenueLog> searchResults;
    private DBConnect conn;

    private static final String[] revenueCriteria = new String[]{"ID", "Quarter / Year", "Employee ID"};

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(revenues);
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        initTableColumns(searchResults);
    }

    @FXML
    public void tableView_contractors_onMouseClicked(MouseEvent mouseEvent) {
    }

    private ObservableList<RevenueLog> performSearch(String query){
        ObservableList<RevenueLog> results = FXCollections.observableArrayList();
        if(query.isEmpty()){
            return revenues;
        }
        for (RevenueLog r : revenues){
            ObservableList<String> clist;
            if(checkComboBox_search_criteria.getCheckModel().getCheckedItems().size() != 0){
                clist = checkComboBox_search_criteria.getCheckModel().getCheckedItems();
            } else {
                clist = FXCollections.observableArrayList(revenueCriteria);
            }
            for (String criteria : clist){
                switch (criteria){
                    case "ID":
                        if (String.valueOf(r.getId()).contains(query)){
                            results.add(r);
                        }
                        break;
                    case "Quarter / Year":
                        if(r.getQuerteryear().contains(query)){
                            results.add(r);
                        }
                        break;
                    case "Employee ID":
                        if(String.valueOf(r.getEmployeeId()).contains(query)){
                            results.add(r);
                        }
                        break;
                        default:
                            break;
                }
            }
        }
        return results;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        revenues = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        conn = new DBConnect();
        checkComboBox_search_criteria.getItems().addAll(revenueCriteria);

        retrieveData();
        initTableColumns(revenues);
    }

    private void retrieveData(){
        try {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM revenueTrackings");
            while (rs.next()){
                if(rs.getInt("id") != 0){
                    revenues.add(new RevenueLog(rs.getInt("id"),
                            rs.getInt("sales"),
                            rs.getInt("employeeId"),
                            rs.getString("quarterYear"),
                            rs.getFloat("revenue")));
                }
            }
        } catch (SQLException sqlException) {
            displayMessage(ERROR, "SQL connection error", sqlException.getMessage());
        } catch (Exception ex) {
            displayMessage(ERROR, ex.getMessage());
        }
    }

    private void initTableColumns(ObservableList<RevenueLog> source){
        tableView_revenueTracker.getColumns().removeAll(idColumn, employeeIDColumn, quarterColumn, revenueColumn, salesColumn);

        idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<RevenueLog, Integer>("id"));

        employeeIDColumn = new TableColumn<>("Employee ID");
        employeeIDColumn.setMinWidth(50);
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<RevenueLog, Integer>("employeeId"));

        quarterColumn = new TableColumn<>("Quarter / Year");
        quarterColumn.setMinWidth(60);
        quarterColumn.setCellValueFactory(new PropertyValueFactory<RevenueLog, String>("quarteryear"));

        revenueColumn = new TableColumn<>("Revenue");
        revenueColumn.setMinWidth(60);
        revenueColumn.setCellValueFactory(new PropertyValueFactory<RevenueLog, Float>("revenue"));

        salesColumn = new TableColumn<>("Sales");
        salesColumn.setMinWidth(30);
        salesColumn.setCellValueFactory(new PropertyValueFactory<RevenueLog, Integer>("sales"));

        tableView_revenueTracker.setItems(source);
        tableView_revenueTracker.getColumns().addAll(idColumn, employeeIDColumn, quarterColumn, revenueColumn, salesColumn);
    }
}
