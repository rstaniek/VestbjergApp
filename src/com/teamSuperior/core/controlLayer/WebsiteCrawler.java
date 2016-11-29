package com.teamSuperior.core.controlLayer;


import com.teamSuperior.guiApp.GUI.AlertBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Domestos on 16.11.29.
 */
public class WebsiteCrawler {
    public static String getExchangeRatioUSD(){
        String value = null;
        try{
            Document doc = Jsoup.connect("https://finance.yahoo.com/quote/USDDKK=X?ltr=1").get();
            Elements e = doc.select("span").attr("data-reactid", "271");
            int i=1;
            for (Element element : e){
                if(i==15){
                    value = element.text();
                    break;
                }
                i++;
            }
        }
        catch (IOException ioex){
            AlertBox.display("Java IO Exception", ioex.getMessage());
        }
        catch (Exception ex){
            AlertBox.display("Unexpected exception", ex.getMessage());
        }
        return value;
    }
    public static String getExchangeRatioEUR(){
        String value = null;
        try{
            Document doc = Jsoup.connect("https://finance.yahoo.com/quote/EURDKK=X?ltr=1").get();
            Elements e = doc.select("span").attr("data-reactid", "271");
            int i=1;
            for (Element element : e){
                if(i==15){
                    value = element.text();
                    break;
                }
                i++;
            }
        }
        catch (IOException ioex){
            AlertBox.display("Java IO Exception", ioex.getMessage());
        }
        catch (Exception ex){
            AlertBox.display("Unexpected exception", ex.getMessage());
        }
        return value;
    }
    public static String getExchangeRatio(String websiteURL){
        String value = null;
        try{
            Document doc = Jsoup.connect(websiteURL).get();
            Elements e = doc.select("span").attr("data-reactid", "271");
            int i=1;
            for (Element element : e){
                if(i==15){
                    value = element.text();
                    break;
                }
                i++;
            }
        }
        catch (IOException ioex){
            //AlertBox.display("Java IO Exception", ioex.getMessage());
            value = "Error";
        }
        return value;
    }
}
