package com.municipal.complaintsapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Switch;

import androidx.core.content.ContextCompat;

import com.municipal.complaintsapp.classes.Registration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.function.Predicate;

public class SharedPreference {

    // creating constant keys for shared preferences.
    public  final String SHARED_PREFS = "shared_prefs";
    public  SharedPreferences sharedpreferences;
    private final Context context;

    public SharedPreference(Context context){
        this.context = context;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }



    public void createSharedPreference(JSONObject json) throws JSONException {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        Iterator<String> jsonIterator = json.keys();
        while (jsonIterator.hasNext()){
            String key = jsonIterator.next();
            Object value = json.get(key);

            if(value == null){
                continue;
            }

            if(value instanceof String){
                editor.putString(key, String.valueOf(value));
            } else if (value instanceof Integer) {
                editor.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Double) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof  Boolean) {
                editor.putBoolean(key, ((Boolean) value).booleanValue());
            }

        }


        editor.apply();
    }


    public boolean isSharedPreferenceAvailable(){
        return sharedpreferences.getAll().size() > 0 ;
    }

    public int getId(){
        return (int) sharedpreferences.getAll().get("Id");
    }
}
