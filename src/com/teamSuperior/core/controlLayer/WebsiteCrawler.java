package com.teamSuperior.core.controlLayer;


import com.teamSuperior.guiApp.GUI.AlertBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Domestos on 16.11.29.
 */
public class WebsiteCrawler {
    public static String retrieveData(String websiteURL, String elementID){
        String value = null;
        try{
            Document doc = Jsoup.connect(websiteURL).get();
            //TODO: finish data crawler
            value =  doc.body().getElementById(elementID).data(); //shit ain't working here
        }
        catch (IOException ioex){
            AlertBox.display("Java IO Exception", ioex.getMessage());
        }
        return value;
    }
}
