package com.teamSuperior.core.model.service;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Product {
    private int id, quantity, contractorId;
    private String barcode, name, subname, category, warehouseLocation;
    private float price;

    public Product(int id, int quantity, String barcode, String name, String subname, String category, String warehouseLocation, float price) {
        this.id = id;
        this.quantity = quantity;
        this.barcode = barcode;
        this.name = name;
        this.subname = subname;
        this.category = category;
        this.warehouseLocation = warehouseLocation;
        this.price = price;
    }

    public Product(int id, int quantity, String barcode, String name, String subname, String category, String warehouseLocation, float price, int contractorId) {
        this.id = id;
        this.quantity = quantity;
        this.barcode = barcode;
        this.name = name;
        this.subname = subname;
        this.category = category;
        this.warehouseLocation = warehouseLocation;
        this.price = price;
        this.contractorId = contractorId;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public String getCategory() {
        return category;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public float getPrice() {
        return price;
    }

    public int getContractorId() {
        return contractorId;
    }

    @Override
    public String toString() {
        return String.format("Product: name=%1$s, q=%2$d", name, quantity);
    }
}
