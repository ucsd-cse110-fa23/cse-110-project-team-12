package edu.ucsd.cse110.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.client.CreateAccountView;
import edu.ucsd.cse110.client.CreateRecipeView;
import edu.ucsd.cse110.client.HomeView;
import edu.ucsd.cse110.client.LogInView;
import edu.ucsd.cse110.client.NoUI;
import edu.ucsd.cse110.client.RecipeDetailedView;
import edu.ucsd.cse110.client.SharePopupView;

public class FactoriesTest {

    Controller controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

    @Test
    public void testModelFactoryMakeCreateRecipeModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.CreateRecipe, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof CreateRecipeModel);
    }

    @Test
    public void testModelFactoryMakeCreateHomeModel() {

        ModelInterface mi = ModelFactory.make(ModelFactory.Type.HomePage, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof HomeModel);
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
    public void testUIFactoryMakeCreateRecipeView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.CreateRecipe, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof CreateRecipeView);
    }

    @Test
    public void testUIFactoryMakeHomeView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.HomePage, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof HomeView);
    }

    @Test
    public void testUIFactoryMakeRecipeDetailedView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.DetailedView, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof RecipeDetailedView);
    }

    @Test
    public void testUIFactoryMakeSharePopupView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.SharePopup, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof SharePopupView);
    }

    @Test
    public void testUIFactoryMakeCreateAccountView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.CreateAccount, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof CreateAccountView);
    }

    @Test
    public void testUIFactoryMakeLogInView() {

        controller.useUI = true;

        UIInterface mi = UIFactory.make(UIFactory.Type.LogIn, controller);
        assertNotNull(mi);
        assertTrue(mi instanceof LogInView);
    }

    @Test
    public void testUIFactoryMakeNoMatchingType() {

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