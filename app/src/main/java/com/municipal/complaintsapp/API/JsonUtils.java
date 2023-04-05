package com.municipal.complaintsapp.API;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

public class JsonUtils {

    public static JSONObject getJsonObject(Object o){
        try {
            return new JSONObject(new Gson().toJson(o));
        }
        catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONObject getJsonObjectFromException(String str){
        try {
         return new JSONObject();
        }
        catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }



}
