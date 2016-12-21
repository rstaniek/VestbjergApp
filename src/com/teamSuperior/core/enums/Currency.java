package com.teamSuperior.core.enums;

/**
 * Created by Domestos on 16.12.21.
 */
public enum Currency {
    USDDKK("USDDKK"),
    EURDKK("EURDKK"),
    PLNDKK("PLNDKK");

    private String currency;

    Currency(String currency){
        this.currency = currency;
    }

    public String getCurrency(){
        return currency;
    }
}
