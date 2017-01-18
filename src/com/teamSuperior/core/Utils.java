package com.teamSuperior.core;

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
}
