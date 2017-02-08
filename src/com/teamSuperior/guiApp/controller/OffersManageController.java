package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.isExpired;
import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;

/**
 * Offer manage controller
 */
public class OffersManageController implements Initializable {
    private static final String[] OFFERS_CRITERIA = new String[]{"Name", "Product ID", "Price", "Discount", "Expiration Date"};

    private static ConnectionController<Offer, Integer> offerConnectionController = new ConnectionController<>(Offer.class);

    @FXML
    public Button clearSearchButton;
    @FXML
    public TextField searchQueryField;
    @FXML
    public CheckComboBox<String> searchCriteriaComboBox;
    @FXML
    public TableView<Offer> offersTableView;
    @FXML
    public Label productNameLabel;
    @FXML
    public Label priceLabel;
    @FXML
    public Label productLabel;
    @FXML
    public Label offerIdLabel;
    @FXML
    public TextField newDiscountField;
    @FXML
    public TextField newPriceField;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Label statusLabel;
    @FXML
    private TableColumn<Offer, Integer> idColumn;
    @FXML
    private TableColumn<Offer, Product> productColumn;
    @FXML
    private TableColumn<Offer, Double> priceColumn;
    @FXML
    private TableColumn<Offer, Timestamp> dateColumn;
    @FXML
    private TableColumn<Offer, String> discountColumn;
    @FXML
    private TableColumn<Offer, Date> expiresDateColumn;
    @FXML
    private TableColumn<Offer, String> statusColumn;

    private ObservableList<Offer> offers;
    private ObservableList<Offer> searchResults;
    private Offer selectedOffer;

    @FXML
    public void clickClearSearch() {
        searchQueryField.clear();
        initTableColumns(offers);
        selectedOffer = offersTableView.getFocusModel().getFocusedItem();
    }

    @FXML
    public void clickOffersTableView() {
        selectedOffer = offersTableView.getFocusModel().getFocusedItem();
        updateText();
    }

    @FXML
    public void handleSearchQuery() {
        searchResults = null;
        searchResults = performSearch(searchQueryField.getText());
        initTableColumns(searchResults);
    }

    private ObservableList<Offer> performSearch(String query) {
        ObservableList<Offer> results = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            return offers;
        }
        for (Offer o : offers) {
            ObservableList<String> clist;
            if (searchCriteriaComboBox.getCheckModel().getCheckedItems().size() != 0) {
                clist = searchCriteriaComboBox.getCheckModel().getCheckedItems();
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
        searchCriteriaComboBox.getItems().addAll(OFFERS_CRITERIA);

        retrieveData();
        initTableColumns(offers);
        selectedOffer = offersTableView.getFocusModel().getFocusedItem();
        updateText();
    }

    private void retrieveData() {
        offers = FXCollections.observableArrayList(offerConnectionController.getAll());
    }

    private void initTableColumns(ObservableList<Offer> source) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        dateColumn.setCellFactory(col -> new TableCell<Offer, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toLocalDateTime().format(Utils.dateFormatter(Utils.FormatterType.DATE)));
            }
        });
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        expiresDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiresDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        expiresDateColumn.setCellFactory(col -> new TableCell<Offer, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toLocalDate().format(Utils.dateFormatter(Utils.FormatterType.DATE)));
            }
        });

        offersTableView.setItems(source);
    }

    private void updateText() {
        productNameLabel.setText(selectedOffer.getProduct().getName());
        priceLabel.setText(String.valueOf(selectedOffer.getPrice()));
        offerIdLabel.setText(String.valueOf(selectedOffer.getId()));
        productLabel.setText(String.valueOf(selectedOffer.getProduct().getId()));
        newPriceField.setText(priceLabel.getText());
        newDiscountField.setText(String.valueOf(selectedOffer.getDiscount()));
        statusLabel.setText(isExpired(selectedOffer.getExpiresDate()));
        if (statusLabel.getText().equals("VALID")) {
            statusLabel.setStyle("-fx-text-fill: #009b29");
        } else {
            statusLabel.setStyle("-fx-text-fill: #9b000f");
        }
    }

    private void refreshWindow() {
        offers.removeAll();
        offers = null;
        offers = FXCollections.observableArrayList();
        retrieveData();
        initTableColumns(offers);
        selectedOffer = offersTableView.getFocusModel().getFocusedItem();
        updateText();
    }

    public void clickUpdate() {
        if (isNumeric(newDiscountField.getText()) &&
                isNumeric(newPriceField.getText())) {
            if (Double.parseDouble(newDiscountField.getText()) < 100.0) {
                if (Double.parseDouble(newPriceField.getText()) > 0) {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setHeaderText("You are about to edit the existing element.");
                    a.setContentText("Do you want to perform this action?");
                    Optional<ButtonType> yesResponse = a.showAndWait();
                    if (yesResponse.isPresent()) {
                        if (ButtonType.OK.equals(yesResponse.get())) {
                            selectedOffer.setPrice(Double.parseDouble(newPriceField.getText()));
                            selectedOffer.setDiscount(Double.parseDouble(newDiscountField.getText()));
                            offerConnectionController.update(selectedOffer);
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

    public void clickDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(String.format("Are you sure you want to delete offer with ID: %d ?", selectedOffer.getId()));
        alert.setContentText("You will not be able to revert this action.");
        Button deleteButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        deleteButton.setText("Delete");
        Optional<ButtonType> deleteResponse = alert.showAndWait();
        if (deleteResponse.isPresent()) {
            if (ButtonType.OK.equals(deleteResponse.get())) {
                offerConnectionController.delete(selectedOffer);
                refreshWindow();
            }
        }
    }
}
