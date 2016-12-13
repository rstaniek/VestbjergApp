package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 12.12.2016.
 */
public class LeaseMachineContainer {
    private static LeaseMachineContainer ourInstance = new LeaseMachineContainer();

    public static LeaseMachineContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<LeaseMachine> leaseMachines;

    private LeaseMachineContainer() {
        leaseMachines = new ArrayList<LeaseMachine>();
    }

    public ArrayList<LeaseMachine> getLeaseMachines() {
        return leaseMachines;
    }
}
