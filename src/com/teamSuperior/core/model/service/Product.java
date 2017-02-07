package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;

/**
 * Product entity
 */
@Entity
@Table(name = "products")
public class Product implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int quantity;
    private String barcode, name, subname, warehouseLocation;
    private double price;
    @ManyToOne
    private ProductCategory category;
    @ManyToOne
    private Contractor contractor;

    public Product() {
    }

    public Product(String name, String subname, String barcode, ProductCategory category, double price, String warehouseLocation, int quantity, Contractor contractor) {
        super();
        this.name = name;
        this.subname = subname;
        this.barcode = barcode;
        this.category = category;
        this.price = price;
        this.warehouseLocation = warehouseLocation;
        this.quantity = quantity;
        this.contractor = contractor;
    }

    public Product(int id, int quantity, String barcode, String name, String subname, ProductCategory category, String warehouseLocation, double price, Contractor contractor) {
        this.id = id;
        this.quantity = quantity;
        this.barcode = barcode;
        this.name = name;
        this.subname = subname;
        this.category = category;
        this.warehouseLocation = warehouseLocation;
        this.price = price;
        this.contractor = contractor;
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

    public ProductCategory getCategory() {
        return category;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public double getPrice() {
        return price;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setContractor(Contractor contractorId) {
        this.contractor = contractorId;
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

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", barcode='" + barcode + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", warehouseLocation='" + warehouseLocation + '\'' +
                ", price=" + price +
                ", contractor=" + contractor +
                '}';
    }

    @Override
    public String toJson() {
        return null;
    }
}
