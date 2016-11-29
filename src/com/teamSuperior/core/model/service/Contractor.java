package com.teamSuperior.core.model.service;

import com.teamSuperior.core.connection.DBConnect;
import java.sql.ResultSet;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */

public class Contractor {
    private static int id;
    private String name;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public Contractor(String name, String address, String city, String zip, String phone, String email) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Contractor.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
