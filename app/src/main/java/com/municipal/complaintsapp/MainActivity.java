package com.municipal.complaintsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.classes.Login;
import com.municipal.complaintsapp.util.ActivityUtil;
import com.municipal.complaintsapp.util.BackgroundTask;
import com.municipal.complaintsapp.util.CustomAlertUtils;
import com.municipal.complaintsapp.util.EmailValidator;
import com.municipal.complaintsapp.util.PasswordValidator;
import com.municipal.complaintsapp.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView view = findViewById(R.id.imageLogo);
        view.setImageResource(R.drawable.cmslogos);

        TextInputLayout mailLayout = findViewById(R.id.filledEmailTextField);
        mailLayout.setStartIconDrawable(R.drawable.mail);
        mailLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));

        TextInputLayout passwordLayout = findViewById(R.id.filledPasswordTextField);
        passwordLayout.setStartIconDrawable(R.drawable.password);
        passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.purple_500));


        email = mailLayout.getEditText();
        password = passwordLayout.getEditText();

        EmailValidator.addEmailValidator(email);
        PasswordValidator.addPasswordValidator(password);

        Button btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(view1 -> {

            if(!validateDetails()){
                return;
            }

            BackgroundTask<String> bgtask = new BackgroundTask<>(new BackgroundTask.BackgroundTaskExecutor<String>() {
                @Override
                public String run() throws Exception {

                    Login login = new Login(email.getText().toString(), password.getText().toString());

                    APICallUtils.makeApiCallWithRetry(ApiList.Login.getApi(), MethodType.POST.toString(),
                            JsonUtils.getJsonObject(login),  new Callback() {
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    if(response.isSuccessful()){

                                        runOnUiThread(() -> new CustomAlertUtils(view2 -> {

                                            try {
                                                String responseData = response.body().string();
                                                JSONObject json = new JSONObject(responseData);
                                                new SharedPreference(getApplicationContext()).createSharedPreference(json);
                                            }catch(JSONException e){
                                                e.printStackTrace();
                                            }
                                            catch (IOException e) {
                                               e.printStackTrace();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            ActivityUtil.nextScreen(MainActivity.this, ComplaintListActivity.class, null);

                                        }, findViewById(R.id.layoutDialogContainer))
                                                .showCustomAlertDialog("Login Successfully.", getBaseContext()));


                                                                           }
                                }

                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    e.printStackTrace();
                                    Snackbar mySnackbar = Snackbar.make(view1, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                                    mySnackbar.show();
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









        });

       initializeButtonClick();
    }

    private void initializeButtonClick(){
        registerButton();
    }

    private void registerButton() {
        TextView clickLogin = findViewById(R.id.loginRegister);
        clickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.nextScreen(MainActivity.this, register.class, null);
            }
        });

    }


    public boolean validateDetails(){

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