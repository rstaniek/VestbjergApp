package com.teamSuperior.core.model.entity;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;

/**
 * Customer entity
 */
@Entity
@Table(name = "customers")
public class Customer implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int salesMade;
    private double totalSpent;
    private String name, surname, address, city, zip, email, phone;

    public Customer() {
    }

    public Customer(int id, int salesMade, double totalSpent, String name, String surname, String address, String city, String zip, String email, String phone) {
        this.id = id;
        this.salesMade = salesMade;
        this.totalSpent = totalSpent;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalesMade() {
        return salesMade;
    }

    public void setSalesMade(int salesMade) {
        this.salesMade = salesMade;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return id + " - " + name + " " + surname;
    }

    @Override
    public String toJson() {
        return null;
    }
}
