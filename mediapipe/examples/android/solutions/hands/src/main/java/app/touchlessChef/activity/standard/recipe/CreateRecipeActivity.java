package app.touchlessChef.activity.standard.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import app.touchlessChef.R;
import app.touchlessChef.adapter.DatabaseAdapter;
import app.touchlessChef.model.Ingredient;
import app.touchlessChef.model.Instruction;
import app.touchlessChef.model.Recipe;
import app.touchlessChef.fragment.recipe.NavigableFragment;
import app.touchlessChef.fragment.recipe.RecipeImageFragment;
import app.touchlessChef.fragment.recipe.RecipeIngredientFragment;
import app.touchlessChef.fragment.recipe.RecipeInstructionFragment;
import app.touchlessChef.constants.RecipeEditConstants;

public class CreateRecipeActivity extends AppCompatActivity implements
        RecipeImageFragment.ImageListener, RecipeInstructionFragment.InstructionListener,
        RecipeIngredientFragment.IngredientListener{

    private static final int REQUEST_OPEN_GALLERY = 10;
    private static final int REQUEST_TO_ACCESS_GALLERY = 11;

    private Recipe currentRecipe;
    private boolean isEditing;
    private DatabaseAdapter databaseAdapter;

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_create);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        Intent intent = getIntent();
        isEditing = intent.getBooleanExtra("isEditing", false);
        if (isEditing)
            currentRecipe = intent.getParcelableExtra("recipe");
        else {
            String currentCategory = intent.getStringExtra("category");
            currentRecipe = new Recipe(currentCategory);
        }

        initializeUI();
        displayFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment instanceof RecipeIngredientFragment)
            displayFragment(0);
        else if (fragment instanceof RecipeInstructionFragment)
            displayFragment(1);
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri imageData = data.getData();
                String imageSrc = Files.getRealPathFromURI(this, imageData);
                ((RecipeImageFragment) Objects.requireNonNull(getSupportFragmentManager()
                        .findFragmentById(R.id.frame_container))).onImageSelected(imageSrc);

                currentRecipe.setImagePath(imageSrc);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_TO_ACCESS_GALLERY) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openGallery();
            else
                Toast.makeText(this, "Permission denied to access the gallery.", Toast.LENGTH_LONG)
                        .show();
        }
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        String nextButtonText = "";

        switch (position) {
            case 0:
                fragment = RecipeImageFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.left_slide_in, R.animator.right_slide_out);
                nextButtonText = "NEXT";
                break;
            case 1:
                fragment = RecipeIngredientFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.right_slide_in, R.animator.left_slide_out);
                nextButtonText = "NEXT";
                break;
            case 2:
                fragment = RecipeInstructionFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.right_slide_in, R.animator.left_slide_out);
                nextButtonText = "Finish";
                break;
        }

        nextButton.setText(nextButtonText);

        assert fragment != null;
        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    public void onNext(View view) {
        ((NavigableFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.frame_container))).onNext();
    }

    private void initializeUI() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nextButton = findViewById(R.id.nextButton);
    }

    @Override
    public void navigateToIngredientsFragment(String name, String description) {
        currentRecipe.setName(name);
        currentRecipe.setDescription(description);
        displayFragment(1);
    }

    @Override
    public void navigateToInstructionsFragment(List<Ingredient> ingredients) {
        currentRecipe.setIngredients(ingredients);
        displayFragment(2);
    }

    @Override
    public void onSelectImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_TO_ACCESS_GALLERY);
        } else {
            openGallery();
        }
    }

    @Override
    public void onStepsFinished(List<Instruction> directions) {
        currentRecipe.setInstructions(directions);
        if (isEditing)
            databaseAdapter.updateRecipe(currentRecipe);
        else
            databaseAdapter.addNewRecipe(currentRecipe);

        Log.i("CreateRecipeActivity", "Final recipe: " + currentRecipe);
        setResult(isEditing ? RecipeEditConstants.RECIPE_EDITED : RecipeEditConstants.RECIPE_ADDED);
        finish();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUEST_OPEN_GALLERY);
    }

    public static class Files {
        public static String getRealPathFromURI(Context context, Uri contentURI) {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(contentURI);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
        }
    }
}