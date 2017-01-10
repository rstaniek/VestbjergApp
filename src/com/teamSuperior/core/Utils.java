package com.teamSuperior.core;

/**
 * Created by Domestos on 16.12.20.
 */
public class Utils {
    public static boolean isNumeric(String s){
        try{
            double d = Double.parseDouble(s);
        }
        catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }
}
