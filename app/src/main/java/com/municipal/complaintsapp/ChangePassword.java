package com.municipal.complaintsapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.databinding.ActivityChangePasswordBinding;
import com.municipal.complaintsapp.util.ActivityUtil;
import com.municipal.complaintsapp.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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


        Button submitButton = findViewById(R.id.submitChangeButton);
        submitButton.setOnClickListener(view -> {
            // Perform validation checks for the input passwords
            if (isValidPassword(currentPassword.getText().toString(), newPassword.getText().toString(), confirmPassword.getText().toString())) {
                updatePassword(currentPassword.getText().toString(), newPassword.getText().toString(), view);
            }else{

                Snackbar mySnackbar = Snackbar.make(view, "Password Mismatch", BaseTransientBottomBar.LENGTH_LONG);
                mySnackbar.show();
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
        if(newPassword == null || confirmPassword == null)
            return false;
        return newPassword.equals(confirmPassword);
    }

    private void updatePassword(String currentPassword, String newPassword, View view) {
        int id = new SharedPreference(getApplicationContext()).getId();
        com.municipal.complaintsapp.classes.ChangePassword password = new com.municipal.complaintsapp.classes.ChangePassword(id, currentPassword,newPassword);



        APICallUtils.makeApiCallWithRetry(ApiList.ChangePassword.getApi(), MethodType.PUT.toString(),
                JsonUtils.getJsonObject(password),  new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()){


                            Snackbar mySnackbar = Snackbar.make(view, "Password Changed SuccessFully", BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        Snackbar mySnackbar = Snackbar.make(view, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                        mySnackbar.show();
                    }
                });

    }
}