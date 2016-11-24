package com.teamSuperior.core.model.service;

import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Sale {
    private int id;
    private String date;
    private ArrayList<Product> products;
    private float price;

    public Sale(int id, String date, ArrayList<Product> products, float price) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public float getPrice() {
        return price;
    }
}
