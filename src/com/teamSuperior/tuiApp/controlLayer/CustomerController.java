package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Customer;
import com.teamSuperior.tuiApp.modelLayer.CustomerContainer;

import java.util.Iterator;

/**
 * Customers controller.
 */

public class CustomerController {

    private CustomerContainer customerContainer;

    public CustomerController() {
        customerContainer = CustomerContainer.getInstance();
    }

    public void addCustomer(int id, String name, String surname, String address, String city, String zip, String phone, String email) {
        customerContainer.getCustomer().add(new Customer(id, name, surname, address, city, zip, phone, email));
    }

    public int viewCustomers() {
        customerContainer.getCustomer().forEach(System.out::print);
        return customerContainer.getCustomer().size();
    }

    public int listIdAndNames() {
        for (Customer customer : customerContainer.getCustomer())
            System.out.printf("ID: %d  Name: %s %s%n", customer.getId(), customer.getName(), customer.getSurname());
        return customerContainer.getCustomer().size();
    }

    public boolean foundCustomerById(int id) {
        boolean found = false;
        for (Customer customer : customerContainer.getCustomer())
            if (customer.getId() == id)
                found = true;
        return found;
    }

    public boolean removeCustomerById(int id) {
        boolean removed = false;
        Iterator<Customer> iterator = customerContainer.getCustomer().iterator();
        while (iterator.hasNext() && !removed) {
            Customer customer = iterator.next();
            if (customer.getId() == id) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

}
