package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * Offer entity
 */
@Entity
@Table(name = "offers")
public class Offer implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date date, expiresDate;
    private int productIDs;
    private double price, discount;
    @Transient
    private String productName;
    private Time time, expiresTime;

    public Offer() {
    }

    public Offer(Date date, int id, int productIDs, double price, double discount, String productName, Time time, Date expiresDate, Time expiresTime, String status) {
        this.date = date;
        this.id = id;
        this.productIDs = productIDs;
        this.price = price;
        this.discount = discount;
        this.productName = productName;
        this.time = time;
        this.expiresDate = expiresDate;
        this.expiresTime = expiresTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public int getProductIDs() {
        return productIDs;
    }

    public void setProductIDs(int productID) {
        this.productIDs = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Time getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Time expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String toJson() {
        return null;
    }
}
