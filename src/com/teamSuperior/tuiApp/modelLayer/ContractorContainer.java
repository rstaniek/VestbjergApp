package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class ContractorContainer {
    private static ContractorContainer ourInstance = new ContractorContainer();

    public static ContractorContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Contractor> contractors;

    private ContractorContainer() {
        contractors = new ArrayList<Contractor>();
    }

    public ArrayList<Contractor> getContractors() {
        return contractors;
    }

    public void setContractors(ArrayList<Contractor> contractors) {
        this.contractors = contractors;
    }
}
