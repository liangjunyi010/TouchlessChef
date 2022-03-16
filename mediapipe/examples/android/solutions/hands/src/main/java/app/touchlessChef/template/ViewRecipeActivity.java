package app.touchlessChef.template;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

import app.touchlessChef.adapter.DatabaseAdapter;
import app.touchlessChef.adapter.ViewPagerAdapter;
import app.touchlessChef.model.Recipe;
import app.touchlessChef.R;
import app.touchlessChef.utils.RecipeValues;

public class ViewRecipeActivity extends MenuActivity {
    private ViewPagerAdapter myAdapter;
    private Recipe currentRecipe;
    private DatabaseAdapter databaseAdapter;

    private ImageView myRecipeImage;
    private TextView myRecipeDescription;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private CollapsingToolbarLayout myCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_view_recipe);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        currentRecipe = getIntent().getParcelableExtra("recipe");
        databaseAdapter = DatabaseAdapter.getInstance(this);
        findViewsById();

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(currentRecipe.getName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myRecipeImage.setImageURI(Uri.fromFile(new File(currentRecipe.getImagePath())));
        myRecipeDescription.setText(currentRecipe.getDescription());

        myTabLayout.bringToFront();
        myAdapter = new ViewPagerAdapter(getSupportFragmentManager(), currentRecipe);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        myCollapsingToolbarLayout.setCollapsedTitleTypeface(font);
        myCollapsingToolbarLayout.setExpandedTitleTypeface(font);

        myViewPager.setAdapter(myAdapter);
        myTabLayout.setupWithViewPager(myViewPager);
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTabLayout));
        myTabLayout.setTabsFromPagerAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_recipe:
                Intent intent = new Intent();
                intent.putExtra("recipe", currentRecipe);
                setResult(RecipeValues.RECIPE_SHOULD_BE_EDITED, intent);
                finish();
                break;
            case R.id.delete_recipe:
                Intent data = new Intent();
                data.putExtra("recipeId", currentRecipe.getId());
                setResult(RecipeValues.RECIPE_SHOULD_BE_DELETED, data);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViewsById() {
        myRecipeImage = findViewById(R.id.recipe_image);
        myRecipeDescription = findViewById(R.id.recipe_description);
        myToolbar = findViewById(R.id.toolbar);
        myViewPager = findViewById(R.id.viewpager);
        myTabLayout = findViewById(R.id.tablayout);
        myCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
    }
}