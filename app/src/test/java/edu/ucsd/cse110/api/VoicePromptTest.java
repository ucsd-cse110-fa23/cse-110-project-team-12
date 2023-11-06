package edu.ucsd.cse110.api;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

public class VoicePromptTest {
    @Test
    public void testVoicePromptSuccess() {
        try{
            VoicePrompt vp = new VoicePrompt("./voice2.wav");
            vp.startRecording();
            File f = vp.stopRecording();
            assertNotNull(f);
        }
        catch (Exception e) {}
    }

    @Test
    public void TestMocks() {
        VoicePromptMock vpm = new VoicePromptMock(VoicePromptMock.PromptType.MealType);
        vpm.startRecording();
        File f = vpm.stopRecording();
        assertTrue(f.exists());
    }
}
