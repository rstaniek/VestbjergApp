package com.teamSuperior.core.model.entity;

import com.teamSuperior.core.model.permission.Level1;
import com.teamSuperior.core.model.permission.Level2;
import com.teamSuperior.core.model.permission.Level3;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Employee extends Person {

    private Level1 accessLevel1;
    private Level2 accessLevel2;
    private Level3 accessLevel3;



    public Employee(String name, String surname, String street, String city, String zip, String email, String phoneNo, String password) {
        super(name, surname, street, city, zip, email, phoneNo, password);
        accessLevel1 = new Level1();
        accessLevel2 = new Level2();
        accessLevel3 = new Level3();
    }


}
