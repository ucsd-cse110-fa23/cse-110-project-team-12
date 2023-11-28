package edu.ucsd.cse110.api;

import static org.junit.Assert.assertEquals;

import javafx.scene.Node;
import javafx.scene.Parent;

import org.junit.Test;

public class ControllerTest {
    // @Test
    // public void TestMessagePassing() {
    //     Controller c = new Controller(false, null, null, null);
    //     Model m = new Model();
    //     c.addModel(Controller.ModelType.CreateRecipe, m);
    //     UI ui = new UI(c);
    //     c.addUI(Controller.UIType.CreateRecipe, ui);
    //     assertEquals(12, m.getValue());
    // }
}

class UI implements UIInterface {
    UI(Controller c) {
        c.receiveMessageFromUI(new Message(Message.HomeModel.CloseCreateRecipeView));
    }

    @Override
    public void receiveMessage(Message m) {
    }

    @Override
    public void addChild(Node n) {}

    @Override
    public void removeChild(Node n) {}

    @Override
    public Parent getUI() {
        return null;
    }
}

class Model implements ModelInterface {
    private int value;

    public int getValue() {
        return value;
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeModel.CloseCreateRecipeView)
            value = 12;
    }

    @Override
    public Object getState() {
        return null;
    }
}
