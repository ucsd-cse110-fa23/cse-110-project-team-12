package edu.ucsd.cse110.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.client.NoUI;

public class FactoriesTest {

    Controller controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

    @Test
    public void testModelFactoryMakeCreateRecipeModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.CreateRecipe, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof CreateRecipeModel);
    }

    @Test
    public void testModelFactoryMakeRecipeDetailedModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.DetailedView, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof RecipeDetailedModel);
    }

    @Test
    public void testModelFactoryMakeSharePopupModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.SharePopup, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof SharePopupModel);
    }

    @Test
    public void testModelFactoryMakeCreateAccountModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.CreateAccount, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof CreateAccountModel);
    }

    @Test
    public void testModelFactoryMakeLogInModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.LogIn, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof LogInModel);
    }

    @Test
    public void testModelFactoryMakeNoMatchingType() {

        ModelInterface mi = ModelFactory.make(null, controller);
        assertNull(mi);
    }

    @Test
    public void testUIFactoryMakeNoMatchingType() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(null, controller);
        assertNull(mi);
    }

    @Test
    public void testUIFactoryMakeNoUI() {

        controller.useUI = false;

        UIInterface mi = UIFactory.make(null, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof NoUI);
    }
}