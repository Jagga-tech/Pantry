package com.pantrypal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pantrypal.data.model.PantryItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirebasePantryRepository {

    private final FirebaseFirestore firestore;
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_PANTRY_ITEMS = "pantryItems";

    public FirebasePantryRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for async operations
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    /**
     * Get collection reference for user's pantry items
     */
    private CollectionReference getPantryItemsCollection(String userId) {
        return firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(COLLECTION_PANTRY_ITEMS);
    }

    /**
     * Get all pantry items with real-time updates
     */
    public LiveData<List<PantryItem>> getAllPantryItems(String userId) {
        return getPantryItemsByUser(userId);
    }

    /**
     * Get pantry items by user with real-time updates
     */
    public LiveData<List<PantryItem>> getPantryItemsByUser(String userId) {
        MutableLiveData<List<PantryItem>> liveData = new MutableLiveData<>();

        getPantryItemsCollection(userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        liveData.setValue(new ArrayList<>());
                        return;
                    }

                    if (snapshots != null) {
                        List<PantryItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            PantryItem item = doc.toObject(PantryItem.class);
                            item.setId(doc.getId());
                            items.add(item);
                        }
                        liveData.setValue(items);
                    }
                });

        return liveData;
    }

    /**
     * Get expiring items (within 3 days) with real-time updates
     */
    public LiveData<List<PantryItem>> getExpiringItems(String userId) {
        MutableLiveData<List<PantryItem>> liveData = new MutableLiveData<>();

        // Calculate date 3 days from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date threeDaysFromNow = calendar.getTime();

        getPantryItemsCollection(userId)
                .whereLessThanOrEqualTo("expirationDate", threeDaysFromNow)
                .orderBy("expirationDate", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        liveData.setValue(new ArrayList<>());
                        return;
                    }

                    if (snapshots != null) {
                        List<PantryItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            PantryItem item = doc.toObject(PantryItem.class);
                            item.setId(doc.getId());
                            items.add(item);
                        }
                        liveData.setValue(items);
                    }
                });

        return liveData;
    }

    /**
     * Search pantry items by name
     */
    public void searchByName(String userId, String searchQuery, RepositoryCallback<List<PantryItem>> callback) {
        getPantryItemsCollection(userId)
                .orderBy("ingredientName")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PantryItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PantryItem item = doc.toObject(PantryItem.class);
                        item.setId(doc.getId());
                        items.add(item);
                    }
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Search failed");
                });
    }

    /**
     * Add new pantry item
     */
    public void addPantryItem(String userId, PantryItem item, RepositoryCallback<String> callback) {
        item.setUserId(userId);
        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());

        getPantryItemsCollection(userId)
                .add(item.toFirestoreMap())
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to add item");
                });
    }

    /**
     * Create or update pantry item
     */
    public void createPantryItem(PantryItem item, RepositoryCallback<Void> callback) {
        if (item.getUserId() == null || item.getUserId().isEmpty()) {
            callback.onFailure("User ID is required");
            return;
        }

        if (item.getId() == null || item.getId().isEmpty()) {
            // Create new item
            item.setCreatedAt(new Date());
            item.setUpdatedAt(new Date());
            getPantryItemsCollection(item.getUserId())
                    .add(item.toFirestoreMap())
                    .addOnSuccessListener(documentReference -> {
                        callback.onSuccess(null);
                    })
                    .addOnFailureListener(e -> {
                        callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to create item");
                    });
        } else {
            // Update existing item
            item.setUpdatedAt(new Date());
            getPantryItemsCollection(item.getUserId())
                    .document(item.getId())
                    .set(item.toFirestoreMap())
                    .addOnSuccessListener(aVoid -> {
                        callback.onSuccess(null);
                    })
                    .addOnFailureListener(e -> {
                        callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update item");
                    });
        }
    }

    /**
     * Update existing pantry item
     */
    public void updatePantryItem(String userId, PantryItem item, RepositoryCallback<Void> callback) {
        if (item.getId() == null || item.getId().isEmpty()) {
            callback.onFailure("Item ID is required for update");
            return;
        }

        item.setUpdatedAt(new Date());

        getPantryItemsCollection(userId)
                .document(item.getId())
                .update(item.toFirestoreMap())
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update item");
                });
    }

    /**
     * Delete pantry item
     */
    public void deletePantryItem(String userId, String itemId, RepositoryCallback<Void> callback) {
        if (itemId == null || itemId.isEmpty()) {
            callback.onFailure("Item ID is required for deletion");
            return;
        }

        getPantryItemsCollection(userId)
                .document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to delete item");
                });
    }

    /**
     * Get pantry item by ID
     */
    public void getPantryItemById(String userId, String itemId, RepositoryCallback<PantryItem> callback) {
        getPantryItemsCollection(userId)
                .document(itemId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        PantryItem item = documentSnapshot.toObject(PantryItem.class);
                        if (item != null) {
                            item.setId(documentSnapshot.getId());
                            callback.onSuccess(item);
                        } else {
                            callback.onFailure("Failed to parse item");
                        }
                    } else {
                        callback.onFailure("Item not found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to get item");
                });
    }

    /**
     * Get pantry items by category
     */
    public LiveData<List<PantryItem>> getPantryItemsByCategory(String userId, String category) {
        MutableLiveData<List<PantryItem>> liveData = new MutableLiveData<>();

        getPantryItemsCollection(userId)
                .whereEqualTo("category", category)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        liveData.setValue(new ArrayList<>());
                        return;
                    }

                    if (snapshots != null) {
                        List<PantryItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            PantryItem item = doc.toObject(PantryItem.class);
                            item.setId(doc.getId());
                            items.add(item);
                        }
                        liveData.setValue(items);
                    }
                });

        return liveData;
    }
}
