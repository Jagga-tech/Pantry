package com.pantrypal.data.repository;

import android.app.Application;

import com.pantrypal.data.dao.IngredientDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.Ingredient;

import java.util.List;

public class IngredientRepository {
    private IngredientDao ingredientDao;

    public IngredientRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        ingredientDao = db.ingredientDao();
    }

    public void insert(Ingredient ingredient) {
        new Thread(() -> ingredientDao.insert(ingredient)).start();
    }

    public void update(Ingredient ingredient) {
        new Thread(() -> ingredientDao.update(ingredient)).start();
    }

    public void delete(Ingredient ingredient) {
        new Thread(() -> ingredientDao.delete(ingredient)).start();
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient>[] ingredients = new List[1];
        Thread thread = new Thread(() -> ingredients[0] = ingredientDao.getAllIngredients());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingredients[0];
    }

    public List<Ingredient> getIngredientsByCategory(String category) {
        List<Ingredient>[] ingredients = new List[1];
        Thread thread = new Thread(() -> ingredients[0] = ingredientDao.getIngredientsByCategory(category));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingredients[0];
    }

    public List<Ingredient> searchIngredients(String query) {
        List<Ingredient>[] ingredients = new List[1];
        Thread thread = new Thread(() -> ingredients[0] = ingredientDao.searchIngredients(query));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingredients[0];
    }

    public List<String> getAllCategories() {
        List<String>[] categories = new List[1];
        Thread thread = new Thread(() -> categories[0] = ingredientDao.getAllCategories());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categories[0];
    }
}