package com.municipal.complaintsapp.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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


        public static <T> T fromJson(JSONObject json, Class<T> classOfT) throws JSONException {
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), classOfT);
        }


        public  static <T> T fromJsonWithDate(JSONObject json, Class<T> classOfT){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // set the date format to match the date string
            gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    String dateStr = json.getAsJsonPrimitive().getAsString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    format.setTimeZone(TimeZone.getDefault());
                    try {
                        return format.parse(dateStr);
                    } catch (ParseException e) {
                        throw new JsonParseException("Failed to parse date " + dateStr, e);
                    }
                }
            });

            return gsonBuilder.create().fromJson(json.toString(), classOfT);
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
