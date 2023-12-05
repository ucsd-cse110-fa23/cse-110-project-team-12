package edu.ucsd.cse110.api;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class VoicePromptTest {
    @Test
    public void testVoicePromptConstructor() {
        VoicePromptInterface vp = new VoicePrompt("./voice2.wav");
        assertNotNull(vp);
    }
    @Test
    public void testVoicePromptMockConstructor() {
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.MealType);
        VoicePromptInterface vp = new VoicePromptMock(promptTypes);
        assertNotNull(vp);
    }
    @Test
    public void testVoicePromptSuccess() {
        try {
            // VoicePromptInterface vp = new VoicePrompt("./voice2.wav");
            // vp.startRecording();
            // File f = vp.stopRecording();
            // assertNotNull(f);
        }
        catch (Exception e) {}
    }

    @Test
    public void testVoicePromptMockSuccess() {
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.MealType);
        VoicePromptInterface vpm = new VoicePromptMock(promptTypes);
        vpm.startRecording();
        File f = vpm.stopRecording();
        assertTrue(f.exists());
    }
}
