package edu.ucsd.cse110.api;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.ucsd.cse110.server.services.whisper.*;

public class WhisperTest {
    @Test
    public void testWhisperConstructor() {
        try {
            WhisperInterface w = new Whisper();
            assertNotNull(w);
        } catch(Exception e) {}
    }
    @Test
    public void testWhisperMockConstructor() {
        try {
            WhisperInterface w = new WhisperMock();
            assertNotNull(w);
        } catch(Exception e) {}
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
    public void testWhisperMockValidMealTypeTranscribe() {
        try {
            WhisperInterface w = new WhisperMock();
            File f = new File("src/main/java/edu/ucsd/cse110/api/assets/iwantlunch.wav");
            byte[] fileBinary = Files.readAllBytes(f.toPath());
            String response = w.transcribe(fileBinary);
            assertTrue(response.contains("lunch"));
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperMockInvalidNotMealTypeTranscribe() {
        try {
            WhisperInterface w = new WhisperMock();
            File f = new File("src/main/java/edu/ucsd/cse110/api/assets/helloiwouldlikesupperplease.wav");
            byte[] fileBinary = Files.readAllBytes(f.toPath());
            String response = w.transcribe(fileBinary);
            assertTrue(!response.contains("breakfast") && !response.contains("lunch") && !response.contains("dinner"));
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperMockInvalidMultipleMealTypeTranscribe() {
        try {
            WhisperInterface w = new WhisperMock();
            File f = new File("src/main/java/edu/ucsd/cse110/api/assets/iwantlunchanddinner.wav");
            byte[] fileBinary = Files.readAllBytes(f.toPath());
            String response = w.transcribe(fileBinary);
            assertTrue(response.contains("lunch") && response.contains("dinner"));
        }
        catch (Exception e) {}
    }

    @Test
    public void testWhisperMockIngredientListTrascribe() {
        try {
            WhisperInterface w = new WhisperMock();
            File f = new File("./src/main/java/edu/ucsd/cse110/api/assets/ingredients.wav");
            byte[] fileBinary = Files.readAllBytes(f.toPath());
            String response = w.transcribe(fileBinary).toLowerCase();
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