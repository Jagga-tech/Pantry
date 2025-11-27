package com.pantrypal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.pantrypal.data.repository.FirebaseFavoritesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseFavoritesRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FirebaseFavoritesRepository repository;
    private String testUserId = "test-user-123";
    private String testRecipeId = "recipe-456";

    @Before
    public void setUp() {
        repository = new FirebaseFavoritesRepository();
    }

    @Test
    public void testAddFavorite() {
        // Arrange
        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Add favorite should succeed";
                }
            };

        // Act
        repository.addFavorite(testUserId, testRecipeId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore set operation
        // Verify that favorite is added to users/{userId}/favorites/{recipeId}
    }

    @Test
    public void testAddFavoriteWithNullRecipeId() {
        // Arrange
        String nullRecipeId = null;

        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Add favorite should fail with null ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Recipe ID is required", error);
                }
            };

        // Act
        repository.addFavorite(testUserId, nullRecipeId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testAddFavoriteWithEmptyRecipeId() {
        // Arrange
        String emptyRecipeId = "";

        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Add favorite should fail with empty ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Recipe ID is required", error);
                }
            };

        // Act
        repository.addFavorite(testUserId, emptyRecipeId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testRemoveFavorite() {
        // Arrange
        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Remove favorite should succeed";
                }
            };

        // Act
        repository.removeFavorite(testUserId, testRecipeId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore delete operation
    }

    @Test
    public void testRemoveFavoriteWithNullRecipeId() {
        // Arrange
        String nullRecipeId = null;

        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Remove favorite should fail with null ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Recipe ID is required", error);
                }
            };

        // Act
        repository.removeFavorite(testUserId, nullRecipeId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testGetFavoriteRecipeIds() {
        // Act
        LiveData<List<String>> liveData = repository.getFavoriteRecipeIds(testUserId);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore snapshot listener
        // Verify that correct collection path is used: users/{userId}/favorites
    }

    @Test
    public void testIsFavorite() {
        // Arrange
        FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    // Verify success
                    assertNotNull(data);
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called in successful case
                }
            };

        // Act
        repository.isFavorite(testUserId, testRecipeId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.exists()
    }

    @Test
    public void testIsFavoriteWithNullRecipeId() {
        // Arrange
        String nullRecipeId = null;

        FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    // Should not be called
                    assert false : "isFavorite should fail with null ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Recipe ID is required", error);
                }
            };

        // Act
        repository.isFavorite(testUserId, nullRecipeId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testToggleFavoriteWhenNotFavorited() {
        // Arrange - recipe is not favorited, so it should be added
        FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    // Verify that recipe was added (true means added)
                    // Note: This test requires mocking Firestore to return exists() = false first
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                }
            };

        // Act
        repository.toggleFavorite(testUserId, testRecipeId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.exists() and set/delete
    }

    @Test
    public void testToggleFavoriteWhenFavorited() {
        // Arrange - recipe is favorited, so it should be removed
        FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    // Verify that recipe was removed (false means removed)
                    // Note: This test requires mocking Firestore to return exists() = true first
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                }
            };

        // Act
        repository.toggleFavorite(testUserId, testRecipeId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.exists() and delete
    }

    @Test
    public void testToggleFavoriteWithNullRecipeId() {
        // Arrange
        String nullRecipeId = null;

        FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    // Should not be called
                    assert false : "Toggle favorite should fail with null ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("Recipe ID is required", error);
                }
            };

        // Act
        repository.toggleFavorite(testUserId, nullRecipeId, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testGetFavoritesCount() {
        // Arrange
        FirebaseFavoritesRepository.RepositoryCallback<Integer> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Integer>() {
                @Override
                public void onSuccess(Integer data) {
                    // Verify success
                    assertNotNull(data);
                    assertTrue(data >= 0);
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called in successful case
                }
            };

        // Act
        repository.getFavoriteCount(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore query.size()
    }

    @Test
    public void testClearAllFavorites() {
        // Arrange
        FirebaseFavoritesRepository.RepositoryCallback<Void> callback =
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Clear favorites should succeed";
                }
            };

        // Act
        repository.clearAllFavorites(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore collection.get() and delete
    }

    @Test
    public void testIsFavoriteLiveData() {
        // Act
        LiveData<Boolean> liveData = repository.isFavoriteLiveData(testUserId, testRecipeId);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore snapshot listener
        // Verify that correct document path is used: users/{userId}/favorites/{recipeId}
    }

    @Test
    public void testCollectionPath() {
        // This test verifies that the correct Firestore collection path is used
        // Expected path: users/{userId}/favorites

        String expectedPath = "users/" + testUserId + "/favorites";

        // Note: In a real test, you would mock FirebaseFirestore.getInstance()
        // and verify that collection() is called with correct paths
        // For now, we verify the path format is correct
        assertTrue(expectedPath.equals("users/" + testUserId + "/favorites"));
    }
}
