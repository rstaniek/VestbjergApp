package com.teamSuperior.core.model.entity;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public abstract class Person {
    private String name, surname, street, city, zip, email, phoneNo, password;

    public Person(String name, String surname, String street, String city, String zip, String email, String phoneNo, String password) {
        this.name = name;
        this.surname = surname;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPassword() {
        return password;
    }
}
//SOME COMMENT
