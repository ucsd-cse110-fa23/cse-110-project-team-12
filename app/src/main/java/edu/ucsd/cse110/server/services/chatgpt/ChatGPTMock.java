package edu.ucsd.cse110.server.services.chatgpt;

// Mock ChatGPT to use for testing, construced with integer to set the model response
public class ChatGPTMock implements ChatGPTInterface {
	private int desiredResponse;

	/**
	 * default constructor
	 */
	public ChatGPTMock() {
		this.desiredResponse = 1;
	}

	/**
	 * 
	 * @param desiredResponse which given response you want
	 */
	public ChatGPTMock(int desiredResponse) {
		this.desiredResponse = desiredResponse;
	}

	/**
	 * Fake prompt to ChatGPT
	 * 
	 * @param mealType    mealType is not used but needs to be passed in to match
	 *                    the interface
	 * @param ingredients ingredients is not used but needs to be passed in to match
	 *                    the interface
	 * 
	 * @return String[] array whose first entry is the generated name and
	 *         whose second entry is the rest of the generated text
	 */
	public String[] promptGPT(String mealType, String ingredients) {
		String name = "";
		String information = "";

		if (desiredResponse == 1) {
			name = "Avocado Taco Bowls";
			information = "Ingredients:\n" + //
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
					"Carefully place the filled avocado halves on plates, and serve your Avocado Taco Bowls immediately. Enjoy!";
		}
		if (desiredResponse == 2) {
			name = "Chopped Cobb Salad";
			information = "Ingredients:\n" + //
					"2 cups romaine lettuce, chopped\n" + //
					"2 hard-boiled eggs, chopped\n" + //
					"2 boneless, skinless chicken breasts, grilled and diced\n" + //
					"1 cup cherry tomatoes, halved\n" + //
					"1/2 cup crumbled blue cheese\n" + //
					"1 avocado, diced\n" + //
					"4 slices of bacon, cooked and crumbled\n" + //
					"1/4 cup red onion, finely chopped\n" + //
					"Your favorite salad dressing (e.g., ranch or blue cheese)\n" + //
					"Instructions:\n" + //
					"\n" + //
					"Prepare the Ingredients:\n" + //
					"Grill the chicken breasts until they are fully cooked, then dice them into bite-sized pieces.\n" + //
					"Cook the bacon until it's crispy, then crumble it into small bits.\n" + //
					"Hard-boil the eggs, cool them, and chop them into small pieces.\n" + //
					"Halve the cherry tomatoes, chop the avocado, crumble the blue cheese, and finely chop the red onion.\n"
					+ //
					"Assemble the Salad:\n" + //
					"In a large salad bowl or on individual serving plates, arrange a layer of chopped romaine lettuce.\n"
					+ //
					"Arrange the Toppings:\n" + //
					"In rows or sections on top of the lettuce, arrange the diced chicken, hard-boiled egg, cherry tomatoes, avocado, blue cheese, crumbled bacon, and chopped red onion.\n"
					+ //
					"Serve:\n" + //
					"Serve your Chopped Cobb Salad with your favorite salad dressing on the side, allowing everyone to drizzle dressing over their salad as desired.";
		}

		String[] result = { name, information };
		return result;
	}
}
