package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.CustomerContainer;
import com.teamSuperior.tuiApp.modelLayer.Lease;
import com.teamSuperior.tuiApp.modelLayer.LeaseContainer;
import com.teamSuperior.tuiApp.modelLayer.LeaseMachineContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Leases controller.
 */
public class LeaseController {
    private LeaseContainer leaseContainer;
    private CustomerContainer customerContainer;
    private LeaseMachineContainer leaseMachineContainer;

    public LeaseController() {
        leaseContainer = LeaseContainer.getInstance();
        customerContainer = CustomerContainer.getInstance();
        leaseMachineContainer = LeaseMachineContainer.getInstance();
        load();
    }

    public void create(int id, int leaseMachineId, int customerId, String borrowDate, String returnDate, double price) {
        leaseContainer.getLeases().add(new Lease(id, leaseMachineId, customerId, borrowDate, returnDate, price));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/leases.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(leaseContainer.getLeases());
        } catch (IOException e) {
            System.out.println("Problem saving leases.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Lease> leases = null;
        try {
            FileInputStream fis = new FileInputStream("data/leases.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            leases = (ArrayList<Lease>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading leases.");
            c.printStackTrace();
        }
        if (leases != null) {
            leaseContainer.setLeases(leases);
        }
    }

    public int listAll() {
        leaseContainer.getLeases().forEach(System.out::print);
        return leaseContainer.getLeases().size();
    }

    public boolean canLease() {
        return (customerContainer.getCustomers().size() > 0 && leaseMachineContainer.getLeaseMachines().size() > 0);
    }

    public int listIdAndNames() {
        for (Lease lease : leaseContainer.getLeases()) {
            System.out.println("ID: " + lease.getId() + "  Lease machine ID: " + lease.getLeaseMachineId() + "  Customer ID: " + lease.getCustomerId());
        }
        return leaseContainer.getLeases().size();
    }

    public boolean foundLeaseById(int id) {
        boolean found = false;
        for (Lease lease : leaseContainer.getLeases())
            if (lease.getId() == id)
                found = true;
        return found;
    }

    public boolean removeLeaseById(int id) {
        boolean removed = false;
        Iterator<Lease> iterator = leaseContainer.getLeases().iterator();
        while (!removed && iterator.hasNext()) {
            Lease lease = iterator.next();
            if (lease.getId() == id) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public int getMachineId(int id) {
        int machineId = -1;
        for (Lease lease : leaseContainer.getLeases())
            if (lease.getId() == id)
                machineId = lease.getLeaseMachineId();
        return machineId;
    }
}
