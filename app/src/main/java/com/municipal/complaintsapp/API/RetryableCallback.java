package com.municipal.complaintsapp.API;

import android.os.Handler;
import android.os.Looper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


abstract class RetryableCallback implements Callback {

    private int retryCount;

    RetryableCallback(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (retryCount > 0) {
            int delay = (int) (1000 + (Math.random() * 2000));
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                retryCount--;
                call.clone().enqueue(this);
            }, delay);
        } else {
            onFinalFailure(call, e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!APICallUtils.isCallSuccess(response)) {

            try {
                String responseData = response.body().string();
                JSONObject json = null;
                json = new JSONObject(responseData);
                onFinalFailure(call,  new IOException(json.getString("Message")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            onFinalResponse(call, response);
        }
    }

    public abstract void onFinalResponse(Call call, Response response) throws IOException;
    public abstract void onFinalFailure(Call call, IOException e);
}
