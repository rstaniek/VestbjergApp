package com.teamSuperior.core.model.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

/**
 * Created by rajmu on 17.01.09.
 */
public class Offer {
    private Date date;
    private int id, productID;
    private double price, discount;
    private String productName;
    private Time time;

    public Offer(Date date, int id, int productID, double price, double discount, String productName, Time time) {
        this.date = date;
        this.id = id;
        this.productID = productID;
        this.price = price;
        this.discount = discount;
        this.productName = productName;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
