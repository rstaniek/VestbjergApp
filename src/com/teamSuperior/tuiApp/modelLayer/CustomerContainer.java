package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of customers.
 */
public class CustomerContainer {
    private static CustomerContainer ourInstance = new CustomerContainer();
    private ArrayList<Customer> customers;

    private CustomerContainer() {
        customers = new ArrayList<>();
    }

    public static CustomerContainer getInstance() {
        return ourInstance;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
