package com.municipal.complaintsapp.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void addPasswordValidator(EditText passwordEditText) {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString().trim())) {
                    passwordEditText.setError("Please enter your " +passwordEditText.getHint() +".");
                }
               else if (!isPasswordValid(s.toString().trim())) {
                    passwordEditText.setError("Password must be at least 8 characters long and contain at least one number, one lowercase letter, one uppercase letter, and one symbol.");
                } else {
                    passwordEditText.setError(null);
                }
            }
        });

    }



}