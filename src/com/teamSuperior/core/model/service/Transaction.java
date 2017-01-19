package com.teamSuperior.core.model.service;

import com.teamSuperior.core.connection.DBConnect;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import static com.teamSuperior.core.Utils.arrayToString;

/**
 * Created by rajmu on 17.01.11.
 */
public class Transaction {

    private int id, employeeID;
    private ArrayList<Integer> productIDs, discountIDs;
    private String productIDs_str, discountIDs_str, description;
    private double price;
    private Date date;
    private Time time;
    private DBConnect conn;

    public Transaction(int id, int employeeID, ArrayList<Integer> productIDs, ArrayList<Integer> discountIDs, double price, String description, Date date, Time time) {
        this.id = id;
        this.employeeID = employeeID;
        this.productIDs = productIDs;
        this.discountIDs = discountIDs;
        this.price = price;
        this.description = description;
        this.date = date;
        this.time = time;
        productIDs_str = arrayToString(productIDs);
        discountIDs_str = arrayToString(discountIDs);
    }

    public int getId() {
        return id;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public ArrayList<Integer> getProductIDs() {
        return productIDs;
    }

    public ArrayList<Integer> getDiscountIDs() {
        return discountIDs;
    }

    public String getProductIDs_str() {
        return productIDs_str;
    }

    public String getDiscountIDs_str() {
        return discountIDs_str;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

}
