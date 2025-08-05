package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    public FirebaseHelper(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // 🔐 Login method
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            fetchUserRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(context, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔍 Fetch role from Firestore
    private void fetchUserRole(String uid) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        routeUser(role);
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching user role", Toast.LENGTH_SHORT).show();
                });
    }

    // 🚪 Redirect based on role
    private void routeUser(String role) {
        Class<?> targetActivity;

        switch (role) {
            case "admin":
                targetActivity = AdminActivity.class;
                break;
            case "member":
                targetActivity = MemberActivity.class;
                break;
            default:
                Toast.makeText(context, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                return;
        }

        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

