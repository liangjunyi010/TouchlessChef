package app.touchlessChef.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.touchlessChef.constants.RecipeConstants;
import app.touchlessChef.model.Recipe;
import app.touchlessChef.fragment.view.ViewIngredientsFragment;
import app.touchlessChef.fragment.view.ViewInstructionsFragment;

public class RecipePagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_COUNT = 2;
    private final Recipe recipe;


    public RecipePagerAdapter(FragmentManager fm, Recipe recipe) {
        super(fm);
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = ViewIngredientsFragment.newInstance(recipe.getIngredients());
                break;
            case 1:
                frag = ViewInstructionsFragment.newInstance(recipe.getInstructions());
                break;
        }
        assert frag != null;
        return frag;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = RecipeConstants.INGREDIENTS;
                break;
            case 1:
                title = RecipeConstants.INSTRUCTIONS;
                break;
        }
        return title;
    }
}
