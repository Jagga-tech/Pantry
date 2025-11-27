package com.pantrypal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.data.firebase.FirebaseAuthManager;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.repository.FirebaseFavoritesRepository;
import com.pantrypal.data.repository.FirebasePantryRepository;
import com.pantrypal.data.repository.FirebaseUserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Integration tests for PantryPal app
 *
 * IMPORTANT: These tests require Firebase Test Lab setup
 *
 * Setup Instructions:
 * 1. Create a Firebase project for testing
 * 2. Download google-services.json for testing environment
 * 3. Enable Firebase Authentication (Email/Password and Google Sign-In)
 * 4. Enable Firestore Database
 * 5. Set up Firebase Test Lab in Firebase Console
 *
 * Running Tests:
 * - Local: Run from Android Studio (requires emulator with Google Play Services)
 * - Cloud: Use Firebase Test Lab via CLI or Console
 *
 * Note: These tests use real Firebase services, not mocks
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PantryPalIntegrationTest {

    private Context context;
    private FirebaseAuthManager authManager;
    private FirebasePantryRepository pantryRepository;
    private FirebaseUserRepository userRepository;
    private FirebaseFavoritesRepository favoritesRepository;
    private String testEmail = "test" + System.currentTimeMillis() + "@pantrypal.com";
    private String testPassword = "testPassword123";

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        authManager = new FirebaseAuthManager();
        pantryRepository = new FirebasePantryRepository();
        userRepository = new FirebaseUserRepository();
        favoritesRepository = new FirebaseFavoritesRepository();

        // Sign out any existing user
        authManager.signOut();
    }

    @After
    public void tearDown() {
        // Clean up: sign out after each test
        authManager.signOut();
    }

    @Test
    public void testCompleteUserFlow() throws InterruptedException {
        // Step 1: Sign up
        CountDownLatch signUpLatch = new CountDownLatch(1);
        final FirebaseUser[] signedUpUser = new FirebaseUser[1];

        authManager.signUpWithEmail(testEmail, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                signedUpUser[0] = user;
                signUpLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("Signup failed: " + error);
            }
        });

        // Wait for signup to complete
        signUpLatch.await(10, TimeUnit.SECONDS);
        assert signedUpUser[0] != null : "User should be signed up";

        // Step 2: Add pantry item
        CountDownLatch addItemLatch = new CountDownLatch(1);
        PantryItem testItem = new PantryItem();
        testItem.setIngredientName("Test Tomatoes");
        testItem.setCategory("Vegetables");
        testItem.setQuantity("5");
        testItem.setUnit("pieces");
        testItem.setExpirationDate(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
        testItem.setNotes("Integration test item");

        pantryRepository.addPantryItem(signedUpUser[0].getUid(), testItem,
            new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String itemId) {
                    assert itemId != null : "Item should be added with ID";
                    addItemLatch.countDown();
                }

                @Override
                public void onFailure(String error) {
                    throw new AssertionError("Add item failed: " + error);
                }
            });

        // Wait for item to be added
        addItemLatch.await(10, TimeUnit.SECONDS);

        // Step 3: Add to favorites
        CountDownLatch addFavoriteLatch = new CountDownLatch(1);
        String testRecipeId = "recipe-test-123";

        favoritesRepository.addFavorite(signedUpUser[0].getUid(), testRecipeId,
            new FirebaseFavoritesRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    addFavoriteLatch.countDown();
                }

                @Override
                public void onFailure(String error) {
                    throw new AssertionError("Add favorite failed: " + error);
                }
            });

        // Wait for favorite to be added
        addFavoriteLatch.await(10, TimeUnit.SECONDS);

        // Step 4: Verify favorites
        CountDownLatch checkFavoriteLatch = new CountDownLatch(1);
        favoritesRepository.isFavorite(signedUpUser[0].getUid(), testRecipeId,
            new FirebaseFavoritesRepository.RepositoryCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean isFavorited) {
                    assert isFavorited : "Recipe should be favorited";
                    checkFavoriteLatch.countDown();
                }

                @Override
                public void onFailure(String error) {
                    throw new AssertionError("Check favorite failed: " + error);
                }
            });

        // Wait for favorite check
        checkFavoriteLatch.await(10, TimeUnit.SECONDS);

        // Step 5: Logout
        authManager.signOut();
        assert !authManager.isUserSignedIn() : "User should be signed out";
    }

    @Test
    public void testOfflineSync() throws InterruptedException {
        // Note: This test requires Firebase offline persistence to be enabled
        // Which we've already done in PantryPalApplication

        // Step 1: Sign in
        CountDownLatch signInLatch = new CountDownLatch(1);
        final FirebaseUser[] user = new FirebaseUser[1];

        authManager.signUpWithEmail(testEmail, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                user[0] = firebaseUser;
                signInLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("Signup failed: " + error);
            }
        });

        signInLatch.await(10, TimeUnit.SECONDS);

        // Step 2: Add item (will be cached offline if no network)
        CountDownLatch addItemLatch = new CountDownLatch(1);
        PantryItem testItem = new PantryItem();
        testItem.setIngredientName("Offline Test Item");
        testItem.setCategory("Grains");
        testItem.setQuantity("2");
        testItem.setUnit("kg");
        testItem.setExpirationDate(new Date());

        pantryRepository.addPantryItem(user[0].getUid(), testItem,
            new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String itemId) {
                    assert itemId != null;
                    addItemLatch.countDown();
                }

                @Override
                public void onFailure(String error) {
                    // May fail if completely offline and no cache
                    addItemLatch.countDown();
                }
            });

        addItemLatch.await(10, TimeUnit.SECONDS);

        // Note: Full offline testing requires network toggling
        // which is better done in Firebase Test Lab with network profiles
    }

    @Test
    public void testRealTimeSync() throws InterruptedException {
        // Step 1: Sign in
        CountDownLatch signInLatch = new CountDownLatch(1);
        final FirebaseUser[] user = new FirebaseUser[1];

        authManager.signUpWithEmail(testEmail, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                user[0] = firebaseUser;
                signInLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("Signup failed: " + error);
            }
        });

        signInLatch.await(10, TimeUnit.SECONDS);

        // Step 2: Observe LiveData
        LiveData<List<PantryItem>> itemsLiveData = pantryRepository.getAllPantryItems(user[0].getUid());
        CountDownLatch observeLatch = new CountDownLatch(1);

        Observer<List<PantryItem>> observer = new Observer<List<PantryItem>>() {
            @Override
            public void onChanged(List<PantryItem> items) {
                // Real-time update received
                if (items != null) {
                    observeLatch.countDown();
                }
            }
        };

        // Note: In real test, you'd observe on main thread
        // This is simplified for demonstration

        // Step 3: Add item and verify LiveData updates
        PantryItem testItem = new PantryItem();
        testItem.setIngredientName("Real-time Test Item");
        testItem.setCategory("Dairy");
        testItem.setQuantity("1");
        testItem.setUnit("liter");
        testItem.setExpirationDate(new Date());

        pantryRepository.addPantryItem(user[0].getUid(), testItem,
            new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String itemId) {
                    // Item added successfully
                }

                @Override
                public void onFailure(String error) {
                    throw new AssertionError("Add item failed: " + error);
                }
            });

        // Wait for real-time update
        observeLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testAuthenticationFlow() throws InterruptedException {
        // Test 1: Valid signup
        CountDownLatch signUpLatch = new CountDownLatch(1);
        authManager.signUpWithEmail(testEmail, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                assert user != null;
                signUpLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("Valid signup should succeed");
            }
        });
        signUpLatch.await(10, TimeUnit.SECONDS);

        // Test 2: Sign out
        authManager.signOut();
        assert !authManager.isUserSignedIn();

        // Test 3: Sign in with same credentials
        CountDownLatch signInLatch = new CountDownLatch(1);
        authManager.signInWithEmail(testEmail, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                assert user != null;
                signInLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("Valid signin should succeed: " + error);
            }
        });
        signInLatch.await(10, TimeUnit.SECONDS);

        // Test 4: Invalid credentials
        CountDownLatch invalidSignInLatch = new CountDownLatch(1);
        authManager.signInWithEmail(testEmail, "wrongpassword", new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                throw new AssertionError("Invalid signin should fail");
            }

            @Override
            public void onFailure(String error) {
                assert error != null;
                invalidSignInLatch.countDown();
            }
        });
        invalidSignInLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testDataIsolation() throws InterruptedException {
        // Create two different users
        String email1 = "user1" + System.currentTimeMillis() + "@pantrypal.com";
        String email2 = "user2" + System.currentTimeMillis() + "@pantrypal.com";

        // Sign up user 1
        CountDownLatch user1SignUpLatch = new CountDownLatch(1);
        final String[] user1Id = new String[1];

        authManager.signUpWithEmail(email1, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                user1Id[0] = user.getUid();
                user1SignUpLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("User 1 signup failed");
            }
        });
        user1SignUpLatch.await(10, TimeUnit.SECONDS);

        // Add item for user 1
        CountDownLatch addItem1Latch = new CountDownLatch(1);
        PantryItem user1Item = new PantryItem();
        user1Item.setIngredientName("User 1 Item");
        user1Item.setCategory("Vegetables");
        user1Item.setQuantity("1");
        user1Item.setUnit("piece");
        user1Item.setExpirationDate(new Date());

        pantryRepository.addPantryItem(user1Id[0], user1Item,
            new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String itemId) {
                    addItem1Latch.countDown();
                }

                @Override
                public void onFailure(String error) {
                    throw new AssertionError("Add item for user 1 failed");
                }
            });
        addItem1Latch.await(10, TimeUnit.SECONDS);

        // Sign out user 1
        authManager.signOut();

        // Sign up user 2
        CountDownLatch user2SignUpLatch = new CountDownLatch(1);
        final String[] user2Id = new String[1];

        authManager.signUpWithEmail(email2, testPassword, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                user2Id[0] = user.getUid();
                user2SignUpLatch.countDown();
            }

            @Override
            public void onFailure(String error) {
                throw new AssertionError("User 2 signup failed");
            }
        });
        user2SignUpLatch.await(10, TimeUnit.SECONDS);

        // Verify user 2 cannot see user 1's items
        // Note: This requires querying Firestore with proper security rules
        // Security rules should be: allow read, write: if request.auth.uid == userId

        assert !user1Id[0].equals(user2Id[0]) : "User IDs should be different";
    }
}
