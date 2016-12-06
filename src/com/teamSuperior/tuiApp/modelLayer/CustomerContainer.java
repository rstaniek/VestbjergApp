package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class CustomerContainer {
    private static CustomerContainer ourInstance = new CustomerContainer();

    public static CustomerContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Customer> customers;

    private CustomerContainer() {
        customers = new ArrayList<Customer>();
    }

    public ArrayList<Customer> getCustomer() {
        return customers;
    }

    public void setCustomer(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
