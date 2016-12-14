package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.LeaseMachine;
import com.teamSuperior.tuiApp.modelLayer.LeaseMachineContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Lease machine controller.
 */
public class LeaseMachineController {
    private LeaseMachineContainer leaseMachineContainer;

    public LeaseMachineController() {
        leaseMachineContainer = LeaseMachineContainer.getInstance();
        load();
    }

    public void create(int id, String name, double priceForDay) {
        //by default it is not leased
        leaseMachineContainer.getLeaseMachines().add(new LeaseMachine(id, name, priceForDay, false));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/lease-machines.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(leaseMachineContainer.getLeaseMachines());
        } catch (IOException e) {
            System.out.println("Problem saving lease machines.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<LeaseMachine> leaseMachines = null;
        try {
            FileInputStream fis = new FileInputStream("data/lease-machines.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            leaseMachines = (ArrayList<LeaseMachine>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading lease machines.");
            c.printStackTrace();
        }
        if (leaseMachines != null) {
            leaseMachineContainer.setLeaseMachines(leaseMachines);
        }
    }

    public int listIdAndNames() {
        for (LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            System.out.printf("ID: %d  Name: %s%n", leaseMachine.getId(), leaseMachine.getName());
        return leaseMachineContainer.getLeaseMachines().size();
    }

    public boolean foundMachineById(int id) {
        boolean found = false;
        for (LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            if (leaseMachine.getId() == id)
                found = true;
        return found;
    }

    public boolean removeMachineById(int id) {
        boolean removed = false;
        Iterator<LeaseMachine> iterator = leaseMachineContainer.getLeaseMachines().iterator();
        while (!removed && iterator.hasNext()) {
            LeaseMachine leaseMachine = iterator.next();
            if (leaseMachine.getId() == id) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public int listAll() {
        leaseMachineContainer.getLeaseMachines().forEach(System.out::print);
        return leaseMachineContainer.getLeaseMachines().size();
    }

    public void markAsLeased(int id) {
        for (LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            if (leaseMachine.getId() == id)
                leaseMachine.setLeased(true);
    }

    public void markAsNotLeased(int id) {
        for (LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            if (leaseMachine.getId() == id)
                leaseMachine.setLeased(false);
    }
}
