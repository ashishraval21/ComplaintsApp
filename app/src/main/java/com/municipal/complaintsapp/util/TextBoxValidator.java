package com.municipal.complaintsapp.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextBoxValidator {


    public static void inputValidator(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
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
                    editText.setError("Please enter " + editText.getHint() );
                }
               else {
                    editText.setError(null);
                }
            }
        });

    }
}
