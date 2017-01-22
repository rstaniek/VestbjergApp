package com.teamSuperior.core.model;

/**
 * Created by rajmu on 17.01.22.
 */
public class Discount {
    int id;
    double value;
    String title;

    public Discount(int id, double value, String title) {
        this.id = id;
        this.value = value;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
