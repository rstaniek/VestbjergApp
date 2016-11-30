package com.teamSuperior.core.controlLayer;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.teamSuperior.core.connection.DBConnect.*;
/**
 * Created by Smoothini on 29.11.2016.
 */
public class CustomerControl {

    /*public void addCustomer(String name, String surname, String street, String city, String zip, String email, String phone, String password){
        try{
            String query = "INSERT INTO employees ("
        }
    }*/


    public void viewCustomers(){
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM employees";
            resultSet = getFromDataBase(query);
            while(resultSet.next())
            {
                System.out.println("Name: " + resultSet.getString(2));
                System.out.println("Surname: " + resultSet.getString(3));
                System.out.println("Street: " + resultSet.getString(4));
                System.out.println("City: " + resultSet.getString(5));
                System.out.println("Zip: " + resultSet.getString(6));
                System.out.println("eMail: " + resultSet.getString(7));
                System.out.println("Phone: " +resultSet.getString(8));
                System.out.println("Password: " + resultSet.getString(9));
                System.out.println();
            }
            resultSet.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
