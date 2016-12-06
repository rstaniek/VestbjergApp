package com.teamSuperior.core.model.service;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Offer {
    private int id;
    private Date date;
    private ArrayList<Product> products;
    private float totalPrice, discount;
    private int customerID;
    private boolean isAccepted;

    public Offer(int id, Date date, ArrayList<Product> products, float totalPrice, float discount, int customerID, boolean isAccepted) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.customerID = customerID;
        this.isAccepted = isAccepted;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
