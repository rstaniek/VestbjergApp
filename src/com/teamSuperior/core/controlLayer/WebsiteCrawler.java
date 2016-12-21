package com.teamSuperior.core.controlLayer;


import com.teamSuperior.core.enums.Currency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Domestos on 16.11.29.
 */
public class WebsiteCrawler {

    public static String getExchangeRatioBloomberg(Currency c) {
        String value = null;
        String websiteURL = String.format("https://www.bloomberg.com/quote/%1$s:CUR", c.getCurrency());
        try{
            Document doc = Jsoup.connect(websiteURL).get();
            Elements elements = doc.select("div.price");
            value = elements.get(0).text();
        }
        catch (IOException ex){
            value = "Error";
        }
        return value;
    }
}
