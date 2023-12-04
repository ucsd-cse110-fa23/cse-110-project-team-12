package edu.ucsd.cse110.api;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;

public class MessageTest {
    @Test
    public void TestCreateValidMessage() {
        Message m = new Message(Message.HomeView.UpdateRecipeList);  
        Message m1 = new Message(Message.CreateRecipeModel.CreateRecipeGotoPage,
            "PageType", "",
            "MealType", "FFF");
        assertNotNull(m);
        assertNotNull(m1);
    }

    @Test
    public void TestExistsException() {
        int count = 0;
        try {
            new Message(Message.HomeView.OpenRecipe);
        }
        catch (Exception e) {
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void TestExistExceptionArgs() {
        int count = 0;
        try {
            new Message(Message.CreateRecipeModel.CreateRecipeGotoPage, "BAD KEY", "");
        }
        catch (Exception e) {
            count++;
        }
        assertEquals(1, count);
    }
}
