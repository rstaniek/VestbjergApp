package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.LeaseMachine;
import com.teamSuperior.tuiApp.modelLayer.LeaseMachineContainer;

import java.util.Iterator;

/**
 * Created by Smoothini on 12.12.2016.
 */
public class LeaseMachineController {
    private LeaseMachineContainer leaseMachineContainer;

    public LeaseMachineController(){leaseMachineContainer = LeaseMachineContainer.getInstance();}

    public void addLeaseMachine(int id, String name, double priceForDay){
        //by default it is not leased
        leaseMachineContainer.getLeaseMachines().add(new LeaseMachine(id, name, priceForDay, false));
    }

    public int listIdAndNames(){
        for(LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            System.out.println("ID: " + leaseMachine.getId() + "  Name: " + leaseMachine.getName());
        return leaseMachineContainer.getLeaseMachines().size();
    }

    public boolean foundMachineById(int id){
        boolean found = false;
        for(LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines())
            if(leaseMachine.getId() == id)
                found = true;
        return found;
    }

    public boolean removeMachineById(int id){
        boolean removed = false;
        Iterator<LeaseMachine> iterator = leaseMachineContainer.getLeaseMachines().iterator();
        while(!removed && iterator.hasNext()){
            LeaseMachine leaseMachine = iterator.next();
            if(leaseMachine.getId() == id){
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public int viewLeaseMachines(){
        for(LeaseMachine leaseMachine : leaseMachineContainer.getLeaseMachines()){
            System.out.println("ID: " + leaseMachine.getId());
            System.out.println("Name: " + leaseMachine.getName());
            System.out.println("Price for a day: " + leaseMachine.getPriceForDay() + "$");
            if(leaseMachine.isLeased())
                System.out.println("Leased: Yes");
            else
                System.out.println("Leased: No");
        }
        return leaseMachineContainer.getLeaseMachines().size();
    }
}
