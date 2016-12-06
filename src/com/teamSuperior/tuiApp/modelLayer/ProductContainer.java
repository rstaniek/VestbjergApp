package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class ProductContainer {
    private static ProductContainer ourInstance = new ProductContainer();

    public static ProductContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Product> products;

    private ProductContainer() {
        products = new ArrayList<Product>();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
