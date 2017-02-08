package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
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
import java.sql.SQLException;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Offer manage controller
 */
public class OffersManageController implements Initializable {
    private static final String[] OFFERS_CRITERIA = new String[]{"Name", "Product ID", "Price", "Discount", "Expiration Date"};

    private static Controller<Offer, Integer> offerController = new Controller<>(Offer.class);

    @FXML
    public Button btn_search_clear;
    @FXML
    public TextField text_search_query;
    @FXML
    public CheckComboBox<String> checkComboBox_search_criteria;
    @FXML
    public TableView tableView_offers;
    @FXML
    public Label label_productName;
    @FXML
    public Label label_price;
    @FXML
    public Label label_productID;
    @FXML
    public Label label_offerID;
    @FXML
    public TextField text_newDiscount;
    @FXML
    public TextField text_newPrice;
    @FXML
    public Button btn_update;
    @FXML
    public Button btn_delete;
    @FXML
    public Label label_status;
    private TableColumn<Offer, Integer> idColumn;
    private TableColumn<Offer, Product> productColumn;
    private TableColumn<Offer, Double> priceColumn;
    private TableColumn<Offer, Date> dateColumn;
    private TableColumn<Offer, String> discountColumn;
    private TableColumn<Offer, Time> timeColumn;
    private TableColumn<Offer, Date> expiresDateColumn;
    private TableColumn<Offer, Time> expiresTimeColumn;
    private TableColumn<Offer, String> statusColumn;
    private ObservableList<Offer> offers;
    private ObservableList<Offer> searchResults;
    private Offer selectedOffer;
    private DBConnect conn;

    @FXML
    public void btn_search_clear_onClick(ActionEvent actionEvent) {
        text_search_query.clear();
        initTableColumns(offers);
        selectedOffer = (Offer) tableView_offers.getFocusModel().getFocusedItem();
    }

    @FXML
    public void tableView_employees_onMouseClicked(MouseEvent mouseEvent) {
        selectedOffer = (Offer) tableView_offers.getFocusModel().getFocusedItem();
        updateText();
    }

    @FXML
    public void text_search_query_onKeyReleased(KeyEvent keyEvent) {
        searchResults = null;
        searchResults = performSearch(text_search_query.getText());
        tableView_offers.getColumns().removeAll(idColumn,
                productColumn,

                priceColumn,
                discountColumn,
                dateColumn,
                timeColumn,
                expiresDateColumn,
                expiresTimeColumn,
                statusColumn);
        initTableColumns(searchResults);
    }

    private ObservableList<Offer> performSearch(String query) {
        ObservableList<Offer> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return offers;
        }
        for (Offer o : offers) {
            ObservableList<String> clist;
            if (checkComboBox_search_criteria.getCheckModel().getCheckedItems().size() != 0) {
                clist = checkComboBox_search_criteria.getCheckModel().getCheckedItems();
            } else {
                clist = FXCollections.observableArrayList(OFFERS_CRITERIA);
            }
            for (String criteria : clist) {
                switch (criteria) {
                    case "Name":
                        if (o.getProduct().getName().contains(query)) {
                            results.add(o);
                        }
                        break;
                    case "Product ID":
                        if (String.valueOf(o.getProduct().getId()).contains(query)) {
                            results.add(o);
                        }
                        break;
                    case "Price":
                        if (String.valueOf(o.getPrice()).contains(query)) {
                            results.add(o);
                        }
                        break;
                    case "Discount":
                        if (String.valueOf(o.getDiscount()).contains(query)) {
                            results.add(o);
                        }
                        break;
                    case "Expiration Date":
                        if (String.valueOf(o.getExpiresDate().toString()).contains(query)) {
                            results.add(o);
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
        offers = FXCollections.observableArrayList();
        searchResults = FXCollections.observableArrayList();
        checkComboBox_search_criteria.getItems().addAll(OFFERS_CRITERIA);
        conn = new DBConnect();

        retrieveData();
        initTableColumns(offers);
        selectedOffer = (Offer) tableView_offers.getFocusModel().getFocusedItem();
        updateText();
    }

    private void retrieveData() {
        offers = FXCollections.observableArrayList(offerController.getAll());
    }

    private void initTableColumns(ObservableList<Offer> source) {
        idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<Offer, Integer>("id"));

        productColumn = new TableColumn<>("Product");
        productColumn.setMinWidth(100);
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));

        dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        discountColumn = new TableColumn<>("Discount");
        discountColumn.setMinWidth(60);
        discountColumn.setCellValueFactory(new PropertyValueFactory<Offer, String>("discount_str"));

        priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(60);
        priceColumn.setCellValueFactory(new PropertyValueFactory<Offer, Double>("price"));

        timeColumn = new TableColumn<>("Time");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory(new PropertyValueFactory<Offer, Time>("time"));

        expiresDateColumn = new TableColumn<>("Expires");
        expiresDateColumn.setMinWidth(100);
        expiresDateColumn.setCellValueFactory(new PropertyValueFactory<Offer, Date>("expiresDate"));

        expiresTimeColumn = new TableColumn<>("Exp time");
        expiresTimeColumn.setMinWidth(100);
        expiresTimeColumn.setCellValueFactory(new PropertyValueFactory<Offer, Time>("expiresTime"));

        statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<Offer, String>("status"));

        tableView_offers.setItems(source);
        tableView_offers.getColumns().addAll(idColumn,
                productColumn,
                priceColumn,
                discountColumn,
                dateColumn,
                timeColumn,
                expiresDateColumn,
                expiresTimeColumn,
                statusColumn);
    }

    private void updateText() {
        label_productName.setText(selectedOffer.getProduct().getName());
        label_price.setText(String.valueOf(selectedOffer.getPrice()));
        label_offerID.setText(String.valueOf(selectedOffer.getId()));
        label_productID.setText(String.valueOf(selectedOffer.getProduct().getId()));
        text_newPrice.setText(label_price.getText());
        text_newDiscount.setText(String.valueOf(selectedOffer.getDiscount()));
//        label_status.setText(isExpired(selectedOffer.getExpiresDate()));
        if (label_status.getText().equals("VALID")) {
            label_status.setStyle("-fx-text-fill: #009b29");
        } else {
            label_status.setStyle("-fx-text-fill: #9b000f");
        }
    }

    public void refreshWindow() {
        offers.removeAll();
        offers = null;
        offers = FXCollections.observableArrayList();
        tableView_offers.getColumns().removeAll(idColumn,
                productColumn,
                priceColumn,
                discountColumn,
                dateColumn,
                timeColumn,
                expiresDateColumn,
                expiresTimeColumn,
                statusColumn);
        retrieveData();
        initTableColumns(offers);
        selectedOffer = (Offer) tableView_offers.getFocusModel().getFocusedItem();
        updateText();
    }

    public void btn_update_onClick(ActionEvent actionEvent) {
        if (isNumeric(text_newDiscount.getText()) &&
                isNumeric(text_newPrice.getText())) {
            if (Double.parseDouble(text_newDiscount.getText()) < 100.0) {
                if (Double.parseDouble(text_newPrice.getText()) > 0) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setHeaderText("You are about to edit the existing element.");
                    a.setContentText("Do you want to perform this action?");
                    Button btnYes = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
                    Optional<ButtonType> yesResponse = a.showAndWait();
                    if (yesResponse.isPresent()) {
                        if (ButtonType.OK.equals(yesResponse.get())) {
                            conn = new DBConnect();
                            try {
                                conn.upload(String.format("UPDATE offers SET price='%1$s',discount='%2$s' WHERE id=%3$d",
                                        text_newPrice.getText(),
                                        text_newDiscount.getText(),
                                        selectedOffer.getId()));
                            } catch (SQLException sqlEx) {
                                displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
                            }
                            refreshWindow();
                        }
                    }
                } else {
                    displayError(VALUE_LESS_THAN_ZERO);
                }
            } else {
                displayError(OFFER_DISCOUNT_OUT_OF_BOUND);
            }
        } else {
            displayError(VALIDATION_ILLEGAL_CHARS);
        }
    }

    public void btn_delete_onClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(String.format("Are you sure you want to delete offer with ID: %d ?", selectedOffer.getId()));
        alert.setContentText("You will not be able to revert this action.");
        Button deleteButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        if (deleteResponse.isPresent()) {
            if (ButtonType.OK.equals(deleteResponse.get())) {
                conn = new DBConnect();
                try {
                    conn.upload(String.format("DELETE FROM offers WHERE id=%1$d", selectedOffer.getId()));
                } catch (SQLException sqlEx) {
                    displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
                }
                refreshWindow();
            }
        }
    }
}
