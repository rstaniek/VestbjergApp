package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of products.
 */
public class ProductContainer {
    private static ProductContainer ourInstance = new ProductContainer();
    private ArrayList<Product> products;

    private ProductContainer() {
        products = new ArrayList<>();
    }

    public static ProductContainer getInstance() {
        return ourInstance;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
