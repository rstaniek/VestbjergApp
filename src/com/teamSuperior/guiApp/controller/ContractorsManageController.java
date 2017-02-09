package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.service.Contractor;
import com.teamSuperior.guiApp.GUI.WaitingBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contractor manage controller
 */
public class ContractorsManageController implements Initializable {
    private static final String[] CONTRACTORS_CRITERIA = new String[]{"Name", "Address", "City", "ZIP", "Phone", "Email"};

    private static ConnectionController<Contractor, Integer> contractorConnectionController = new ConnectionController<>(Contractor.class);

    @FXML
    public TableView<Contractor> contractorsTableView;

    @FXML
    public TextField nameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cityField;
    @FXML
    public TextField zipField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField searchQueryField;

    @FXML
    public Button saveButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button clearSearchButton;

    @FXML
    private TableColumn<Contractor, String> nameColumn;
    @FXML
    private TableColumn<Contractor, String> addressColumn;
    @FXML
    private TableColumn<Contractor, String> cityColumn;
    @FXML
    private TableColumn<Contractor, String> zipColumn;
    @FXML
    private TableColumn<Contractor, String> phoneColumn;
    @FXML
    private TableColumn<Contractor, String> emailColumn;
    @FXML
    public CheckComboBox<String> searchCriteriaComboBox;

    private ObservableList<Contractor> contractors;
    private Contractor selectedContractor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contractors = FXCollections.observableArrayList();
        searchCriteriaComboBox.getItems().addAll(CONTRACTORS_CRITERIA);

        searchQueryField.textProperty().addListener(
                (observable, oldValue, newValue) -> applyFilter(newValue)
        );

        WaitingBox waitingBox = new WaitingBox("Fetching data from the database.");
        Task<Void> fetch = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                contractors = FXCollections.observableArrayList(contractorConnectionController.getAll());
                return null;
            }
        };

        fetch.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue.equals(Worker.State.SUCCEEDED)) {
                    initTableColumns(contractors);
                    waitingBox.closeWindow();
                    selectedContractor = contractorsTableView.getFocusModel().getFocusedItem();
                } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                    waitingBox.closeWindow();
                }
            }
        });

        Thread th = new Thread(fetch);
        th.setDaemon(true);
        th.start();
    }

    private void initTableColumns(ObservableList<Contractor> source) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        contractorsTableView.setItems(source);
    }

    @FXML
    public void clickContractorsTableView() {
        selectedContractor = contractorsTableView.getFocusModel().getFocusedItem();
        nameField.setText(selectedContractor.getName());
        addressField.setText(selectedContractor.getAddress());
        cityField.setText(selectedContractor.getCity());
        zipField.setText(selectedContractor.getZip());
        phoneField.setText(selectedContractor.getPhone());
        System.out.println(selectedContractor.toString());
    }

    @FXML
    public void clickSave() throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Update info confirmation");
        a.setContentText(String.format("Are you sure you want to update information about %1$s contractor?", selectedContractor.getName()));
        Button yesButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");
        Optional<ButtonType> yesResponse = a.showAndWait();
        if (yesResponse.isPresent() && ButtonType.OK.equals(yesResponse.get())) {
            WaitingBox waitingBox = new WaitingBox("Updating changes.");
            Task<Void> update = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    selectedContractor.setName(nameField.getText());
                    selectedContractor.setAddress(addressField.getText());
                    selectedContractor.setCity(cityField.getText());
                    selectedContractor.setZip(zipField.getText());
                    selectedContractor.setPhone(phoneField.getText());
                    contractorConnectionController.update(selectedContractor);

                    contractors = FXCollections.observableArrayList(contractorConnectionController.getAll());
                    return null;
                }
            };

            update.stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue.equals(Worker.State.SUCCEEDED)) {
                        contractorsTableView.setItems(contractors);
                        waitingBox.closeWindow();
                    } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                        waitingBox.closeWindow();
                    }
                }
            });

            Thread thread = new Thread(update);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    public void clickDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to perform an non-revertable action!");
        alert.setContentText(String.format("Are you sure you want to delete %1$s from the contractors list?", selectedContractor.getName()));
        Button deleteButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        Alert alertFinal = new Alert(Alert.AlertType.CONFIRMATION);
        alertFinal.setHeaderText("Are you sure?");
        alertFinal.setContentText("There is no way to take back this operation. Are you fully aware of that?");
        if (deleteResponse.isPresent()) {
            if (ButtonType.OK.equals(deleteResponse.get())) {
                Optional<ButtonType> deleteFinal = alertFinal.showAndWait();
                if (deleteFinal.isPresent()) {
                    if (ButtonType.OK.equals(deleteFinal.get())) {
                        WaitingBox waitingBox = new WaitingBox("Deleting contractor from the database.");
                        Task<Void> delete = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                contractorConnectionController.delete(selectedContractor);
                                contractors = FXCollections.observableArrayList(contractorConnectionController.getAll());
                                return null;
                            }
                        };

                        delete.stateProperty().addListener(new ChangeListener<Worker.State>() {
                            @Override
                            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                                if (newValue.equals(Worker.State.SUCCEEDED)) {
                                    contractorsTableView.setItems(contractors);
                                    waitingBox.closeWindow();
                                } else if (newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                                    waitingBox.closeWindow();
                                }
                            }
                        });

                        Thread th = new Thread(delete);
                        th.setDaemon(true);
                        th.start();
                    }
                }
            }
        }
    }

    @FXML
    public void clickClearSearch() {
        searchQueryField.clear();
        initTableColumns(contractors);
    }

    private void applyFilter(String query) {
        ObservableList<String> searchCriteriaList;
        if (searchCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
            searchCriteriaList = searchCriteriaComboBox.getCheckModel().getCheckedItems();
        } else {
            searchCriteriaList = FXCollections.observableArrayList(CONTRACTORS_CRITERIA);
        }
        contractorsTableView.setItems(contractors.filtered(contractor -> {

            if (query == null || query.isEmpty()) {
                return true;
            } else {
                String lowerCaseFilter = query.toLowerCase();

                for (String criteria : searchCriteriaList) {
                    switch (criteria) {
                        case "Name":
                            if (contractor.getName().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Address":
                            if (contractor.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "City":
                            if (contractor.getCity().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "ZIP":
                            if (contractor.getZip().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Phone":
                            if (contractor.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case "Email":
                            if (contractor.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        }));
    }
}
