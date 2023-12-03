package edu.ucsd.cse110.server.services.dalle;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import java.awt.Image;

public class DallETest {
    DallEInterface dalle;
    @Before
    public void init(){
        dalle = new DallEMock();
    }
    @Test
    public void testDallEConstructor() {
        dalle = new DallE();
        assertNotNull(dalle);
    }
    @Test
    public void testDallEMockConstructor() {
        assertNotNull(dalle);
    }
    @Test
    public void testDallEImageGeneration(){
        String recipeTitle = "Orange Juice";
        Image result = dalle.promptDallE(recipeTitle);
        assertNotNull(result);
    }
}
