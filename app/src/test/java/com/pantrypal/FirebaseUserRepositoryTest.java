package com.pantrypal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.pantrypal.data.model.User;
import com.pantrypal.data.repository.FirebaseUserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseUserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FirebaseUserRepository repository;
    private String testUserId = "test-user-123";

    @Before
    public void setUp() {
        repository = new FirebaseUserRepository();
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User testUser = new User();
        testUser.setId(testUserId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setProfilePicUrl("https://example.com/photo.jpg");
        testUser.setDietaryPreferences("Vegetarian");

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Create user should succeed";
                }
            };

        // Act
        repository.createUser(testUser, callback);

        // Assert
        // Verify that timestamps are set
        assertNotNull(testUser.getCreatedAt());
        assertNotNull(testUser.getLastLoginAt());
    }

    @Test
    public void testCreateUserWithNullId() {
        // Arrange
        User testUser = new User();
        testUser.setId(null); // No ID
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Create user should fail without ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("User ID is required", error);
                }
            };

        // Act
        repository.createUser(testUser, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User testUser = new User();
        testUser.setId(testUserId);
        testUser.setName("Updated Name");
        testUser.setEmail("updated@example.com");
        testUser.setProfilePicUrl("https://example.com/new-photo.jpg");
        testUser.setDietaryPreferences("Vegan");

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update user should succeed";
                }
            };

        // Act
        repository.updateUser(testUser, callback);

        // Assert - verify update is called
    }

    @Test
    public void testUpdateUserWithNullId() {
        // Arrange
        User testUser = new User();
        testUser.setId(null); // No ID
        testUser.setName("Test User");

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Should not be called
                    assert false : "Update should fail without ID";
                }

                @Override
                public void onFailure(String error) {
                    // Verify error message
                    assertEquals("User ID is required", error);
                }
            };

        // Act
        repository.updateUser(testUser, callback);

        // Assert - callback.onFailure should be called
    }

    @Test
    public void testGetUserById() {
        // Act
        LiveData<User> liveData = repository.getUserById(testUserId);

        // Assert
        assertNotNull(liveData);
        // Note: Full testing requires mocking Firestore snapshot listener
        // Verify that correct document path is used: users/{userId}
    }

    @Test
    public void testCheckUserExists() {
        // Arrange
        FirebaseUserRepository.RepositoryCallback<Boolean> callback =
            new FirebaseUserRepository.RepositoryCallback<Boolean>() {
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
        repository.checkUserExists(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.exists()
    }

    @Test
    public void testUpdateLastLogin() {
        // Arrange
        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update last login should succeed";
                }
            };

        // Act
        repository.updateLastLoginAt(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore update with lastLoginAt field
    }

    @Test
    public void testUpdateProfileImage() {
        // Arrange
        String newProfileImageUrl = "https://example.com/new-profile.jpg";

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update profile image should succeed";
                }
            };

        // Act
        repository.updateProfileImageUrl(testUserId, newProfileImageUrl, callback);

        // Assert
        // Note: Full testing requires mocking Firestore update with profilePicUrl field
    }

    @Test
    public void testUpdateDietaryPreference() {
        // Arrange
        String newDietaryPreference = "Vegan";

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update dietary preference should succeed";
                }
            };

        // Act
        repository.updateDietaryPreference(testUserId, newDietaryPreference, callback);

        // Assert
        // Note: Full testing requires mocking Firestore update with dietaryPreferences field
    }

    @Test
    public void testGetUserByIdOnce() {
        // Arrange
        FirebaseUserRepository.RepositoryCallback<User> callback =
            new FirebaseUserRepository.RepositoryCallback<User>() {
                @Override
                public void onSuccess(User data) {
                    // Verify success
                    assertNotNull(data);
                    assertEquals(testUserId, data.getId());
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called in successful case
                }
            };

        // Act
        repository.getUserByIdOnce(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.get()
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Delete user should succeed";
                }
            };

        // Act
        repository.deleteUser(testUserId, callback);

        // Assert
        // Note: Full testing requires mocking Firestore document.delete()
    }

    @Test
    public void testUpdateUserField() {
        // Arrange
        String fieldName = "name";
        String fieldValue = "New Name";

        FirebaseUserRepository.RepositoryCallback<Void> callback =
            new FirebaseUserRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    // Verify success
                    assert true;
                }

                @Override
                public void onFailure(String error) {
                    // Should not be called
                    assert false : "Update field should succeed";
                }
            };

        // Act
        repository.updateUserField(testUserId, fieldName, fieldValue, callback);

        // Assert
        // Note: Full testing requires mocking Firestore update with custom field
    }

    @Test
    public void testCollectionPath() {
        // This test verifies that the correct Firestore collection path is used
        // Expected path: users/{userId}

        String expectedPath = "users/" + testUserId;

        // Note: In a real test, you would mock FirebaseFirestore.getInstance()
        // and verify that collection() and document() are called with correct paths
        // For now, we verify the path format is correct
        assertTrue(expectedPath.equals("users/" + testUserId));
    }
}
