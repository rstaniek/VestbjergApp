package com.teamSuperior.core.API;

/**
 * Created by Domestos Maximus on 20-Feb-17.
 */
public class Product {
    private int Id;
    private String name;
    private float price;
    private int quantity;

    public Product(int id, String name, float price, int quantity) {
        this.Id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
