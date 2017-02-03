package com.teamSuperior.core.model.service;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Machine {
    private int id;
    private String name;
    private float pricePerDay;
    private boolean leased;

    public Machine(int id, String name, float pricePerDay, boolean leased) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.leased = leased;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPricePerDay() {
        return pricePerDay;
    }

    public boolean isLeased() {
        return leased;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", leased=" + leased +
                '}';
    }
}
