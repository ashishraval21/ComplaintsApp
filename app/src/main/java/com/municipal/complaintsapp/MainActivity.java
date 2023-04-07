package com.municipal.complaintsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.municipal.complaintsapp.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

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

        Button btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(view1 -> {
            TextInputLayout emailLayout = findViewById(R.id.filledEmailTextField);
            String email = emailLayout.getEditText().getText().toString();

            TextInputLayout passwordLayout1 = findViewById(R.id.filledPasswordTextField);
            String password = passwordLayout1.getEditText().getText().toString();


            Login login = new Login(email, password);

            APICallUtils.makeApiCallWithRetry(ApiList.Login.getApi(), MethodType.POST.toString(),
                    JsonUtils.getJsonObject(login),  new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if(response.isSuccessful()){

                                try {
                                    String responseData = response.body().string();
                                    JSONObject json = new JSONObject(responseData);
                                   // SharedPreference.createSharedPreference(json);
                                    new SharedPreference(getApplicationContext()).createSharedPreference(json);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                Snackbar mySnackbar = Snackbar.make(view1, "Login Successful.", BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();
                                ActivityUtil.nextScreen(MainActivity.this, add_complaints.class, null);
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


}