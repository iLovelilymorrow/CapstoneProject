package com.example.capstoneproject.helper;

import android.content.Context;
import android.util.Log;

import com.example.capstoneproject.model.MembershipType;
import com.example.capstoneproject.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private Context context;

    public interface LoginCallback {
        void onSuccess(String userId, String username, String userRole);
        void onFailure(String errorMessage);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface MembershipTypesCallback {
        void onSuccess(List<MembershipType> membershipTypes);
        void onFailure(Exception e);
    }

    public interface EmailCheckCallback {
        void onResult(boolean isUsed);
        void onError(Exception e);
    }

    public interface GeneralCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface GenerateIdCallback
    {
        void onGenerated(String memberId);
        void onError(Exception e);
    }

    public FirebaseHelper(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void loginUser(String email, String password, final LoginCallback callback)
    {
        if (email.isEmpty() || password.isEmpty()){
            callback.onFailure("Email and password cannot be empty");
            return;
        }


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

    public void registerUser(String email, String password, RegisterCallback callback)
    {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task ->
                {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user != null)
                        {
                            user.sendEmailVerification();
                            callback.onSuccess();
                        }
                        firebaseAuth.signOut();
                    }
                    else
                    {
                        callback.onFailure("Registration failed: " + task.getException().getMessage());
                        Log.e("FirebaseHelper", "Error creating user: " + task.getException().getMessage());
                    }
                });
    }

    public void createUserWithEmail(String email, String password, String phoneNumber)
    {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                createUserInFirestore(uid, email, phoneNumber);
            }
            else
            {
                Log.e("FirebaseHelper", "Error creating user: " + task.getException().getMessage());
            }
        });
    }

    public void createUserInFirestore(String uid, String email, String phoneNumber)
    {
        generateIDWithCounter("member", new GenerateIdCallback()
        {
            @Override
            public void onGenerated(String memberId)
            {
                Users users = new Users(memberId, email, "member", "change_me " + memberId, phoneNumber);

                db.collection("users").document(uid).set(users)
                        .addOnSuccessListener(aVoid -> Log.d("FirebaseHelper", "User created with UID: " + uid))
                        .addOnFailureListener(e -> Log.e("FirebaseHelper", "Error creating user: " + e.getMessage()));
            }

            @Override
            public void onError(Exception e)
            {
                Log.e("FirebaseHelper", "Failed to generate member ID: " + e.getMessage());
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

    public void isEmailAvailable(String phoneNumber, final EmailCheckCallback callback)
    {
        db.collection("users")
                .whereEqualTo("email", phoneNumber)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
        {
            callback.onResult(!queryDocumentSnapshots.isEmpty());
        }).addOnFailureListener(callback :: onError);
    }

    public void generateIDWithCounter(String role, GenerateIdCallback callback)
    {
        DocumentReference counterRef = db.collection("counters").document(role);

        db.runTransaction(transaction ->
        {
            DocumentSnapshot snapshot = transaction.get(counterRef);
            long current = snapshot.exists() ? snapshot.getLong("lastNumber") : 0;
            long next = current + 1;
            transaction.set(counterRef, Collections.singletonMap("lastNumber", next), SetOptions.merge());

            String prefix = role.equals("admin") ? "A" : "M";
            String padded;

            if (role.equals("admin"))
            {
                padded = String.format("%02d", next);
            }
            else
            {
                padded = String.format("%04d", next);
            }

            return prefix + padded;
        }).addOnSuccessListener(callback::onGenerated)
          .addOnFailureListener(callback::onError);
    }

    public void generateMembershipTypeID(final GenerateIdCallback callback)
    {
        db.collection("membershiptype").get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            int count = queryDocumentSnapshots.size();
            String formattedID = String.format("MT%03d", count + 1);
            callback.onGenerated(formattedID);
        });
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}