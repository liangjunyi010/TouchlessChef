package app.touchlessChef.dao;

import static app.touchlessChef.constants.RecipeConstants.VIETNAM_DEFAULT_RECIPE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.touchlessChef.dao.recipe.IngredientDAO;
import app.touchlessChef.dao.recipe.InstructionDAO;
import app.touchlessChef.dao.recipe.RecipeDAO;
import app.touchlessChef.model.Ingredient;
import app.touchlessChef.model.Instruction;
import app.touchlessChef.model.Recipe;

public class SQLiteDbCRUD extends SQLiteOpenHelper {
    private IngredientDAO ingredientDAO;
    private InstructionDAO instructionDAO;
    private static final String TAG = SQLiteDbCRUD.class.getSimpleName();

    public SQLiteDbCRUD(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(InstructionDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(IngredientDAO.Config.CREATE_TABLE_STATEMENT);
        default_insert_recipe(db, VIETNAM_DEFAULT_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion +
                " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + InstructionDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeDAO.Config.TABLE_NAME);
        onCreate(db);
    }

    private void default_insert_recipe(SQLiteDatabase db, Recipe recipe) {
        ingredientDAO = new IngredientDAO(db);
        instructionDAO = new InstructionDAO(db);
        long recipeID = default_insert_helper(db, recipe.getName(), recipe.getCategory(),
                recipe.getDescription(), recipe.getImagePath(),
                recipe.getTime(), recipe.getMealType());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.setRecipeId(recipeID);
            ingredientDAO.insert(ingredient);
            Log.i("DAO", "Inserted " + ingredient);
        }
        for (Instruction instruction : recipe.getInstructions()) {
            instruction.setRecipeId(recipeID);
            instructionDAO.insert(instruction);
            Log.i("DAO", "Inserted " + instruction);
        }

    }

    private long default_insert_helper(SQLiteDatabase db, String name, String category,
                                       String description, String imagePath,
                                       String time, String mealType){
        ingredientDAO = new IngredientDAO(db);
        instructionDAO = new InstructionDAO(db);
        ContentValues values = new ContentValues();
        values.put(RecipeDAO.Config.KEY_NAME, name);
        values.put(RecipeDAO.Config.KEY_CATEGORY, category);
        values.put(RecipeDAO.Config.KEY_DESCRIPTION, description);
        values.put(RecipeDAO.Config.KEY_IMAGE_PATH, imagePath);
        values.put(RecipeDAO.Config.KEY_TIME, time);
        values.put(RecipeDAO.Config.KEY_MEAL_TYPE, mealType);
        return db.insert(RecipeDAO.Config.TABLE_NAME, null, values);
    }

}
