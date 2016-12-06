package com.teamSuperior.tuiApp.modelLayer;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class Product {
    private int id, barcode, quantity, contractorId;
    private double price;
    private String name, subname, category, location;

    public Product(int id, String name, String subname, int barcode, String category, double price, String location, int quantity, int contractorId) {
        this.id = id;
        this.barcode = barcode;
        this.quantity = quantity;
        this.contractorId = contractorId;
        this.price = price;
        this.name = name;
        this.subname = subname;
        this.category = category;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
