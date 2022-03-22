package app.touchlessChef.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.touchlessChef.dao.recipe.IngredientDAO;
import app.touchlessChef.dao.recipe.InstructionDAO;
import app.touchlessChef.dao.recipe.RecipeDAO;

public class SQLiteDbCRUD extends SQLiteOpenHelper {
    private static final String TAG = SQLiteDbCRUD.class.getSimpleName();

    public SQLiteDbCRUD(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(UserDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(RecipeDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(InstructionDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(IngredientDAO.Config.CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion +
                " to " + newVersion + ", which will destroy all old data.");
//        db.execSQL("DROP TABLE IF EXISTS " + UserDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InstructionDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeDAO.Config.TABLE_NAME);
        onCreate(db);
    }


}
