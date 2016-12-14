package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of contractors.
 */
public class ContractorContainer {
    private static ContractorContainer ourInstance = new ContractorContainer();
    private ArrayList<Contractor> contractors;

    private ContractorContainer() {
        contractors = new ArrayList<>();
    }

    public static ContractorContainer getInstance() {
        return ourInstance;
    }

    public ArrayList<Contractor> getContractors() {
        return contractors;
    }

    public void setContractors(ArrayList<Contractor> contractors) {
        this.contractors = contractors;
    }
}
