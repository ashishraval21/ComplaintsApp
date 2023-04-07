package com.municipal.complaintsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.classes.Registration;
import com.municipal.complaintsapp.databinding.ActivityRegisterBinding;
import com.municipal.complaintsapp.util.ActivityUtil;
import com.municipal.complaintsapp.util.ErrorMessage;
import com.municipal.complaintsapp.util.SharedPreference;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class register extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(new SharedPreference(getApplicationContext()).isSharedPreferenceAvailable()){
            ActivityUtil.nextScreen(register.this, ComplaintListActivity.class, null);
            finish();
        }





        ImageView view = findViewById(R.id.imageLogo);
        view.setImageResource(R.drawable.cmslogos);

        TextInputLayout nameLayout = findViewById(R.id.filledNameTextField);
        nameLayout.setStartIconDrawable(R.drawable.icons8);
        nameLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));

        TextInputLayout phoneLayout = findViewById(R.id.filledMobileNumberTextField);
        phoneLayout.setStartIconDrawable(R.drawable.phone);
        phoneLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));

        TextInputLayout mailLayout = findViewById(R.id.filledEmailTextField);
        mailLayout.setStartIconDrawable(R.drawable.mail);
        mailLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));

        TextInputLayout passwordLayout = findViewById(R.id.filledPasswrdTextField);
        passwordLayout.setStartIconDrawable(R.drawable.password);
        passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));


        TextView clickLogin = findViewById(R.id.loginRegister);
        clickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.nextScreen(register.this, MainActivity.class, null);
                finish();
            }
        });


        Button registerButton = findViewById(R.id.btnregisterform);
        registerButton.setOnClickListener(view1 -> {

            Registration registration = new Registration();
            registration.setEmail(mailLayout.getEditText().getText().toString());
            registration.setPassword(passwordLayout.getEditText().getText().toString());
            registration.setFirstName(nameLayout.getEditText().getText().toString());
            registration.setMobileNo(phoneLayout.getEditText().getText().toString());

            APICallUtils.makeApiCallWithRetry(ApiList.Register.getApi(), MethodType.POST.toString(),
                    JsonUtils.getJsonObject(registration),  new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if(response.isSuccessful()){
                                Snackbar mySnackbar = Snackbar.make(view1, "Registration Successful, Please Verify Your Email Now.", BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();
                                ActivityUtil.nextScreen(register.this, MainActivity.class, null);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            Snackbar mySnackbar = Snackbar.make(view1, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.show();
                        }
                    });




        });

    }



}