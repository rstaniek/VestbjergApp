package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.entity.Employee;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Statistics {
    private float monthlyRevenue, expenses;
    private Employee employee;
    private int productsSold, unsuccessfulProducts;

    public Statistics(float monthlyRevenue, float expenses, Employee employee, int productsSold, int unsuccessfulProducts) {
        this.monthlyRevenue = monthlyRevenue;
        this.expenses = expenses;
        this.employee = employee;
        this.productsSold = productsSold;
        this.unsuccessfulProducts = unsuccessfulProducts;
    }

    public float getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public float getExpenses() {
        return expenses;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getProductsSold() {
        return productsSold;
    }

    public int getUnsuccessfulProducts() {
        return unsuccessfulProducts;
    }
}
