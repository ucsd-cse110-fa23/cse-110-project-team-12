package edu.ucsd.cse110.story;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIFactory;
import edu.ucsd.cse110.api.VoicePromptMock;

public class MS2US5Test {
    
    Controller controller;

    /*
     * Given I am not in the PantryPal app
     * And the PantryPal server is down
     * When I click to open the PantryPal app
     * Then I am shown an info page saying that the PantryPal server is unavailable.
     */
    @Test
    public void testUS5BDD1() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

        // PantryPal server is down
        controller.receiveMessageFromModel(new Message(Message.HttpRequest.ServerError));

        // shown an info page saying that the PantryPal server is unavailable
        assertNotNull(controller.existsUI(UIFactory.Type.ServerErrorPopup));
    }
}
