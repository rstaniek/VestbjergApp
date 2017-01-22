package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.model.BasketItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rajmu on 17.01.19.
 */
public class BasketListViewCell extends ListCell<BasketItem> {
    @FXML
    public GridPane itemGrid;
    @FXML
    public Label label_name;
    @FXML
    public Label label_subname;
    @FXML
    public Label label_price;
    @FXML
    public ImageView imageView_item;
    @FXML
    public Label label_discount;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(BasketItem item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../layout/basketCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            label_name.setText(item.getName());
            label_subname.setText(item.getSubname());
            label_price.setText(String.format("kr. %.2f", item.getPrice()));
            if(item.getDiscount().isEmpty()){
                label_discount.setVisible(false);
                label_discount.setText("");
                label_price.setStyle("-fx-text-fill: black");
            } else {
                label_discount.setVisible(true);
                label_discount.setText(String.format("Discount: %s%s", item.getDiscount(), "%"));
                label_price.setStyle("-fx-text-fill: #9b000f");
            }
            if(item.getImgURL().isEmpty()){
                imageView_item.setImage(new Image("http://remix1436.ct8.pl/resources/img/big-white-chicken.jpg"));
            } else {
                imageView_item.setImage(new Image(item.getImgURL()));
            }

            System.out.printf("Creating new ListView cell: name=%s, id=%d, url=%s\n", item.getName(), item.getItemID(), item.getImgURL());
            setText(null);
            setGraphic(itemGrid);
        }
    }
}
