package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.modelLayer.Product;
import com.teamSuperior.tuiApp.modelLayer.ProductContainer;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class AddTestingData {
    public AddTestingData(){
        ProductContainer.getInstance().getProducts().add(new Product(1, "biscuit", "cock sucker", 12312, "food", 20, "fakta", 123, 1234));
        ProductContainer.getInstance().getProducts().add(new Product(2, "dildo", "giana mouse", 18512, "food", 69, "futex", 1, 72));
        ProductContainer.getInstance().getProducts().add(new Product(3, "devils dick", "best shit ever", 66666, "leisure objects", 0, "hell", 666, 23422));

    }
}
