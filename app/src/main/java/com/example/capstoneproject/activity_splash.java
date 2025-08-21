package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler; // Keep for minimum splash time if desired
import android.util.Log;
import android.widget.Toast; // For error messages if needed

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.adminlogic.AdminActivity;
import com.example.capstoneproject.helper.FirebaseHelper;
import com.example.capstoneproject.loginlogic.LoginActivity;
import com.example.capstoneproject.memberlogic.MemberActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_splash extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long MIN_SPLASH_DISPLAY_TIME = 1500; // Minimum time splash is shown (milliseconds)

    private FirebaseAuth mAuth;
    private FirebaseHelper firebaseHelper; // Assuming FirebaseHelper handles Firestore calls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Apply window insets if needed for your splash layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_splash), (v, insets) -> { // Ensure R.id.activity_splash_root exists
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseHelper = new FirebaseHelper(this); // Initialize your helper

        new Handler().postDelayed(this::checkUserPersistence, MIN_SPLASH_DISPLAY_TIME);
    }

    private void checkUserPersistence() {
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Or firebaseHelper.getCurrentUser()

        if (currentUser != null) {
            // User is signed in, fetch details and redirect
            Log.d(TAG, "User " + currentUser.getUid() + " is already logged in. Fetching details...");
            firebaseHelper.fetchUserDetails(currentUser.getUid(), new FirebaseHelper.LoginCallback() {
                @Override
                public void onSuccess(String userId, String username, String userRole) {
                    Log.d(TAG, "Fetched details for auto-login: User: " + username + ", Role: " + userRole);
                    redirectToRoleBasedActivity(username, userRole);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e(TAG, "Auto-login: Failed to fetch user details: " + errorMessage);
                    // Critical failure to get details for an active session.
                    // Option 1: Go to Login (user might need to re-auth or there's a data issue)
                    navigateToLogin();
                    // Option 2: You could sign them out here if details are absolutely critical for app function
                    // mAuth.signOut();
                    // navigateToLogin();
                    Toast.makeText(activity_splash.this, "Session active, but failed to load details. Please log in.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // No user is signed in
            Log.d(TAG, "No user logged in. Navigating to LoginActivity.");
            navigateToLogin();
        }
    }

    private void redirectToRoleBasedActivity(String username, String userRole) {
        Intent intent;
        if ("admin".equalsIgnoreCase(userRole)) {
            intent = new Intent(activity_splash.this, AdminActivity.class);
        } else {
            intent = new Intent(activity_splash.this, MemberActivity.class);
        }
        intent.putExtra("USERNAME_EXTRA", username);
        intent.putExtra("USER_ROLE_EXTRA", userRole);
        startActivity(intent);
        finish(); // Finish SplashActivity
    }

    private void navigateToLogin() {
        Intent intent = new Intent(activity_splash.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish SplashActivity
    }
}
