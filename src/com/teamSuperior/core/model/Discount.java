package com.teamSuperior.core.model;

import javax.persistence.*;

/**
 * Discount entity
 */
@Entity
@Table(name = "discounts")
public class Discount implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double value;
    private String title;

    public Discount() {
    }

    public Discount(int id, double value, String title) {
        this.id = id;
        this.value = value;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toJson() {
        return null;
    }
}
