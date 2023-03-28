package com.municipal.complaintsapp.API;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APICallUtils {

    public static void makeApiCallWithRetry(String url, String method, JSONObject jsonObject, final Callback callback, int retryCount) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        Request.Builder builder = new Request.Builder().url(url);

        if (method.equalsIgnoreCase("POST")) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            builder.post(body);
        } else if (method.equalsIgnoreCase("GET")) {
            builder.get();
        } else if (method.equalsIgnoreCase("PUT")) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            builder.put(body);
        }

        Request request = builder.build();

        client.newCall(request).enqueue(new RetryableCallback(retryCount) {
            @Override
            public void onFinalResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }

            @Override
            public void onFinalFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


//public void makeApiCallWithRetry(url, method, jsonObject, new Callback() {
//    @Override
//    public void onResponse(Response response) throws IOException {
//        if (response.isSuccessful()) {
//            // Handle the response here
//        }
//    }
//}, 3);


}
