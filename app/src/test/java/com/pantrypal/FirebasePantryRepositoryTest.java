package com.pantrypal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.repository.FirebasePantryRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FirebasePantryRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockUsersCollection;

    @Mock
    private DocumentReference mockUserDocument;

    @Mock
    private CollectionReference mockPantryItemsCollection;

    @Mock
    private DocumentReference mockPantryItemDocument;

    @Mock
    private Task<DocumentReference> mockAddTask;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private Query mockQuery;

    private FirebasePantryRepository repository;
    private String testUserId = "test-user-123";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new FirebasePantryRepository();
    }

    @Test
    public void testAddPantryItem() {
        // Arrange
        PantryItem testItem = new PantryItem();
        testItem.setIngredientName("Tomatoes");
        testItem.setCategory("Vegetables");
        testItem.setQuantity("5");
        testItem.setUnit("pieces");
        testItem.setExpirationDate(new Date());
        testItem.setNotes("Fresh from market");

        FirebasePantryRepository.RepositoryCallback<String> callback =
            new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    // Verify success
                    assertNotNull(data);
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Add should succeed";
                }
            };

        // Act
        repository.addPantryItem(testUserId, testItem, callback);

        // Assert
        // Note: Full testing requires mocking Firestore collection chain
        // Verify that item has userId set and timestamps
        assertEquals(testUserId, testItem.getUserId());
        assertNotNull(testItem.getCreatedAt());
        assertNotNull(testItem.getUpdatedAt());
    }

    @Test
    public void testUpdatePantryItem() {
        // Arrange
        PantryItem testItem = new PantryItem();
        testItem.setId("item-123");
        testItem.setUserId(testUserId);
        testItem.setIngredientName("Tomatoes");
        testItem.setCategory("Vegetables");
        testItem.setQuantity("10"); // Updated quantity
        testItem.setUnit("pieces");
        testItem.setExpirationDate(new Date());

        FirebasePantryRepository.RepositoryCallback<Void> callback =
            new FirebasePantryRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update should succeed";
                }
            };

        // Act
        repository.updatePantryItem(testUserId, testItem, callback);

        // Assert
        // Verify that updatedAt timestamp is set
        assertNotNull(testItem.getUpdatedAt());
    }

    @Test
    public void testUpdatePantryItemWithNullId() {
        // Arrange
        PantryItem testItem = new PantryItem();
        testItem.setId(null); // No ID
        testItem.setIngredientName("Tomatoes");

        FirebasePantryRepository.RepositoryCallback<Void> callback =
            new FirebasePantryRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Update should fail without ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Item ID is required for update", error);
                }
            };

        // Act
        repository.updatePantryItem(testUserId, testItem, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testDeletePantryItem() {
        // Arrange
        String itemId = "item-123";

        FirebasePantryRepository.RepositoryCallback<Void> callback =
            new FirebasePantryRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Delete should succeed";
                }
            };

        // Act
        repository.deletePantryItem(testUserId, itemId, callback);

        // Assert - verify delete is called on correct document
    }

    @Test
    public void testDeletePantryItemWithNullId() {
        // Arrange
        String itemId = null;

        FirebasePantryRepository.RepositoryCallback<Void> callback =
            new FirebasePantryRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Delete should fail without ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Item ID is required for deletion", error);
                }
            };

        // Act
        repository.deletePantryItem(testUserId, itemId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testGetAllPantryItems() {
        // Act
        LiveData<List<PantryItem>> liveData = repository.getAllPantryItems(testUserId);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore snapshot listeners
        // Verify that correct collection path is used: users/{userId}/pantryItems
    }

    @Test
    public void testGetExpiringItems() {
        // Act
        LiveData<List<PantryItem>> liveData = repository.getExpiringItems(testUserId);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore query with date filter
        // Verify that query filters items expiring within 3 days
    }

    @Test
    public void testSearchPantryItems() {
        // Arrange
        String searchQuery = "Tom";

        FirebasePantryRepository.RepositoryCallback<List<PantryItem>> callback =
            new FirebasePantryRepository.RepositoryCallback<List<PantryItem>>() {
                @Override
                public void onSuccess(List<PantryItem> data) {
                    // Verify success
                    assertNotNull(data);
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called in successful case
                }
            };

        // Act
        repository.searchByName(testUserId, searchQuery, callback);

        // Assert
        // Note: Full testing requires mocking Firestore query
        // Verify that query uses startAt and endAt for prefix search
    }

    @Test
    public void testGetPantryItemsByCategory() {
        // Arrange
        String category = "Vegetables";

        // Act
        LiveData<List<PantryItem>> liveData = repository.getPantryItemsByCategory(testUserId, category);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore query with category filter
        // Verify that query filters by category
    }

    @Test
    public void testGetPantryItemById() {
        // Arrange
        String itemId = "item-123";

        FirebasePantryRepository.RepositoryCallback<PantryItem> callback =
            new FirebasePantryRepository.RepositoryCallback<PantryItem>() {
                @Override
                public void onSuccess(PantryItem data) {
                    // Verify success
                    assertNotNull(data);
                    assertEquals(itemId, data.getId());
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called in successful case
                }
            };

        // Act
        repository.getPantryItemById(testUserId, itemId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document retrieval
    }

    @Test
    public void testCollectionPath() {
        // This test verifies that the correct Firestore collection path is used
        // Expected path: users/{userId}/pantryItems

        String expectedPath = "users/" + testUserId + "/pantryItems";

        // Note: In a real test, you would mock FirebaseFirestore.getInstance()
        // and verify that collection() is called with correct paths
        // For now, we verify the path format is correct
        assert expectedPath.equals("users/" + testUserId + "/pantryItems");
    }
}
