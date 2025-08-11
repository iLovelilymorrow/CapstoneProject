package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.adminlogic.AdminActivity;
import com.example.capstoneproject.memberlogic.MemberActivity;

// FirebaseAuth and FirebaseUser are no longer needed for the initial check here
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

    private FirebaseHelper firebaseHelper;
    // private FirebaseAuth mAuth; // mAuth instance can still be used for login process if FirebaseHelper doesn't encapsulate it fully

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_layout);

        // mAuth = FirebaseAuth.getInstance(); // Initialize if needed for direct login calls
        firebaseHelper = new FirebaseHelper(this);

        // --- Persistence Check REMOVED from here ---

        initializeViews(); // Initialize views directly

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseHelper.loginUser(email, password, new FirebaseHelper.LoginCallback() {
                @Override
                public void onSuccess(String userId, String username, String userRole) {
                    Toast.makeText(LoginActivity.this, "Login Successful. Welcome " + username, Toast.LENGTH_SHORT).show();
                    redirectToRoleBasedActivity(username, userRole); // This method remains
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        signupButton.setOnClickListener(v -> {
            Toast.makeText(this, "Signup Clicked (Implement SignupActivity)", Toast.LENGTH_SHORT).show();
        });
    }

    // This method is still needed if login is successful from this activity
    private void redirectToRoleBasedActivity(String username, String userRole) {
        Intent intent;
        if ("admin".equalsIgnoreCase(userRole)) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MemberActivity.class);
        }
        intent.putExtra("USERNAME_EXTRA", username);
        intent.putExtra("USER_ROLE_EXTRA", userRole);
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.button2);
        signupButton = findViewById(R.id.button);
    }
}
