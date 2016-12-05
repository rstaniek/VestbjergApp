package com.teamSuperior.tuiApp.modelLayer;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class Offer {
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

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return String.format(
                "ID: %d%nProduct ID: %d%nDate: %s%nPrice: $%.2f%nDiscount: $%.2f%n%n",
                id, productId, date, price, discount
        );
    }
}
