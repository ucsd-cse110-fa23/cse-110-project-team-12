package edu.ucsd.cse110.api;

import java.util.Map;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class SharePopupModel implements ModelInterface {
    Controller controller;
    String shareLink;
    public SharePopupModel(Controller controller) {
        this.controller = controller;
    }
    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.RecipeDetailedModel.SetRecipeShareLink) {
			shareLink = (String) m.getKey("RecipeShareLink");
            controller.receiveMessageFromModel(new Message(Message.SharePopupModel.SetRecipeShareLink,
                                                    Map.ofEntries(Map.entry("RecipeShareLink", shareLink))));
		}
        else if (m.getMessageType() == Message.SharePopupView.CloseButton) {
            this.controller.receiveMessageFromModel(new Message(Message.SharePopupModel.CloseSharePopupView));
        }
        else if (m.getMessageType() == Message.SharePopupView.ClipboardButton) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(shareLink);
            clipboard.setContent(content);
        }
    }
}
