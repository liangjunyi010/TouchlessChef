package app.touchlessChef.constants;

import java.util.ArrayList;
import java.util.List;

import app.touchlessChef.R;
import app.touchlessChef.model.Ingredient;
import app.touchlessChef.model.Instruction;
import app.touchlessChef.model.Recipe;

public class RecipeConstants {
    public static final String VIETNAMESE = "Vietnamese";
    public static final String CHINESE = "Chinese";

    public static final Recipe VIETNAM_DEFAULT_RECIPE = loadVNDefault();
    public static final int DEFAULT_IMAGE = R.drawable.vn_chicken_rice;

    private static Recipe loadVNDefault() {
        // Reference: https://tasty.co/recipe/hainanese-chicken-rice
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("3 lb whole chicken(1.3 kg), giblets removed"));
        ingredients.add(new Ingredient("¼ cup kosher salt(60 g), divided"));
        ingredients.add(new Ingredient("4 inch pieces fresh ginger, peeled and cut into ¼-inch (6 mm) slices"));
        ingredients.add(new Ingredient("1 bunch fresh scallion"));
        ingredients.add(new Ingredient("1 gal cold water(3.7 L), plus more as needed"));
        ingredients.add(new Ingredient("2 tablespoons sesame oil"));

        List<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction("1. To clean the chicken, rub all over with a handful of kosher salt, getting rid of any loose skin. Rinse the chicken well inside and out. Pat dry with paper towels."));
        instructions.add(new Instruction("2. Remove any excess fat from the chicken and set aside for later."));
        instructions.add(new Instruction("3. Season the chicken generously with salt. Stuff the chicken cavity with the ginger slices and scallions."));
        instructions.add(new Instruction("4. Place the chicken in a large stock pot, cover with cold water by 1 inch (2 cm), and season with salt to taste."));
        instructions.add(new Instruction("5. Bring to a boil over high heat, then immediately reduce the heat to low to maintain a simmer. Cover and cook for about 30 minutes, or until the internal temperature of the chicken reaches 165°F (75°C). Remove the pot from the heat."));
        instructions.add(new Instruction("6. Remove the chicken from the pot, reserving the poaching liquid for later, and transfer to an ice bath for 5 minutes to stop the cooking process and to keep the chicken skin springy. Discard the ginger and green onion."));
        instructions.add(new Instruction("7. After it’s cooled, pat the chicken dry with paper towels and rub all over with sesame oil. This will help prevent the chicken from drying out."));
        instructions.add(new Instruction("8. In a large wok or skillet, heat ¼ cup (60 ml) of sesame oil over medium-high heat. Add 2 tablespoons of reserved chopped chicken fat, the garlic, ginger, and salt, and fry until aromatic, about 10 minutes."));
        instructions.add(new Instruction("9. Reserve ¼ of the fried garlic mixture, then add the rice to the remaining fried garlic and stir to coat. Cook for 3 minutes."));
        instructions.add(new Instruction("10. Transfer the rice to a rice cooker and add 2 cups (480 ml) of reserved poaching broth. Steam the rice for 60 minutes, or until tender."));
        instructions.add(new Instruction("11. While the rice is cooking, carve the chicken for serving."));
        instructions.add(new Instruction("12. Make the chili sauce: combine the sambal, Sriracha, sugar, garlic, ginger, lime juice, and chicken broth in a small bowl and stir to incorporate."));
        instructions.add(new Instruction("13. Make the ginger garlic sauce: in a small bowl, combine the ginger, garlic, salt, peanut oil, and rice vinegar, and stir to incorporate."));
        instructions.add(new Instruction("14. Make the soy sauce: in a small bowl, combine the reserved fried garlic and ginger with the oyster sauce, dark soy sauce, light soy sauce, and chicken broth, and stir to incorporate."));
        instructions.add(new Instruction("15. Serve the sliced chicken with the rice, dipping sauces, sliced cucumbers, and fresh cilantro."));
        instructions.add(new Instruction("16. Enjoy!"));



        return new Recipe("Vietnamese Chicken Rice", "Vietnamese",
                "For 6 people", ingredients, instructions, "default",
                "Main-course", "1 hr");
    }}
