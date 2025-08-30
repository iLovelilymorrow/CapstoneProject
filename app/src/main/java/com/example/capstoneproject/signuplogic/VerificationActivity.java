package com.example.capstoneproject.signuplogic;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.capstoneproject.R;
import com.example.capstoneproject.helper.FirebaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationActivity extends AppCompatActivity
{
    private TextView messageTextView;
    private EditText codeEditText;
    private MaterialButton verifyButton;

    private String verificationId;
    private String phoneNumber;
    private String email;
    private String password;
    private String code;

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verification_layout);

        firebaseHelper = new FirebaseHelper(this);

        getStringExtras();
        initializeViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verification), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verifyButton.setOnClickListener(v ->
        {
            verifyCode();
        });
    }

    private void verifyCode()
    {
        code = codeEditText.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                firebaseHelper.createUserWithEmail(email, password, phoneNumber);
                finish();
            }
            else
            {
                messageTextView.setText("Code verification failed, please try again.");
            }
        });
    }

    private void getStringExtras()
    {
        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
    }

    private void initializeViews()
    {
        messageTextView = findViewById(R.id.messageTextView);
        codeEditText = findViewById(R.id.codeEditText);
        verifyButton = findViewById(R.id.confirmButton);
    }
}