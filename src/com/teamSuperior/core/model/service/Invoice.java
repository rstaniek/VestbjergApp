package com.teamSuperior.core.model.service;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Invoice{
    private String empID, date;
    private int amount;
    private boolean isApproved;

    public Invoice(String empID, String date, int amount) {
        this.empID = empID;
        this.date = date;
        this.amount = amount;
        isApproved = false;
    }

    public String getEmpID() {
        return empID;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void approve(){
        isApproved = true;
    }
}
