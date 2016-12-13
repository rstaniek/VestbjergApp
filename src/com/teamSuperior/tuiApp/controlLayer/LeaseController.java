package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.*;

import java.util.Iterator;

/**
 * Created by Smoothini on 13.12.2016.
 */
public class LeaseController {
    private LeaseContainer leaseContainer;
    private CustomerContainer customerContainer;
    private LeaseMachineContainer leaseMachineContainer;

    public LeaseController(){
        leaseContainer = LeaseContainer.getInstance();
        customerContainer = CustomerContainer.getInstance();
        leaseMachineContainer = LeaseMachineContainer.getInstance();
    }

    public void addLease(int id, int leaseMachineId, int customerId, String borrowDate, String returnDate, double price){
        leaseContainer.getLeases().add(new Lease(id, leaseMachineId, customerId, borrowDate, returnDate, price));
    }

    public boolean canLease(){
        boolean can = false;
        if(customerContainer.getCustomer().size() > 0 && leaseMachineContainer.getLeaseMachines().size() > 0)
            can = true;
        return can;
    }

    public int viewLeases(){
        for(Lease lease : leaseContainer.getLeases()){
            System.out.println("ID: " + lease.getId());
            System.out.println("Lease Machine ID: " + lease.getLeaseMachineId());
            System.out.println("Customer ID: " + lease.getCustomerId());
            System.out.println("Borrow date: " + lease.getBorrowDate());
            System.out.println("Return date: " + lease.getReturnDate());
            System.out.println("Price: " + lease.getPrice());
            System.out.println();
        }
        return leaseContainer.getLeases().size();
    }

    public int listIdAndNames(){
        for(Lease lease : leaseContainer.getLeases()){
            System.out.println("ID: " + lease.getId() + "  Lease machine ID: " + lease.getLeaseMachineId() + "  Customer ID: " + lease.getCustomerId());
        }
        return leaseContainer.getLeases().size();
    }

    public boolean foundLeaseById(int id){
        boolean found = false;
        for(Lease lease : leaseContainer.getLeases())
            if(lease.getId() == id)
                found = true;
        return found;
    }

    public boolean removeLeaseById(int id){
        boolean removed = false;
        Iterator<Lease> iterator = leaseContainer.getLeases().iterator();
        while(!removed && iterator.hasNext()){
            Lease lease = iterator.next();
            if(lease.getId() == id){
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public int getMachineId(int id){
        int machineId = -1;
        for(Lease lease : leaseContainer.getLeases())
            if(lease.getId() == id)
                machineId = lease.getLeaseMachineId();
        return machineId;
    }
}
