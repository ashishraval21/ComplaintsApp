package com.municipal.complaintsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.municipal.complaintsapp.util.ActivityUtil;

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

        Button login = findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout emailLayout = findViewById(R.id.filledEmailTextField);
                String email = emailLayout.getEditText().getText().toString();

                TextInputLayout passwordLayout = findViewById(R.id.filledPasswordTextField);
                String password = passwordLayout.getEditText().getText().toString();





            }
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