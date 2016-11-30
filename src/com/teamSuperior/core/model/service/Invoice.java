package com.teamSuperior.core.model.service;

import java.util.Date;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Invoice {
    private static int id;
    private Date invoiceDate, dueDate;
    private int productsID, saleID;
    private String buyerName;
    private double price;
    private boolean paid;

    public Invoice(Date invoiceDate, Date dueDate, int productsID, int saleID, String buyerName, double price, boolean paid) {
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.productsID = productsID;
        this.saleID = saleID;
        this.buyerName = buyerName;
        this.price = price;
        this.paid = paid;
    }

    public static int getId() {
        return id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getProductsID() {
        return productsID;
    }

    public void setProductsID(int productsID) {
        this.productsID = productsID;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}