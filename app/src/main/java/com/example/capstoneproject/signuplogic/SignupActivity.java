package com.example.capstoneproject.signuplogic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.capstoneproject.R;
import com.example.capstoneproject.helper.FirebaseHelper;
import com.example.capstoneproject.helper.ValidateInputHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity
{
    private TextView messageTextView;
    private MaterialButton backButton;
    private MaterialButton confirmButton;

    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private ValidateInputHelper validateInputHelper;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_layout);

        validateInputHelper = new ValidateInputHelper();
        firebaseHelper = new FirebaseHelper(this);
        initializeViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton.setOnClickListener(v ->
        {
            finish();
        });

        confirmButton.setOnClickListener(v ->
        {
            validateInput();
        });
    }

    private void validateInput()
    {
        boolean isReady = true;

        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            isReady = false;
        } else if (!validateInputHelper.isEmailValid(email)) {
            emailEditText.setError("Invalid email");
            isReady = false;
        }

        String e164PhoneNumber = null;
        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Field cannot be empty");
            isReady = false;
        } else if (!validateInputHelper.isValidPHMobile(phoneNumber)) {
            phoneNumberEditText.setError("Invalid phone number");
            isReady = false;
        }
        else
        {
            e164PhoneNumber = validateInputHelper.normalizePHMobile(phoneNumber);
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Field cannot be empty");
            isReady = false;
        } else if (!validateInputHelper.isPasswordValid(password)) {
            passwordEditText.setError("Password must be at least 8 characters");
            isReady = false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Field cannot be empty");
            isReady = false;
        } else if (!validateInputHelper.isPasswordSame(password, confirmPassword)) {
            confirmPasswordEditText.setError("Passwords does not match");
            isReady = false;
        }

        if (isReady) {
            if (e164PhoneNumber == null) {
                phoneNumberEditText.setError("Invalid phone number format");
                return;
            }

            firebaseHelper.isEmailAvailable(email, new FirebaseHelper.EmailCheckCallback() {
                @Override
                public void onResult(boolean isUsed) {
                    if (isUsed) {
                        emailEditText.setError("Phone number already registered");
                    }
                    else
                    {
                        firebaseHelper.registerUser(email, password, new FirebaseHelper.RegisterCallback()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Log.d("Signup", "Registration successful");
                                Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(String errorMessage)
                            {
                                Log.e("Signup", "Registration failed: " + errorMessage);
                                Toast.makeText(SignupActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Signup", "Error checking phone number: " + e.getMessage());
                }
            });
        }
    }

    private void initializeViews()
    {
        messageTextView = findViewById(R.id.messageTextView);
        backButton = findViewById(R.id.backButton);
        confirmButton = findViewById(R.id.confirmButton);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
    }
}