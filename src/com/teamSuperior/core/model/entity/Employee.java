package com.teamSuperior.core.model.entity;

import com.teamSuperior.core.model.permission.Level1;
import com.teamSuperior.core.model.permission.Level2;
import com.teamSuperior.core.model.permission.Level3;

import java.text.NumberFormat;
import java.util.Locale;

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
    private NumberFormat formatter;
    private Locale loc;
    private int numberOfSales;
    private double totalRevenue;
    private int accessLevel;
    private String numberOfSales_str;
    private String totalRevenue_str;
    private String accessLevel_str;
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
        loc = new Locale("da","DK");
        formatter = NumberFormat.getInstance(loc);
        numberOfSales_str = String.valueOf(numberOfSales);
        totalRevenue_str = String.valueOf("kr " + formatter.format(totalRevenue));
        accessLevel_str = String.valueOf(accessLevel);
        accessLevel1 = new Level1();
        accessLevel2 = new Level2();
        accessLevel3 = new Level3();
    }

    public String getNumberOfSales_str() {
        return numberOfSales_str;
    }

    public String getTotalRevenue_str() {
        return totalRevenue_str;
    }

    public String getAccessLevel_str() {
        return accessLevel_str;
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

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public int getNumberOfSales() {
        return numberOfSales;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getAccessLevel() {
        return accessLevel;
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

    public void setPassword(String password) { this.password = password; }

    private void setLocale(String lang) {
        loc = new Locale(lang);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", position='" + position + '\'' +
                ", formatter=" + formatter +
                ", loc=" + loc +
                ", numberOfSales=" + numberOfSales +
                ", totalRevenue=" + totalRevenue +
                ", accessLevel=" + accessLevel +
                ", numberOfSales_str='" + numberOfSales_str + '\'' +
                ", totalRevenue_str='" + totalRevenue_str + '\'' +
                ", accessLevel_str='" + accessLevel_str + '\'' +
                '}';
    }
}
