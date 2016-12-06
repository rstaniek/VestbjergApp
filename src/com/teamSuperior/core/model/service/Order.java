package com.teamSuperior.core.model.service;

import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Order {
    private int id;
    private int productID;
    private int contractorID;
    private int quantity;
    private String department;
    private boolean isApproved;
    private boolean isDelivered;

    public Order(int id, int productID, int contractorID, int quantity, String department, boolean isApproved, boolean isDelivered) {
        this.id = id;
        this.productID = productID;
        this.contractorID = contractorID;
        this.quantity = quantity;
        this.department = department;
        this.isApproved = isApproved;
        this.isDelivered = isDelivered;
    }

    public int getId() {
        return id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getContractorID() {
        return contractorID;
    }

    public void setContractorID(int contractorID) {
        this.contractorID = contractorID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
}

