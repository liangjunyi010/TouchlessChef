package app.touchlessChef.dao.recipe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.touchlessChef.model.Instruction;

public class InstructionDAO {
    private SQLiteDatabase db;

    public InstructionDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(Instruction instruction) {
        insert(instruction.getContent(), instruction.getRecipeId());
    }

    public void insert(String instruction, long recipeId) {
        ContentValues values = new ContentValues();
        values.put(Config.KEY_BODY, instruction);
        values.put(Config.KEY_RECIPE_ID, recipeId);
        db.insert(Config.TABLE_NAME, null, values);
    }

    public List<Instruction> selectAllByRecipeId(long recipeId) {
        List<Instruction> instructions = new ArrayList<>();
        try (Cursor cursor = db.query(Config.TABLE_NAME,
                new String[]{Config.KEY_ID, Config.KEY_BODY, Config.KEY_RECIPE_ID},
                Config.KEY_RECIPE_ID + " = ?", new String[]{recipeId + ""}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    instructions.add(new Instruction(
                            cursor.getLong(0), cursor.getString(1), cursor.getLong(2)));
                } while (cursor.moveToNext());
            }
        }

        Log.i("DAO", "InstructionDAO returning: " + instructions + " for recipe ID: " + recipeId);
        return instructions;
    }

    public boolean deleteAllByRecipeId(long recipeId) {
        return db.delete(Config.TABLE_NAME, Config.KEY_RECIPE_ID + "=" + recipeId, null) > 0;
    }

    public static class Config {
        public static final String TABLE_NAME = "instructions";
        public static final String KEY_ID = "id";
        public static final String KEY_BODY = "body";
        public static final String KEY_RECIPE_ID = "recipeId";

        public static final String CREATE_TABLE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_BODY + " TEXT NOT NULL, " +
                        KEY_RECIPE_ID + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + KEY_RECIPE_ID + ") REFERENCES " + RecipeDAO.Config.TABLE_NAME + "(" + RecipeDAO.Config.KEY_ID + "))";
    }
}
