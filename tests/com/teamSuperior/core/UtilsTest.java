package com.teamSuperior.core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static com.teamSuperior.core.Utils.*;
import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.02.28.
 */
public class UtilsTest {
    private Random random;

    @Before
    public void setUp() throws Exception {
        random = new Random();
    }

    @Test
    public void testIsNumeric() throws Exception {
        assertTrue(isNumeric(String.valueOf(random.nextDouble())));
        assertTrue(isNumeric(String.valueOf(random.nextInt())));
        assertTrue(isNumeric(String.valueOf(random.nextFloat())));

        assertFalse(isNumeric("asd325"));
        assertFalse(isNumeric("2345asf"));
        assertFalse(isNumeric("asghk"));
    }

    @Test
    public void testIsInteger() throws Exception {
        assertTrue(isInteger(String.valueOf(random.nextInt())));

        assertFalse(isInteger(String.valueOf(random.nextFloat())));
        assertFalse(isInteger(String.valueOf(random.nextDouble())));
        assertFalse(isInteger("asda"));
    }

    @Test
    public void testArrayToString() throws Exception {
        ArrayList<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(2);
        input.add(3);
        assertEquals("Integers", "1,2,3", arrayToString(input));

        ArrayList<Double> input2 = new ArrayList<>();
        input2.add(1.0);
        input2.add(4.2);
        input2.add(6.9);
        assertEquals("Fixed point numeric", "1.0,4.2,6.9", arrayToString(input2));

        ArrayList<Integer> input3 = new ArrayList<>();
        assertEquals("Empty array", "", arrayToString(input3));
    }

    @Ignore("Not yet implemented. Need to figure out the test data")
    @Test
    public void testStringToArray() throws Exception {
        //TODO: to be implemented
    }
}