package com.teamSuperior.core.model.service;

import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Order {
    private int id;
    private ArrayList<Product> products;
    private boolean isApproved;

    public Order(int id, ArrayList<Product> products) {
        this.id = id;
        this.products = products;
        isApproved = false;
    }

    public int getId() {

        return id;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void appprove(){
        isApproved = true;
    }
}
