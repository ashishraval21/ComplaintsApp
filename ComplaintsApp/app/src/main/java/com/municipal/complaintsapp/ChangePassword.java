package com.municipal.complaintsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.municipal.complaintsapp.databinding.ActivityChangePasswordBinding;

public class ChangePassword extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityChangePasswordBinding binding;

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Change Password");

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform validation checks for the input passwords
                if (isValidPassword(currentPassword.getText().toString(), newPassword.getText().toString(), confirmPassword.getText().toString())) {
                    // Call the API to update the password
                    updatePassword(currentPassword.getText().toString(), newPassword.getText().toString());
                }
            }
        });

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the text fields
                currentPassword.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_change_password);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean isValidPassword(String currentPassword, String newPassword, String confirmPassword) {
        // Validate that the current password is correct, the new password and confirm password match, etc.
        // Return true if the password is valid, false otherwise

        return true;
    }

    private void updatePassword(String currentPassword, String newPassword) {
        // Call the API to update the password
    }
}