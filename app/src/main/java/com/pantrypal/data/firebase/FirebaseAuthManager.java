package com.pantrypal.data.firebase;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthManager {

    private final FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    public FirebaseAuthManager() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Callback interface for authentication operations
     */
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }

    /**
     * Sign up with email and password
     */
    public void signUpWithEmail(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            callback.onFailure("Email and password cannot be empty");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Sign up failed";
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Sign in with email and password
     */
    public void signInWithEmail(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            callback.onFailure("Email and password cannot be empty");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Sign in failed";
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Initialize Google Sign-In
     */
    public void initializeGoogleSignIn(Activity activity, String webClientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Get Google Sign-In intent
     */
    public Intent getGoogleSignInIntent() {
        if (googleSignInClient == null) {
            throw new IllegalStateException("Google Sign-In not initialized. Call initializeGoogleSignIn() first.");
        }
        return googleSignInClient.getSignInIntent();
    }

    /**
     * Handle Google Sign-In result
     */
    public void handleGoogleSignInResult(Intent data, AuthCallback callback) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken(), callback);
            } else {
                callback.onFailure("Google Sign-In failed: No account");
            }
        } catch (ApiException e) {
            callback.onFailure("Google Sign-In failed: " + e.getMessage());
        }
    }

    /**
     * Authenticate with Firebase using Google credentials
     */
    private void firebaseAuthWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Authentication with Google failed";
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String email, AuthCallback callback) {
        if (email == null || email.isEmpty()) {
            callback.onFailure("Email cannot be empty");
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Password reset failed";
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Sign out the current user
     */
    public void signOut() {
        firebaseAuth.signOut();
        if (googleSignInClient != null) {
            googleSignInClient.signOut();
        }
    }

    /**
     * Get current signed-in user
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Check if user is signed in
     */
    public boolean isUserSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * Get user ID of current user
     */
    public String getCurrentUserId() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}
