package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of lease machines.
 */
public class LeaseMachineContainer {
    private static LeaseMachineContainer ourInstance = new LeaseMachineContainer();

    public static LeaseMachineContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<LeaseMachine> leaseMachines;

    private LeaseMachineContainer() {
        leaseMachines = new ArrayList<>();
    }

    public ArrayList<LeaseMachine> getLeaseMachines() {
        return leaseMachines;
    }

    public void setLeaseMachines(ArrayList<LeaseMachine> leaseMachines) {
        this.leaseMachines = leaseMachines;
    }
}
