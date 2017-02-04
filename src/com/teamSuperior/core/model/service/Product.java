package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;

/**
 * Product entity class
 */
@Entity
@Table(name = "products")
public class Product implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int quantity, contractorId;
    private String barcode, name, subname, category, warehouseLocation;
    private float price;

    public Product() {
    }

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

    public Product(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", barcode='" + barcode + '\'' +
                ", q='" + quantity + '\'' +
                ", category='" + category + '\'' +
                ", location='" + warehouseLocation + '\'' +
                ", price='" + price + '\'' +
                ", contractor Id='" + contractorId + '\'' +
                '}';
    }

    @Override
    public String toJson() {
        return null;
    }
}
