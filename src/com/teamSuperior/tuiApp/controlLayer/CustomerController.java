package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Customer;
import com.teamSuperior.tuiApp.modelLayer.CustomerContainer;

import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */

public class CustomerController {

    private CustomerContainer customerContainer;

    public CustomerController() { customerContainer = CustomerContainer.getInstance(); }

    public void addCustomer(int id, String name, String surname, String address, String city, String zip, String phone, String email){
        customerContainer.getCustomer().add(new Customer(id, name, surname, address, city, zip, phone, email));
    }

    public int viewCustomers(){
        for(Customer customer : customerContainer.getCustomer()) {
            System.out.println("Id: " + customer.getId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Surname: " + customer.getSurname());
            System.out.println("Address: " + customer.getAddress());
            System.out.println("City: " + customer.getCity());
            System.out.println("Zip: " + customer.getZip());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Email: " + customer.getEmail());
            System.out.println();
        }
        return customerContainer.getCustomer().size();
    }

    public int listIdAndNames(){
        for(Customer customer : customerContainer.getCustomer())
            System.out.println("ID: " + customer.getId() + "  Name: " + customer.getName() + " " + customer.getSurname());
        return customerContainer.getCustomer().size();
    }

    public boolean foundCustomerById(int id){
        boolean found = false;
        for(Customer customer : customerContainer.getCustomer())
            if(customer.getId() == id)
                found = true;
        return found;
    }

    public boolean removeCustomerById(int id){
        boolean removed = false;
        Iterator<Customer> iterator = customerContainer.getCustomer().iterator();
        while(iterator.hasNext() && !removed){
            Customer customer = iterator.next();
            if(customer.getId() == id) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

}




//<3