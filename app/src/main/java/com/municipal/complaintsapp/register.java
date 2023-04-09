package com.municipal.complaintsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.municipal.complaintsapp.util.BackgroundTask;
import com.municipal.complaintsapp.util.CustomAlertUtils;
import com.municipal.complaintsapp.util.EmailValidator;
import com.municipal.complaintsapp.util.ErrorMessage;
import com.municipal.complaintsapp.util.PasswordValidator;
import com.municipal.complaintsapp.util.SharedPreference;
import com.municipal.complaintsapp.util.TextBoxValidator;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class register extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterBinding binding;

    EditText name;
    EditText mobile;

    EditText email;

    EditText password;




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
       // view.setImageResource(R.drawable.cmslogos);

        view.setImageResource(R.drawable.app_logo);

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



        name = nameLayout.getEditText();
        mobile = phoneLayout.getEditText();
        email = mailLayout.getEditText();
        password = passwordLayout.getEditText();

        TextBoxValidator.inputValidator(name);
        TextBoxValidator.inputValidator(mobile);
        EmailValidator.addEmailValidator(email);
       PasswordValidator.addPasswordValidator(password);


        TextView clickLogin = findViewById(R.id.loginRegister);
        clickLogin.setOnClickListener(v -> {
            ActivityUtil.nextScreen(register.this, MainActivity.class, null);
            finish();
        });


        Button registerButton = findViewById(R.id.btnregisterform);
        registerButton.setOnClickListener(view1 -> {

            // verify all the details
            if(!validateDetails()){
                return;
            }

            RegisterUser();





        });





    }

    private void RegisterUser() {

        BackgroundTask<String> bgtask = new BackgroundTask<>(new BackgroundTask.BackgroundTaskExecutor<String>() {
            @Override
            public String run() throws Exception {

                Registration registration = new Registration();
                registration.setEmail(email.getText().toString());
                registration.setPassword(password.getText().toString());
                registration.setFirstName(name.getText().toString());
                registration.setMobileNo(mobile.getText().toString());

                APICallUtils.makeApiCallWithRetry(ApiList.Register.getApi(), MethodType.POST.toString(),
                        JsonUtils.getJsonObject(registration),  new Callback() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful()){

                                    runOnUiThread(() -> new CustomAlertUtils(view1 -> {
                                        ActivityUtil.nextScreen(register.this, MainActivity.class, null);
                                    },findViewById(R.id.layoutDialogContainer) ).showCustomAlertDialog("Registration Successful, Please Verify Your Email Now.", getApplicationContext()));

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                                View rootView = findViewById(android.R.id.content);
                                runOnUiThread(() -> new CustomAlertUtils(view1 -> {

                                },findViewById(R.id.layoutDialogContainer) ).showCustomAlertDialog(e.getMessage(), getApplicationContext()));

                            }
                        });



                return null;
            }
        }, new BackgroundTask.BackgroundTaskListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        bgtask.execute();




    }

    public boolean validateDetails(){

        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Please Enter Full Name.");
            return false;
        }else{
            name.setError(null);
        }


        if(TextUtils.isEmpty(mobile.getText().toString())){
            mobile.setError("Please Enter Mobile Number");
            return false;
        }else{
            mobile.setError(null);
        }

        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Please Enter Email Id");
            return false;
        }else{
            email.setError(null);
        }

        if(!EmailValidator.isEmailValid(email.getText().toString())){
            email.setError("Invalid Email Id.");
            return false;
        }else{
            email.setError(null);
        }

        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please Enter Password");
            return  false;
        }else{
            password.setError(null);
        }

        if(!PasswordValidator.isPasswordValid(password.getText().toString())){
            password.setError("Password must be at least 8 characters long and contain at least one number, one lowercase letter, one uppercase letter, and one symbol.");
            return false;
        }else{
            password.setError(null);
        }

        return true;
    }




}