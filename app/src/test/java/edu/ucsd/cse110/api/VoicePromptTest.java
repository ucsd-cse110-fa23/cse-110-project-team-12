package edu.ucsd.cse110.api;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

public class VoicePromptTest {
    @Test
    public void testVoicePromptConstructor(){
        VoicePromptInterface vp = new VoicePrompt("./voice2.wav");
        assertNotNull(vp);
    }
    @Test
    public void testVoicePromptMockConstructor(){
        VoicePromptInterface vp = new VoicePromptMock(VoicePromptMock.PromptType.MealType);
        assertNotNull(vp);
    }
    @Test
    public void testVoicePromptSuccess() {
        try{
            VoicePromptInterface vp = new VoicePrompt("./voice2.wav");
            vp.startRecording();
            File f = vp.stopRecording();
            assertNotNull(f);
        }
        catch (Exception e) {}
    }

    @Test
    public void testVoicePromptMockSuccess() {
        VoicePromptInterface vpm = new VoicePromptMock(VoicePromptMock.PromptType.MealType);
        vpm.startRecording();
        File f = vpm.stopRecording();
        assertTrue(f.exists());
    }
}
