package com.teamSuperior.core;

import com.teamSuperior.core.testingUtilities.JsonReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by rajmu on 17.03.01.
 */
@RunWith(Parameterized.class)
public class UtilsTest_isNumeric {

    @Parameterized.Parameter
    public JSONObject json;

    @Parameterized.Parameters
    public static Collection data() throws IOException, ParseException, URISyntaxException {
        return JsonReader.data("http://remix1436.ct8.pl/resources/misc/isNumericData.json");
    }

    @Test
    public void test() {
        System.out.println("isNumeric: string[" + json.get("string") + "]");
        System.out.println("isNumeric: expected[" + json.get("expected") + "]");
        assertEquals(String.format("isNumeric (%s)", json.get("string")), json.get("expected"), Utils.isNumeric((String) json.get("string")));
    }
}
