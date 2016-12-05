package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Contractor;
import com.teamSuperior.tuiApp.modelLayer.ContractorContainer;

import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class ContractorController {
    private ContractorContainer contractorContainer;

    public ContractorController() {
        contractorContainer = ContractorContainer.getInstance();
    }

    public void addContractor(int id, String name, String address, String city, String zip, String phone, String email) {
        contractorContainer.getContractors().add(new Contractor(id, name, address, city, zip, phone, email));
    }

    public int listIdAndNames() {
        for (Contractor contractor : contractorContainer.getContractors())
            System.out.println("ID: " + contractor.getId() + "  Name: " + contractor.getName());
        return contractorContainer.getContractors().size();
    }

    public boolean foundContractorById(int id) {
        boolean found = false;
        for (Contractor contractor : contractorContainer.getContractors())
            if (contractor.getId() == id)
                found = true;
        return found;
    }

    public boolean removeContractorById(int id) {
        boolean removed = false;
        Iterator<Contractor> it = contractorContainer.getContractors().iterator();
        while (!removed && it.hasNext()) {
            Contractor contractor = it.next();
            if (contractor.getId() == id) {
                removed = true;
                it.remove();
            }
        }
        return removed;
    }

    public int viewContractors() {
        for (Contractor contractor : contractorContainer.getContractors()) {
            System.out.println("ID: " + contractor.getId());
            System.out.println("Name: " + contractor.getName());
            System.out.println("Address: " + contractor.getAddress());
            System.out.println("City: " + contractor.getCity());
            System.out.println("ZIP: " + contractor.getZip());
            System.out.println("Phone: " + contractor.getPhone());
            System.out.println("Email: " + contractor.getEmail());
            System.out.println();
        }
        return contractorContainer.getContractors().size();
    }
}
