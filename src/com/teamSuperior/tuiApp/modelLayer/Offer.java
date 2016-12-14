package com.teamSuperior.tuiApp.modelLayer;

import java.io.Serializable;

/**
 * Offer model class.
 */
public class Offer implements Serializable {
    private int id, productId;
    private String date;
    private double price, discount;

    public Offer(int id, int productId, String date, double price, double discount) {
        this.id = id;
        this.productId = productId;
        this.date = date;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d%nProduct ID: %d%nDate: %s%nPrice: $%.2f%nDiscount: $%.2f%n%n",
                id, productId, date, price, discount
        );
    }
}
