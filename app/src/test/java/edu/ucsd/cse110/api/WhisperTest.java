package edu.ucsd.cse110.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.server.services.whisper.*;

public class WhisperTest {
    @Test
    public void testWhisperConstructor() {
        // try {
        //     WhisperInterface w = new Whisper();
        //     assertNotNull(w);
        // } catch(Exception e) {}
    }
    @Test
    public void testWhisperMockConstructor() {
        // try {
        //     WhisperInterface w = new WhisperMock();
        //     assertNotNull(w);
        // } catch(Exception e) {}
    }
    @Test
    public void testWhisperNoFileTranscribe() {
        // try {
        //     WhisperInterface w = new Whisper();
        //     File f = new File("src/main/java/edu/ucsd/cse110/api/assets/null.wav");
        //     String response = w.transcribe(f);
        //     assertEquals("!Exception", response);
        // }
        // catch (Exception e) {}
    }

    @Test
    public void testWhisperMockNoFileTranscribe() {
        try {
            WhisperInterface w = new WhisperMock();
            byte[] b = {2};
            String response = w.transcribe(b);
            assertEquals("!Exception", response);
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperMockMealTypeTranscribe() {
        try {
            WhisperInterface w = new WhisperMock();
            byte[] b = {0};
            String response = w.transcribe(b).toLowerCase();
            System.out.println(response);
            assertTrue(response, response.contains("lunch"));
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperMockIngredientListTrascribe() {
        try {
            WhisperInterface w = new WhisperMock();
            byte[] b = {1};
            String response = w.transcribe(b).toLowerCase();
            assertTrue(response,response.contains("banana") 
                    && response.contains("pepper") 
                    && response.contains("onion") 
                    && response.contains("green onion") 
                    && response.contains("salt") 
                    && response.contains("carrot"));
        }
        catch (Exception e) {}
    }
}