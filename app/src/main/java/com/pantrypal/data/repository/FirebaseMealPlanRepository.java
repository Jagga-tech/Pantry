package com.pantrypal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pantrypal.data.model.MealPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repository for managing meal plans in Firestore
 */
public class FirebaseMealPlanRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_MEAL_PLANS = "meal_plans";

    public FirebaseMealPlanRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Get all meal plans for a user
     */
    public LiveData<List<MealPlan>> getUserMealPlans(String userId) {
        MutableLiveData<List<MealPlan>> liveData = new MutableLiveData<>();

        db.collection(COLLECTION_MEAL_PLANS)
                .whereEqualTo("userId", userId)
                .orderBy("startDate", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        liveData.setValue(new ArrayList<>());
                        return;
                    }

                    if (snapshots != null) {
                        List<MealPlan> mealPlans = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            MealPlan mealPlan = doc.toObject(MealPlan.class);
                            if (mealPlan != null) {
                                mealPlan.setId(doc.getId());
                                mealPlans.add(mealPlan);
                            }
                        }
                        liveData.setValue(mealPlans);
                    }
                });

        return liveData;
    }

    /**
     * Get current active meal plan
     */
    public LiveData<MealPlan> getCurrentMealPlan(String userId) {
        MutableLiveData<MealPlan> liveData = new MutableLiveData<>();
        Date now = new Date();

        db.collection(COLLECTION_MEAL_PLANS)
                .whereEqualTo("userId", userId)
                .whereLessThanOrEqualTo("startDate", now)
                .whereGreaterThanOrEqualTo("endDate", now)
                .limit(1)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null || snapshots.isEmpty()) {
                        liveData.setValue(null);
                        return;
                    }

                    DocumentSnapshot doc = snapshots.getDocuments().get(0);
                    MealPlan mealPlan = doc.toObject(MealPlan.class);
                    if (mealPlan != null) {
                        mealPlan.setId(doc.getId());
                    }
                    liveData.setValue(mealPlan);
                });

        return liveData;
    }

    /**
     * Get a specific meal plan by ID
     */
    public void getMealPlanById(String mealPlanId, RepositoryCallback<MealPlan> callback) {
        db.collection(COLLECTION_MEAL_PLANS)
                .document(mealPlanId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        MealPlan mealPlan = documentSnapshot.toObject(MealPlan.class);
                        if (mealPlan != null) {
                            mealPlan.setId(documentSnapshot.getId());
                        }
                        callback.onSuccess(mealPlan);
                    } else {
                        callback.onFailure("Meal plan not found");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /**
     * Create a new meal plan
     */
    public void createMealPlan(MealPlan mealPlan, RepositoryCallback<String> callback) {
        db.collection(COLLECTION_MEAL_PLANS)
                .add(mealPlan.toFirestoreMap())
                .addOnSuccessListener(documentReference -> {
                    String mealPlanId = documentReference.getId();
                    callback.onSuccess(mealPlanId);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /**
     * Update an existing meal plan
     */
    public void updateMealPlan(MealPlan mealPlan, RepositoryCallback<Void> callback) {
        if (mealPlan.getId() == null || mealPlan.getId().isEmpty()) {
            callback.onFailure("Invalid meal plan ID");
            return;
        }

        mealPlan.setUpdatedAt(new Date());

        db.collection(COLLECTION_MEAL_PLANS)
                .document(mealPlan.getId())
                .set(mealPlan.toFirestoreMap())
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /**
     * Delete a meal plan
     */
    public void deleteMealPlan(String mealPlanId, RepositoryCallback<Void> callback) {
        db.collection(COLLECTION_MEAL_PLANS)
                .document(mealPlanId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /**
     * Add a recipe to a meal plan
     */
    public void addRecipeToMealPlan(String mealPlanId, String recipeId, int dayNumber,
                                    String mealType, RepositoryCallback<Void> callback) {
        getMealPlanById(mealPlanId, new RepositoryCallback<MealPlan>() {
            @Override
            public void onSuccess(MealPlan mealPlan) {
                mealPlan.addRecipe(recipeId, dayNumber, mealType);
                updateMealPlan(mealPlan, callback);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * Remove a recipe from a meal plan
     */
    public void removeRecipeFromMealPlan(String mealPlanId, String recipeId,
                                         RepositoryCallback<Void> callback) {
        getMealPlanById(mealPlanId, new RepositoryCallback<MealPlan>() {
            @Override
            public void onSuccess(MealPlan mealPlan) {
                mealPlan.removeRecipe(recipeId);
                updateMealPlan(mealPlan, callback);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * Callback interface for repository operations
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onFailure(String error);
    }
}
