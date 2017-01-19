package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.model.BasketItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by rajmu on 17.01.19.
 */
public class TestBasketController implements Initializable {
    @FXML
    public ListView<BasketItem> listView_basket;

    private ObservableList<BasketItem> basketItems;
    private BasketItem selectedItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        basketItems = FXCollections.observableArrayList();

        basketItems.addAll(
                new BasketItem("Ketchup", "hot AF", 21.37),
                new BasketItem("Tuna", "Fish", 12.49),
                new BasketItem("Big white chicken", "Livestock", 96.0),
                new BasketItem("Tomato", "Vegetable", 5.99)
        );
        //TODO: instead of this dummy content this list will consist of selected products

        listView_basket.setItems(basketItems);
        listView_basket.setCellFactory(basketListView -> new BasketListViewCell());

        listView_basket.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BasketItem>() {
            @Override
            public void changed(ObservableValue<? extends BasketItem> observable, BasketItem oldValue, BasketItem newValue) {
                selectedItem = newValue;
            }
        });
    }
}

