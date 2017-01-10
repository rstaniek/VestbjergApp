package com.teamSuperior.core.model.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

/**
 * Created by rajmu on 17.01.09.
 */
public class Offer {
    private Date date, expiresDate;
    private int id, productID;
    private double price, discount;
    private String productName;
    private Time time, expiresTime;
    private String status, discount_str;

    public Offer(Date date, int id, int productID, double price, double discount, String productName, Time time, Date expiresDate, Time expiresTime, String status) {
        this.date = date;
        this.id = id;
        this.productID = productID;
        this.price = price;
        this.discount = discount;
        this.productName = productName;
        this.time = time;
        this.expiresDate = expiresDate;
        this.expiresTime = expiresTime;
        this.status = status;
        discount_str = discount + "%";

    }

    public String getDiscount_str() {
        return discount_str;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public void setExpiresTime(Time expiresTime) {
        this.expiresTime = expiresTime;
    }

    public Date getExpiresDate() {

        return expiresDate;
    }

    public Time getExpiresTime() {
        return expiresTime;
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
