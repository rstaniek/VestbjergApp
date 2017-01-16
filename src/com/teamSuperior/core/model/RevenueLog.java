package com.teamSuperior.core.model;

/**
 * Created by rajmu on 17.01.16.
 */
public class RevenueLog {

    private int id, sales, employeeId;
    private String querteryear;
    private float revenue;

    public RevenueLog(int id, int sales, int employeeId, String querteryear, float revenue) {
        this.id = id;
        this.sales = sales;
        this.employeeId = employeeId;
        this.querteryear = querteryear;
        this.revenue = revenue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getQuerteryear() {
        return querteryear;
    }

    public void setQuerteryear(String querteryear) {
        this.querteryear = querteryear;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }
}
