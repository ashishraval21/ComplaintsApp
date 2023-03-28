package com.municipal.complaintsapp.API;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExampleCallAPi {

    public static void mainExample() {
        String url = "http://12.22.111.231:8080";
        String method = "POST";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", "user@example.com");
            jsonObject.put("password", "password123");
        } catch ( JSONException e) {
            e.printStackTrace();
        }

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        };

        int retryCount = 3; // Number of times to retry the API call
       APICallUtils.makeApiCallWithRetry(url, method, jsonObject, callback, retryCount);

    }
}
