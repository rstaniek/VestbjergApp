package com.teamSuperior.core.model.service;

import java.sql.Date;

/**
 * Created by rajmu on 17.01.09.
 */
public class Offer {
    private Date date;
    private int id, productID;
    private double price, discount;
    private String productName;

    public Offer(Date date, int id, int productID, double price, double discount, String productName) {
        this.date = date;
        this.id = id;
        this.productID = productID;
        this.price = price;
        this.discount = discount;
        this.productName = productName;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getProductID() {
        return productID;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public String getProductName() {
        return productName;
    }
}
