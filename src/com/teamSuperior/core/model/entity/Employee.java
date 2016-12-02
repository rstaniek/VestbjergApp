package com.teamSuperior.core.model.entity;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.permission.Level1;
import com.teamSuperior.core.model.permission.Level2;
import com.teamSuperior.core.model.permission.Level3;
import com.teamSuperior.guiApp.GUI.AlertBox;
import static com.teamSuperior.core.connection.DBConnect.*;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Employee{

    private Level1 accessLevel1;
    private Level2 accessLevel2;
    private Level3 accessLevel3;
    private int id;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String zip;
    private String email;
    private String phone;
    private String password;
    private String position;
    private int numberOfSales;
    private double totalRevenue;
    private int accessLevel;
    public Employee(int id, String name, String surname, String address, String city, String zip, String email, String phone, String password, String position, int numberOfSales, double totalRevenue, int accessLevel) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.position = position;
        this.numberOfSales = numberOfSales;
        this.totalRevenue = totalRevenue;
        this.accessLevel = accessLevel;
        accessLevel1 = new Level1();
        accessLevel2 = new Level2();
        accessLevel3 = new Level3();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
