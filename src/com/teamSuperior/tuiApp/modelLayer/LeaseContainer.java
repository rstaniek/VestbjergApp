package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of leases.
 */
public class LeaseContainer {
    private static LeaseContainer ourInstance = new LeaseContainer();
    private ArrayList<Lease> leases;

    private LeaseContainer() {
        leases = new ArrayList<>();
    }

    public static LeaseContainer getInstance() {
        return ourInstance;
    }

    public ArrayList<Lease> getLeases() {
        return leases;
    }

    public void setLeases(ArrayList<Lease> leases) {
        this.leases = leases;
    }
}
