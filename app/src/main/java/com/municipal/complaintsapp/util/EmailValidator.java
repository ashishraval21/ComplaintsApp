package com.municipal.complaintsapp.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {


    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void addEmailValidator(EditText emailEditText) {
        emailEditText.addTextChangedListener(new TextWatcher() {
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
                    emailEditText.setError("Please Enter Email Id.");
                }
                else if (!isEmailValid(s.toString().trim())) {
                    emailEditText.setError("Invalid Email Id.");
                } else {
                    emailEditText.setError(null);
                }
            }
        });

    }



}
