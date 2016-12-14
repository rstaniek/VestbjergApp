package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Customer;
import com.teamSuperior.tuiApp.modelLayer.CustomerContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Customers controller.
 */
public class CustomerController {

    private CustomerContainer customerContainer;

    public CustomerController() {
        customerContainer = CustomerContainer.getInstance();
        load();
    }

    public void create(int id, String name, String surname, String address, String city, String zip, String phone, String email) {
        customerContainer.getCustomers().add(new Customer(id, name, surname, address, city, zip, phone, email));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/customers.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(customerContainer.getCustomers());
        } catch (IOException e) {
            System.out.println("Problem saving customers.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Customer> customers = null;
        try {
            FileInputStream fis = new FileInputStream("data/customers.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            customers = (ArrayList<Customer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading customers.");
            c.printStackTrace();
        }
        if (customers != null) {
            customerContainer.setCustomers(customers);
        }
    }

    public int listAll() {
        customerContainer.getCustomers().forEach(System.out::print);
        return customerContainer.getCustomers().size();
    }

    public int listIdAndNames() {
        for (Customer customer : customerContainer.getCustomers())
            System.out.printf("ID: %d  Name: %s %s%n", customer.getId(), customer.getName(), customer.getSurname());
        return customerContainer.getCustomers().size();
    }

    public boolean foundCustomerById(int id) {
        boolean found = false;
        for (Customer customer : customerContainer.getCustomers())
            if (customer.getId() == id)
                found = true;
        return found;
    }

    public boolean removeCustomerById(int id) {
        boolean removed = false;
        Iterator<Customer> iterator = customerContainer.getCustomers().iterator();
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
