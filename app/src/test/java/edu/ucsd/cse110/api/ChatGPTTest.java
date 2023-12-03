package edu.ucsd.cse110.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import edu.ucsd.cse110.server.services.chatgpt.*;


import org.junit.Test;


public class ChatGPTTest {
    private ChatGPTInterface mock;
    private ChatGPTInterface real;

    @Before
    public void setUp() {

    }

    @Test
    public void testChatGPTMockConstructor() throws IOException, InterruptedException, URISyntaxException {
        mock = new ChatGPTMock();
        assertNotNull(mock);
    }

    @Test
    public void testChatGPTMockSendPrompt() throws IOException, InterruptedException, URISyntaxException {
        mock = new ChatGPTMock(1);
        String[] results = mock.promptGPT("Lunch", "The ingredients I have are chicken, lemon, oil, garlic.");
        assertNotNull(results);
        assertEquals("Avocado Taco Bowls", results[0]);
        assertEquals("Ingredients:\n" + //
                "\n" + //
                "For the Taco Bowls:\n" + //
                "\n" + //
                "4 large avocados, ripe but still firm\n" + //
                "1 pound ground beef (or ground turkey, chicken, or a meat alternative for a vegetarian version)\n"
                + //
                "1 small onion, finely chopped\n" + //
                "2 cloves garlic, minced\n" + //
                "1 packet of taco seasoning mix or your own blend of taco spices (chili powder, cumin, paprika, oregano, salt, and pepper)\n"
                + //
                "1 cup cooked rice (white or brown)\n" + //
                "1 cup black beans, drained and rinsed\n" + //
                "1 cup corn kernels (fresh, frozen, or canned)\n" + //
                "1 cup diced tomatoes\n" + //
                "1 cup shredded lettuce\n" + //
                "1/2 cup shredded cheddar cheese (or your favorite cheese)\n" + //
                "1/4 cup sour cream (optional)\n" + //
                "Fresh cilantro, for garnish\n" + //
                "Salsa, for serving\n" + //
                "Instructions:\n" + //
                "\n" + //
                "Prepare the Avocado Bowls:\n" + //
                "Cut the avocados in half and remove the pits. Carefully scoop out some of the flesh from each half to create a small well, making sure not to pierce through the avocado skin. This will create space for your taco fillings.\n"
                + //
                "Cook the Meat:\n" + //
                "In a skillet, heat some oil over medium heat. Add the chopped onion and garlic and sauté until they are soft and fragrant.\n"
                + //
                "Add the ground meat and cook until it's browned and cooked through. Break it up into small pieces as it cooks.\n"
                + //
                "Add the taco seasoning and follow the package instructions or use your own blend of spices. Stir well and let it simmer for a few minutes. If using a seasoning packet, add the recommended amount of water.\n"
                + //
                "Prepare the Fillings:\n" + //
                "Cook the rice according to package instructions and set it aside.\n" + //
                "Warm the black beans and corn in a separate pot or microwave, then set them aside.\n" + //
                "Dice the tomatoes, shred the lettuce, and grate the cheese.\n" + //
                "Assemble the Taco Bowls:\n" + //
                "Start with a layer of cooked rice in each avocado half.\n" + //
                "Add a layer of the seasoned meat.\n" + //
                "Top with black beans, corn, diced tomatoes, shredded lettuce, and shredded cheese.\n" + //
                "Garnish with sour cream, fresh cilantro, and salsa, if desired.\n" + //
                "Serve:\n" + //
                "Carefully place the filled avocado halves on plates, and serve your Avocado Taco Bowls immediately. Enjoy!",
                results[1]);
    }

    @Test
    public void testChatGPTConstructor() throws IOException, InterruptedException, URISyntaxException {
        real = new ChatGPT();
        assertNotNull(real);

    }
    
    // Not testing real chat gpt functionality in automated unit tests
    //  @Test
    //  public void testChatGPT() throws IOException, InterruptedException, URISyntaxException {
    //      real = new ChatGPT();
    //      String[] results = real.promptGPT("Lunch", "The ingredients I have are chicken, lemon, oil, garlic.");
    //      assertNotNull(results[0]);
    //      assertNotNull(results[1]);
    //  }
}
