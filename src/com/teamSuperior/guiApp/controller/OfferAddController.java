package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.Utils;
import com.teamSuperior.core.connection.ConnectionController;
import com.teamSuperior.core.model.service.Offer;
import com.teamSuperior.core.model.service.Product;
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
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.teamSuperior.core.Utils.isNumeric;
import static com.teamSuperior.core.connection.DBConnect.validateField;
import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Offer add controller
 */
public class OfferAddController implements Initializable {

    private static ConnectionController<Product, Integer> productConnectionController = new ConnectionController<>(Product.class);
    private static ConnectionController<Offer, Integer> offerConnectionController = new ConnectionController<>(Offer.class);

    @FXML
    public TextField productField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField discountField;

    @FXML
    public Button okButton;
    @FXML
    public Button clearButton;

    @FXML
    public Label productNameLabel;

    @FXML
    public DatePicker expiresDatePicker;

    private ObservableList<Product> products;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WaitingBox wb = new WaitingBox("Fetching data from the database.");
        Task<Void> fetch = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                products = FXCollections.observableArrayList(productConnectionController.getAll());
                return null;
            }
        };

        fetch.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue.equals(Worker.State.SUCCEEDED) || newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)) {
                    wb.closeWindow();
                    productNameLabel.setText("");
                }
            }
        });

        expiresDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return Utils.dateFormatter(Utils.FormatterType.DATE).format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, Utils.dateFormatter(Utils.FormatterType.DATE));
                } else {
                    return null;
                }
            }
        });

        expiresDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        if (item.isAfter(LocalDate.now()) && item.isBefore(datePicker.getValue())) {
                            setStyle("-fx-background-color: #67aaf4");
                        }
                    }
                };
            }
        });

        expiresDatePicker.setValue(LocalDate.now());

        productField.textProperty().addListener(
                (observable, oldValue, newValue) -> handleProductChange(newValue)
        );

        Thread thread = new Thread(fetch);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void clickOkButton() {
        if (validateField(discountField) &&
                validateField(priceField) &&
                validateField(productField) &&
                isNumeric(discountField.getText()) &&
                isNumeric(priceField.getText()) &&
                isNumeric(productField.getText())) {
            if (Double.parseDouble(discountField.getText()) < 100.0) {
                if (findProduct(Integer.parseInt(productField.getText())) != null) {
                    offerConnectionController.persist(new Offer(
                            productConnectionController.getById(Integer.parseInt(productField.getText())),
                            Double.parseDouble(priceField.getText()),
                            Double.parseDouble(discountField.getText()),
                            Date.valueOf(expiresDatePicker.getValue())
                    ));
                    clickClearButton();
                    displayMessage(INFORMATION, "Offer added successfully");
                } else {
                    displayError(DATABASE_PRODUCTS_NOT_FOUND);
                }
            } else {
                displayError(OFFER_DISCOUNT_OUT_OF_BOUND);
            }
        }
    }

    @FXML
    public void clickClearButton() {
        discountField.clear();
        priceField.clear();
        productField.clear();
        expiresDatePicker.getEditor().clear();
    }

    private void handleProductChange(String productId) {
        if (!productId.isEmpty()) {
            if (isNumeric(productId)) {
                if (findProduct(Integer.parseInt(productId)) != null) {
                    productNameLabel.setText(findProduct(Integer.parseInt(productId)).getName());
                    priceField.setText(Double.toString(findProduct(Integer.parseInt(productId)).getPrice()).replace(",", "."));
                } else {
                    displayError(DATABASE_PRODUCTS_NOT_FOUND);
                }
            }
        } else {
            priceField.clear();
        }
    }

    @FXML
    public void handleDiscountChange() {
        if (!discountField.getText().isEmpty()) { //after the last character is deleted, it resets the price
            if (isNumeric(discountField.getText())) {
                double currentPrice = findProduct(Integer.parseInt(productField.getText())).getPrice();
                double currentDiscount = Double.parseDouble(discountField.getText());
                double newPrice = currentPrice / 100 * (100 - currentDiscount);
                if (newPrice >= 0) { //checks that the price never goes under 0 (discount doesn't go higher than 100)
                    priceField.setText(String.format("%.2f", newPrice).replace(",", "."));
                } else { //should the price go under 0, error pops up, discount field is cleared and price is updated to original value
                    discountField.clear();
                    priceField.setText(String.format("%.2f", currentPrice).replace(",", "."));
                    displayError(OFFER_DISCOUNT_OUT_OF_BOUND);
                }
            } else { //same as in previous case, only different error pops up
                discountField.clear();
                priceField.setText(String.format("%.2f", findProduct(Integer.parseInt(productField.getText())).getPrice()).replace(",", "."));
                displayError(TEXT_FIELD_NON_NUMERIC);
            }
        } else {
            priceField.setText(Double.toString(findProduct(Integer.parseInt(productField.getText())).getPrice()).replace(",", "."));
        }
    }

    @FXML
    public void handlePriceChange() {
        if (priceField.getText().isEmpty()) discountField.clear();
        if (isNumeric(priceField.getText())) {
            double originalPrice = findProduct(Integer.parseInt(productField.getText())).getPrice();
            double currentPrice = Double.parseDouble(priceField.getText());
            double newDiscount = 100 - currentPrice / originalPrice * 100;
            if (newDiscount <= 100 && newDiscount >= 0)
                discountField.setText(String.format("%.2f", newDiscount).replace(",", "."));
            else if (newDiscount <= 100) {
                discountField.clear();
                priceField.setText(Double.toString(originalPrice).replace(",", "."));
                displayError(OFFER_DISCOUNT_LESS_THEN_ZERO);
            } else {
                discountField.clear();
                priceField.setText(Double.toString(originalPrice).replace(",", "."));
                displayError(VALUE_LESS_THAN_ZERO);
            }
        } else {
            discountField.clear();
            priceField.setText(Double.toString(findProduct(Integer.parseInt(productField.getText())).getPrice()).replace(",", "."));
            displayError(TEXT_FIELD_NON_NUMERIC);
        }
    }

    private Product findProduct(int id) {
        Product foundProduct = null;
        for (Product p : products) {
            if (p.getId() == id) foundProduct = p;
        }
        return foundProduct;
    }
}
