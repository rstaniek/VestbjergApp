package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of contractors.
 */
public class ContractorContainer {
    private static ContractorContainer ourInstance = new ContractorContainer();

    public static ContractorContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Contractor> contractors;

    private ContractorContainer() {
        contractors = new ArrayList<>();
    }

    public ArrayList<Contractor> getContractors() {
        return contractors;
    }

}
