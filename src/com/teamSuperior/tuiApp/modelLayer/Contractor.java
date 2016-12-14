package com.teamSuperior.tuiApp.modelLayer;

import java.io.Serializable;

/**
 * Contractor model class.
 */
public class Contractor implements Serializable {
    private int id;
    private String name, address, city, zip, phone, email;

    public Contractor(int id, String name, String address, String city, String zip, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
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

    @Override
    public String toString() {
        return String.format(
                "ID: %d%nName: %s%nAddress: %s%nCity: %s%nZIP: %s%nPhone: %s%nEmail: %s%n%n",
                id, name, address, city, zip, phone, email
        );
    }
}
