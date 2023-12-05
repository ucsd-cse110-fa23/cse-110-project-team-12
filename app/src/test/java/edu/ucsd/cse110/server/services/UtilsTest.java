package edu.ucsd.cse110.server.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import java.awt.image.BufferedImage;

public class UtilsTest {

    @Test
    public void testUnmarshalJson() {
        String jsonStringUser = "{\"_id\": \"xYz1234567890AbCdEfGhIjK\", \"username\": \"akshatjain\", \"password\": \"iloveCoding123\"}";
        UserSchema expectedUser = new UserSchema();
        expectedUser._id = "xYz1234567890AbCdEfGhIjK";
        expectedUser.username = "akshatjain";
        expectedUser.password = "iloveCoding123";

        UserSchema userResult = Utils.unmarshalJson(jsonStringUser, UserSchema.class);

        assertNotNull(userResult);
        assertEquals(expectedUser._id, userResult._id);
        assertEquals(expectedUser.username, userResult.username);
        assertEquals(expectedUser.password, userResult.password);

        String jsonStringRecipe = "{\"_id\": \"2389ru29542q9dr\", \"userId\": \"akshatjain\", \"mealType\": \"Breakfast\", \"ingredients\": \"Chicken, Beef, Oil\", \"timeCreated\": \"2023-07-15 18:29:45.123\", \"title\": \"Chicken and beef\", \"description\": \"add ingredients and mix together\", \"base64ImageEncoding\": \"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAwAB/9j/YgAAAABJRU5ErkJggg\\u003d\\u003d\"}";

        RecipeSchema expectedRecipe = new RecipeSchema();
        expectedRecipe._id = "2389ru29542q9dr";
        expectedRecipe.userId = "akshatjain";
        expectedRecipe.mealType = "Breakfast";
        expectedRecipe.ingredients = "Chicken, Beef, Oil";
        expectedRecipe.timeCreated = "2023-07-15 18:29:45.123";
        expectedRecipe.title = "Chicken and beef";
        expectedRecipe.description = "add ingredients and mix together";
        expectedRecipe.base64ImageEncoding = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAwAB/9j/YgAAAABJRU5ErkJggg==";

        RecipeSchema recipeResult = Utils.unmarshalJson(jsonStringRecipe, RecipeSchema.class);

        assertNotNull(recipeResult);
        assertEquals(expectedRecipe._id, recipeResult._id);
        assertEquals(expectedRecipe.userId, recipeResult.userId);
        assertEquals(expectedRecipe.mealType, recipeResult.mealType);
        assertEquals(expectedRecipe.ingredients, recipeResult.ingredients);
        assertEquals(expectedRecipe.timeCreated, recipeResult.timeCreated);
        assertEquals(expectedRecipe.title, recipeResult.title);
        assertEquals(expectedRecipe.description, recipeResult.description);
        assertEquals(expectedRecipe.base64ImageEncoding, recipeResult.base64ImageEncoding);

    }

    @Test
    public void testMarshalJson() {
        UserSchema user = new UserSchema();
        user._id = "xYz1234567890AbCdEfGhIjK";
        user.username = "akshatjain";
        user.password = "iloveCoding123";

        String expectedUserJson = "{\"_id\":\"xYz1234567890AbCdEfGhIjK\",\"username\":\"akshatjain\",\"password\":\"iloveCoding123\"}";
        String userJson = Utils.marshalJson(user);

        assertNotNull(userJson);
        assertEquals(expectedUserJson, userJson);

        RecipeSchema recipe = new RecipeSchema();
        recipe._id = "2389ru29542q9dr";
        recipe.userId = "akshatjain";
        recipe.mealType = "Breakfast";
        recipe.ingredients = "Chicken, Beef, Oil";
        recipe.timeCreated = "2023-07-15 18:29:45.123";
        recipe.title = "Chicken and beef";
        recipe.description = "add ingredients and mix together";
        recipe.base64ImageEncoding = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAwAB/9j/YgAAAABJRU5ErkJggg==";

        String expectedRecipeJson = "{\"_id\":\"2389ru29542q9dr\",\"userId\":\"akshatjain\",\"mealType\":\"Breakfast\",\"ingredients\":\"Chicken, Beef, Oil\",\"timeCreated\":\"2023-07-15 18:29:45.123\",\"title\":\"Chicken and beef\",\"description\":\"add ingredients and mix together\",\"base64ImageEncoding\":\"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAwAB/9j/YgAAAABJRU5ErkJggg\\u003d\\u003d\"}";
        String recipeJson = Utils.marshalJson(recipe);

        assertNotNull(recipeJson);

        assertEquals(expectedRecipeJson, recipeJson);
    }

    @Test
    public void testEncodeBase64() {
        byte[] inputBytes = "eric-huang-loves-programming".getBytes();
        String expectedBase64String = "ZXJpYy1odWFuZy1sb3Zlcy1wcm9ncmFtbWluZw==";

        String actualBase64String = Utils.encodeBase64(inputBytes);

        assertNotNull(actualBase64String);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test
    public void testDecodeBase64() {

        byte[] expectedBytes = "eric-huang-loves-programming".getBytes();

        String testbase64String = "ZXJpYy1odWFuZy1sb3Zlcy1wcm9ncmFtbWluZw==";
        byte[] actualBytes = Utils.decodeBase64(testbase64String);

        assertNotNull(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testEncodeBufferedImageToBase64() {
        // Create a sample BufferedImage (e.g., a 10x10 image)
        int width = 2;
        int height = 2;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        String expectedBase64 = "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAACAAIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD5/ooooA//2Q==";

        // Encode the BufferedImage to base64
        String actualBase64 = Utils.encodeBufferedImageToBase64(bufferedImage);
        assertNotNull(actualBase64);
        assertEquals(expectedBase64, actualBase64);
    }

    @Test
    public void testDecodeBase64ToBufferedImage() {
        int width = 2;
        int height = 2;
        BufferedImage expectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        String encodedImage = "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAACAAIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD5/ooooA//2Q==";

        BufferedImage actualImage = Utils.decodeBase64ToBufferedImage(encodedImage);

        assertNotNull(actualImage);
        assertEquals(width, actualImage.getWidth());
        assertEquals(height, actualImage.getHeight());
    }
}
