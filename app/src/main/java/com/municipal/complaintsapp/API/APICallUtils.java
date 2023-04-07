package com.municipal.complaintsapp.API;

import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class APICallUtils {

    static int retryCount = 3;


    public static void makeApiCallWithRetry(String url, String method, JSONObject jsonObject, final Callback callback) {
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
        } else if (method.equalsIgnoreCase("POST_MULTIPART")) {

            MultipartBody.Builder multiPartBodyRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);

            List<MultipartBody.Part> bodyParts = new ArrayList<>();
            try {
                JSONArray images = jsonObject.getJSONArray("images");
                if(images.length() > 0){
                    int i =0;
                    for(; i< images.length(); i++){
                        String path = images.getString(i);
                        File file = new File(images.getString(i));
                        System.out.print("API CALL PATH "+path+"  => "+file.exists());
                        multiPartBodyRequest.addFormDataPart("Attachments", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    }

                }

                multiPartBodyRequest.addFormDataPart("Complaint", jsonObject.toString());
                builder.post(multiPartBodyRequest.build());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

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
                callback.onFailure(call, e);
            }
        });
    }

    public static boolean isCallSuccess(Response response){
        int code = response.code();
        return (code >= 200 && code < 400);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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
