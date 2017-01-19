package com.teamSuperior.core.model;

/**
 * Created by rajmu on 17.01.19.
 */
public class BasketItem {
    private int itemID;
    private String name, subname;
    private double price;
    private String imgURL;

    public BasketItem(String name, String subname, double price) {
        this.name = name;
        this.subname = subname;
        this.price = price;
        itemID = -1;
        imgURL = "";
    }

    public BasketItem(int itemID, String name, String subname, double price, String imgURL) {

        this.itemID = itemID;
        this.name = name;
        this.subname = subname;
        this.price = price;
        this.imgURL = imgURL;
    }

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public double getPrice() {
        return price;
    }

    public String getImgURL() {
        return imgURL;
    }
}
