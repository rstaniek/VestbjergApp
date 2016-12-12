package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 12.12.2016.
 */
public class LeaseContainer {
    private static LeaseContainer ourInstance = new LeaseContainer();

    public static LeaseContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Lease> leases;

    private LeaseContainer() { leases = new ArrayList<Lease>(); }

    public ArrayList<Lease> getLeases() { return leases; }
}
