package com.teamSuperior.tuiApp.modelLayer;

/**
 * Created by Smoothini on 12.12.2016.
 */
public class LeaseMachine {
    private int id;
    private String name;
    private double priceForDay;
    private boolean leased;

    public LeaseMachine(int id, String name, double priceForDay, boolean leased) {
        this.id = id;
        this.name = name;
        this.priceForDay = priceForDay;
        this.leased = leased;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceForDay() {
        return priceForDay;
    }

    public void setPriceForDay(double priceForDay) {
        this.priceForDay = priceForDay;
    }

    public boolean isLeased() {
        return leased;
    }

    public void setLeased(boolean leased) {
        this.leased = leased;
    }
}
