package com.teamSuperior.tuiApp.modelLayer;

import java.io.Serializable;

/**
 * Product model class.
 */
public class Product implements Serializable {
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

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d%nName: %s%nSubname: %s%nBarcode: %d%nCategory: %s%nPrice: $%.2f%nLocation: %s%nQuantity: %d%nContractor ID: %d%n%n",
                id, name, subname, barcode, category, price, location, quantity, contractorId);
    }
}
