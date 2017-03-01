package com.teamSuperior.core.testingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by rajmu on 17.03.01.
 */
public class JsonReader {

    public static Collection data(String path) throws IOException, ParseException, URISyntaxException {
        ArrayList<Object[]> data = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject rawJson = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(new URL(path).openStream())));
        Object[] keys = rawJson.keySet().toArray();
        for (Object key : keys) {
            JSONObject json = (JSONObject) rawJson.get(key);
            data.add(new Object[]{json});
        }
        return data;
    }
}
