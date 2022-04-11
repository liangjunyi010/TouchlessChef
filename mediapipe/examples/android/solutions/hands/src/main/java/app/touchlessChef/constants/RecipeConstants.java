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
    public static final int DEFAULT_IMAGE = R.drawable.vn_botloc;

    private static Recipe loadVNDefault() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("1 Pound thinly sliced Chicken Breasts, or boneless chicken thighs"));
        ingredients.add(new Ingredient("1 Tablespoon Olive Oil"));
        ingredients.add(new Ingredient("¾ cup brown sugar"));
        ingredients.add(new Ingredient("⅓ cup soy sauce"));
        ingredients.add(new Ingredient("2 Tablespoons hoisin sauce"));
        ingredients.add(new Ingredient("1 Tablespoon Sweet Chili Sauce"));
        ingredients.add(new Ingredient("1 Tablespoons ginger, peeled and grated"));
        ingredients.add(new Ingredient("Pinch of dried red pepper flakes, to taste"));
        ingredients.add(new Ingredient("½ teaspoon minced garlic"));
        ingredients.add(new Ingredient("Juice of one lime"));

        List<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction("instruction1"));
        instructions.add(new Instruction("instruction2"));
        instructions.add(new Instruction("instruction3"));
        instructions.add(new Instruction("instruction4"));
        instructions.add(new Instruction("instruction5"));
        instructions.add(new Instruction("instruction6"));

        return new Recipe("vn recipe", "Vietnamese",
                "description", ingredients, instructions, "default",
                "Proper Meal", "30 mins");
    }}
