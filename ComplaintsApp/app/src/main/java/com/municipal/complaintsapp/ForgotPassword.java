package com.municipal.complaintsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.municipal.complaintsapp.databinding.ActivityChangePasswordBinding;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout otpTextInputLayout;
    private TextInputLayout newPasswordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;

    private EditText emailEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot Password");
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        otpTextInputLayout = findViewById(R.id.otpTextInputLayout);
        newPasswordTextInputLayout = findViewById(R.id.newPasswordTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
        submitButton = findViewById(R.id.submitButton);
        emailEditText = emailTextInputLayout.getEditText();

        // Initially, only the email textbox is visible
        otpTextInputLayout.setVisibility(View.GONE);
        newPasswordTextInputLayout.setVisibility(View.GONE);
        confirmPasswordTextInputLayout.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextInputLayout.getEditText().getText().toString();
                if (isValidEmail(email)) {
                    // Show the OTP and new password textboxes if email is valid
                    emailTextInputLayout.getEditText().setEnabled(false);
                    otpTextInputLayout.setVisibility(View.VISIBLE);
                    newPasswordTextInputLayout.setVisibility(View.VISIBLE);
                    confirmPasswordTextInputLayout.setVisibility(View.VISIBLE);
                } else {
                    emailTextInputLayout.setError("Invalid email");
                }
            }
        });

    }

    private boolean isValidEmail(String email) {
        // Add code to validate email
        return true;
    }
}