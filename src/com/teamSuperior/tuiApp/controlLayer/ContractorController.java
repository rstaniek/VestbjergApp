package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Contractor;
import com.teamSuperior.tuiApp.modelLayer.ContractorContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contractors controller.
 */
public class ContractorController {

    private ContractorContainer contractorContainer;

    public ContractorController() {
        contractorContainer = ContractorContainer.getInstance();
        load();
    }

    public void create(int id, String name, String address, String city, String zip, String phone, String email) {
        contractorContainer.getContractors().add(new Contractor(id, name, address, city, zip, phone, email));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/contractors.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(contractorContainer.getContractors());
        } catch (IOException e) {
            System.out.println("Problem saving contractors.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Contractor> contractors = null;
        try {
            FileInputStream fis = new FileInputStream("data/contractors.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            contractors = (ArrayList<Contractor>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading contractors.");
            c.printStackTrace();
        }
        if (contractors != null) {
            contractorContainer.setContractors(contractors);
        }
    }

    public int listAll() {
        contractorContainer.getContractors().forEach(System.out::print);
        return contractorContainer.getContractors().size();
    }

    public int listIdAndNames() {
        for (Contractor contractor : contractorContainer.getContractors())
            System.out.printf("ID: %d  Name: %s%n", contractor.getId(), contractor.getName());
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
}
