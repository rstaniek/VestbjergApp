package com.teamSuperior.core;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Domestos on 16.12.20.
 */
public class Utils {
    public static boolean isNumeric(String s){
        if (!s.isEmpty()) {
            try {
                double d = Double.parseDouble(s);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }

    public static <T> String arrayToString(ArrayList<T> array){
        String result = "";
        if(array.size() != 0){
            for(T element : array){
                result += element + ",";
            }
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

    public static ArrayList<Integer> stringToArray(String s){
        ArrayList<Integer> result = new ArrayList<>();
        String[] tmp = s.split(",");
        for (String s1 : tmp){
            result.add(Integer.parseInt(s1));
        }
        return result;
    }

    /***
     * checks if the offer is still valid and usable
     * @return VALID if valid and EXPIRED when expired
     */
    public static String isExpired(Date expirationDate){
        if(expirationDate.before(Date.valueOf(LocalDate.now()))){
            return "EXPIRED";
        } else {
            return "VALID";
        }
    }

    public static boolean isValidOffer(Date date){
        return !date.before(Date.valueOf(LocalDate.now()));
    }
}
