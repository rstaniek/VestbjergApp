package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Customer;
import com.teamSuperior.tuiApp.modelLayer.CustomerContainer;

/**
 * Created by Smoothini on 01.12.2016.
 */

public class CustomerController {

    private CustomerContainer customerContainer;

    public CustomerController() { customerContainer = CustomerContainer.getInstance(); }

    public void addCustomer(int id, String name, String surname, String address, String city, String zip, String phone, String email){
        customerContainer.getCustomer().add(new Customer(id, name, surname, address, city, zip, phone, email));
    }

}




//<3