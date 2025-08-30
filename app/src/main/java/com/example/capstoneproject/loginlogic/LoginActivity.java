package com.example.capstoneproject.loginlogic;

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

import com.example.capstoneproject.helper.FirebaseHelper;
import com.example.capstoneproject.R;
import com.example.capstoneproject.adminlogic.AdminActivity;
import com.example.capstoneproject.memberlogic.MemberActivity;
import com.example.capstoneproject.signuplogic.SignupActivity;


public class LoginActivity extends AppCompatActivity
{
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

        firebaseHelper = new FirebaseHelper(this);

        initializeViews(); // Initialize views directly

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton.setOnClickListener(v ->
        {

        });

        signupButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
    //sign in cretion of user in "users"

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
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.button2);
        signupButton = findViewById(R.id.button);
    }
}
