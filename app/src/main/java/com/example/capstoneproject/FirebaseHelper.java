package com.example.capstoneproject;

import android.content.Context;
import android.util.Log;

import com.example.capstoneproject.model.MembershipType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    public interface LoginCallback {
        void onSuccess(String userId, String username, String userRole); // Pass role if needed for navigation
        void onFailure(String errorMessage);
    }

    public interface MembershipTypesCallback {
        void onSuccess(List<MembershipType> membershipTypes);
        void onFailure(Exception e);
    }

    public interface GeneralCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public FirebaseHelper(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void loginUser(String email, String password, final LoginCallback callback) {
        if (email.isEmpty() || password.isEmpty()) {
            callback.onFailure("Email and password cannot be empty.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            // Fetch user details (like username and role) from Firestore
                            fetchUserDetails(userId, callback);
                        } else {
                            callback.onFailure("Login successful but user data not found.");
                        }
                    } else {
                        callback.onFailure("Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    public void fetchUserDetails(String userId, final LoginCallback callback) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String username = document.getString("username"); // Your Firestore field
                            String role = document.getString("role");    // Assuming you have a 'role' field

                            if (username == null) {
                                username = "N/A"; // Fallback if username is not in DB
                            }
                            if (role == null) {
                                role = "member"; // Default role or handle error
                            }
                            callback.onSuccess(userId, username, role);
                        } else {
                            callback.onFailure("User details not found in database.");
                        }
                    } else {
                        callback.onFailure("Failed to fetch user details: " + task.getException().getMessage());
                    }
                });
    }

    public void createMembershipType(MembershipType membershipType, final GeneralCallback callback)
    {
        db.collection("membershiptype").add(membershipType).addOnSuccessListener(documentReference ->
        {
            Log.d("FirebaseHelper", "Membership type created with ID: " + documentReference.getId());
            callback.onSuccess();
        }).addOnFailureListener(e ->
        {
            Log.w("FirebaseHelper", "Error creating membership type", e);
            callback.onFailure(e);
        });
    }

    public void fetchMembershipTypes(final MembershipTypesCallback callback) {
        db.collection("membershiptype").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<MembershipType> membershipTypeList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
            {
                MembershipType membershipType = document.toObject(MembershipType.class);
                membershipTypeList.add(membershipType);
            }
            callback.onSuccess(membershipTypeList);

        }).addOnFailureListener(e ->
        {
            Log.e("FirebaseHelper", "Error getting membership types: " + e.getMessage(), e);
            callback.onFailure(e);
        });
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
    }
}