package app.touchlessChef.activity.standard.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


import androidx.annotation.DrawableRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;


import app.touchlessChef.R;
import app.touchlessChef.activity.standard.MenuActivity;
import app.touchlessChef.adapter.DatabaseAdapter;
import app.touchlessChef.constants.RecipeConstants;
import app.touchlessChef.fragment.home.ChineseFragment;
import app.touchlessChef.fragment.home.VietnamFragment;
import app.touchlessChef.model.Recipe;
import app.touchlessChef.fragment.home.BaseFragment;
import app.touchlessChef.activity.standard.recipe.CreateRecipeActivity;
import app.touchlessChef.activity.standard.recipe.ViewRecipeActivity;
import app.touchlessChef.constants.RecipeEditConstants;

public class MainActivity extends MenuActivity implements BaseFragment.FragmentListener {
    private static final int REQUEST_ADD_RECIPE = 1;
    private static final int REQUEST_VIEW_RECIPE = 2;
    private DatabaseAdapter databaseAdapter;

    private ImageView firstView;
    private ImageView secondView;
    private ViewSwitcher mViewSwitcher;
    private DrawerLayout drawer;
    private String currentCategory;
    private final Activity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_home_main);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        firstView = findViewById(R.id.firstView);
        int defaultImage = R.drawable.vn_botloc;
        firstView.setImageResource(defaultImage);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                VietnamFragment.newInstance(RecipeConstants.VIETNAMESE)).commit();
        secondView = findViewById(R.id.secondView);
        mViewSwitcher = findViewById(R.id.switcher);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(font);
        mCollapsingToolbarLayout.setExpandedTitleTypeface(font);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            @DrawableRes int image = -1;
            switch (item.toString()) {
                case RecipeConstants.VIETNAMESE:
                    currentCategory = RecipeConstants.VIETNAMESE;
                    image = R.drawable.vn_botloc;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                            VietnamFragment.newInstance(RecipeConstants.VIETNAMESE)).commit();
                    break;
                case RecipeConstants.CHINESE:
                    currentCategory = RecipeConstants.CHINESE;
                    image = R.drawable.chn_dumpling;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                            ChineseFragment.newInstance(RecipeConstants.CHINESE)).commit();
                    break;
            }
            if (firstView.getVisibility() == View.VISIBLE) {
                secondView.setImageResource(image);
                mViewSwitcher.showNext();
            } else {
                firstView.setImageResource(image);
                mViewSwitcher.showPrevious();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_recipe) {
            Intent intent = new Intent(this, CreateRecipeActivity.class);
            intent.putExtra("category", currentCategory);
            mActivity.startActivityForResult(intent, REQUEST_ADD_RECIPE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD_RECIPE:
                switch (resultCode) {
                    case RecipeEditConstants.RECIPE_ADDED:
                        Snackbar.make(getWindow().getDecorView(), "Recipe added.",
                                Snackbar.LENGTH_LONG).show();
                        break;
                    case RecipeEditConstants.RECIPE_EDITED:
                        Snackbar.make(getWindow().getDecorView(), "Recipe modified.",
                                Snackbar.LENGTH_LONG).show();
                        break;
                }
                break;
            case REQUEST_VIEW_RECIPE:
                switch (resultCode) {
                    case RecipeEditConstants.RECIPE_SHOULD_BE_EDITED:
                        Recipe recipe = data.getParcelableExtra("recipe");
                        Intent intent = new Intent(this, CreateRecipeActivity.class);
                        intent.putExtra("recipe", recipe);
                        intent.putExtra("category", recipe.getCategory());
                        intent.putExtra("isEditing", true);
                        mActivity.startActivityForResult(intent, REQUEST_ADD_RECIPE);
                        break;
                    case RecipeEditConstants.RECIPE_SHOULD_BE_DELETED:
                        long recipeId = data.getLongExtra("recipeId", -1);
                        if (recipeId != -1) {
                            onDeleteRecipe(recipeId);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onShowRecipe(Recipe recipe, Pair<ImageView, String> pairs) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("recipe", recipe);

        ActivityOptions transitionActivityOptions =
                ActivityOptions.makeSceneTransitionAnimation(
                        mActivity, pairs.first, pairs.second);
        mActivity.startActivityForResult(
                intent, REQUEST_VIEW_RECIPE, transitionActivityOptions.toBundle());
    }

    @Override
    public void onDeleteRecipe(long recipeId) {
        databaseAdapter.deleteRecipe(recipeId);
        Snackbar.make(getWindow().getDecorView(), "Recipe deleted.", Snackbar.LENGTH_LONG).show();
    }
}

