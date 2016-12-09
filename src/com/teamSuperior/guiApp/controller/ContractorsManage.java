package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.core.model.service.Contractor;
import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Domestos Maximus on 09-Dec-16.
 */
public class ContractorsManage implements Initializable {
    @FXML
    public TableView tableView_contractors;

    private ObservableList<Contractor> contractors;
    private static Employee loggedInUser;
    private Contractor selectedContractor;
    private DBConnect conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialized");
        contractors = FXCollections.observableArrayList();
        conn = new DBConnect();
        loggedInUser = LogInPopupController.getUser();
        ResultSet rs = conn.getFromDataBase("SELECT * FROM contractors");
        try{
            while(rs.next()){
                if(rs.getString("name") != null &&
                        rs.getString("adress") != null &&
                        rs.getString("city") != null &&
                        rs.getString("zip") != null &&
                        rs.getString("phone") != null &&
                        rs.getString("email") != null){
                    Contractor tmp = new Contractor(rs.getString("name"),
                            rs.getString("adress"),
                            rs.getString("city"),
                            rs.getString("zip"),
                            rs.getString("phone"),
                            rs.getString("email"));
                    System.out.print(tmp.toString());
                    contractors.add(tmp);
                }
            }
        }
        catch (SQLException sqlException){
            AlertBox.display("SQL exception", sqlException.getMessage());
        }
        catch (Exception ex){
            AlertBox.display("Unexpected exception", ex.getMessage());
        }

        //init table
        initTableColumns();
    }

    private void initTableColumns(){
        TableColumn<Contractor, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("name"));

        TableColumn<Contractor, String> addressCol = new TableColumn<>("Address");
        addressCol.setMinWidth(150);
        addressCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("address"));

        TableColumn<Contractor, String> cityCol = new TableColumn<>("City");
        cityCol.setMinWidth(80);
        cityCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("city"));

        TableColumn<Contractor, String> zipCol = new TableColumn<>("ZIP");
        zipCol.setMinWidth(50);
        zipCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("zip"));

        TableColumn<Contractor, String> phoneCol = new TableColumn<>("Phone number");
        phoneCol.setMinWidth(150);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("phone"));

        TableColumn<Contractor, String> emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(150);
        emailCol.setCellValueFactory(new PropertyValueFactory<Contractor, String>("email"));

        tableView_contractors.setItems(contractors);
        tableView_contractors.getColumns().addAll(nameCol, addressCol, cityCol, zipCol, phoneCol, emailCol);
    }

    @FXML
    public void tableView_contractors_onMouseClicked(MouseEvent mouseEvent) {
        selectedContractor = (Contractor) tableView_contractors.getFocusModel().getFocusedItem();
    }
}
