package app.touchlessChef.fragment.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import app.touchlessChef.R;
import app.touchlessChef.model.Ingredient;
import app.touchlessChef.model.Instruction;

public class RecipeViewFragment extends Fragment {
    private List<Ingredient> ingredientList;
    private List<Instruction> instructionList;

    public RecipeViewFragment() {
    }

    public static RecipeViewFragment newInstance(List<Ingredient> ingredients,
                                                 List<Instruction> instructions) {
        RecipeViewFragment fragment = new RecipeViewFragment();
        if (ingredients == null) ingredients = new ArrayList<>();
        if (instructions == null) instructions = new ArrayList<>();
        Bundle args = new Bundle();

        args.putParcelableArrayList("ingredients", (ArrayList<Ingredient>) ingredients);
        args.putParcelableArrayList("instructions", (ArrayList<Instruction>) instructions);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_recipe, container, false);

        Bundle args = getArguments();
        if (args != null) {
            ingredientList = args.getParcelableArrayList("ingredients");
            instructionList = args.getParcelableArrayList("instructions");
        }

        // linking view(tag) to fragment
        TextView recipeView = view.findViewById(R.id.recipe_holder);

        // code to design the format of the fragment
        for (Ingredient ingredient: ingredientList)
        {
            recipeView.append("\n");
            recipeView.append(ingredientList.indexOf(ingredient) + 1 +  "." + ingredient.getName());
            recipeView.append("\n");
        }

        for(Instruction instruction : instructionList)
        {
            recipeView.append("\n");
            recipeView.append(instruction.getContent());
            recipeView.append("\n");

        }

        return view;
    }
}
