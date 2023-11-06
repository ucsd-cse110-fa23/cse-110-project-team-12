package edu.ucsd.cse110.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import java.io.File;

public class WhisperTest {
    @Test
    public void testWhisperMealTypeTranscribe() {
        try{
            Whisper w = new Whisper();
            File f = new File("src/main/java/edu/ucsd/cse110/api/assets/iwantlunch.wav");
            String response = w.transcribe(f).toLowerCase();
            assertTrue(response.contains("lunch"));
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperIngredientListTrascribe() {
        try{
            Whisper w = new Whisper();
            File f = new File("src/main/java/edu/ucsd/cse110/api/assets/ingredients.wav");
            String response = w.transcribe(f).toLowerCase();
            assertTrue(response.contains("banana") 
                    && response.contains("pepper") 
                    && response.contains("onion") 
                    && response.contains("green onion") 
                    && response.contains("salt") 
                    && response.contains("carrot"));
        }
        catch (Exception e) {}
    }
}
