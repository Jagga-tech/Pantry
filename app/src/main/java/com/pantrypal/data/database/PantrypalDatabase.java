package com.pantrypal.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pantrypal.data.dao.IngredientDao;
import com.pantrypal.data.dao.PantryItemDao;
import com.pantrypal.data.dao.RecipeDao;
import com.pantrypal.data.dao.UserDao;
import com.pantrypal.data.mock.MockPantryData;
import com.pantrypal.data.mock.MockRecipeData;
import com.pantrypal.data.mock.MockUserData;
import com.pantrypal.data.model.Ingredient;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.model.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, PantryItem.class, Recipe.class, Ingredient.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class PantrypalDatabase extends RoomDatabase {
    private static final String TAG = "PantrypalDB";
    
    public abstract UserDao userDao();
    public abstract PantryItemDao pantryItemDao();
    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();

    private static PantrypalDatabase INSTANCE;

    public static PantrypalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PantrypalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PantrypalDatabase.class, "pantrypal_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Populate with mock data on background thread to avoid lock conflicts
                                    Log.d(TAG, "🔧 Database created - scheduling initial mock data load");
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        Log.d(TAG, "🔧 Loading initial mock data...");
                                        populateMockData(INSTANCE);
                                    });
                                }

                                @Override
                                public void onOpen(SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    // Clear and reload mock data on every open (for testing)
                                    Log.d(TAG, "🔓 Database opened - scheduling mock data reload");
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        Log.d(TAG, "🔓 Reloading mock data...");
                                        clearAndReloadMockData(INSTANCE);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populateMockData(PantrypalDatabase database) {
        try {
            // Insert mock user
            User mockUser = MockUserData.getMockUser();
            database.userDao().insert(mockUser);
            Log.d(TAG, "✅ Inserted 1 mock user");

            // Insert mock recipes
            java.util.List<Recipe> recipes = MockRecipeData.getAllRecipes();
            for (Recipe recipe : recipes) {
                database.recipeDao().insert(recipe);
            }
            Log.d(TAG, "✅ Inserted " + recipes.size() + " mock recipes");

            // Insert mock pantry items
            java.util.List<PantryItem> items = MockPantryData.getAllPantryItems();
            for (PantryItem item : items) {
                database.pantryItemDao().insert(item);
            }
            Log.d(TAG, "✅ Inserted " + items.size() + " mock pantry items");
            Log.d(TAG, "✅ Mock data population complete!");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error populating mock data: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private static void clearAndReloadMockData(PantrypalDatabase database) {
        try {
            // Delete all existing data
            Log.d(TAG, "🗑️  Clearing existing data...");
            database.getOpenHelper().getWritableDatabase().execSQL("DELETE FROM recipes");
            database.getOpenHelper().getWritableDatabase().execSQL("DELETE FROM pantry_items");
            database.getOpenHelper().getWritableDatabase().execSQL("DELETE FROM users");
            Log.d(TAG, "🗑️  Data cleared");

            // Reload mock data
            populateMockData(database);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error clearing and reloading mock data: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
}