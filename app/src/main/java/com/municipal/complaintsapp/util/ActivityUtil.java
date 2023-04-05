package com.municipal.complaintsapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.municipal.complaintsapp.MainActivity;
import com.municipal.complaintsapp.register;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActivityUtil {

    public static void nextScreen(Context currentActivity, Class<?> nextActivity, HashMap<String, Object> params){
        Intent myIntent = new Intent(currentActivity, nextActivity);
        if(!isNullOrEmptyMap(params)){
            myIntent.putExtra("params",params);
        }
        currentActivity.startActivity(myIntent);
    }


    public static boolean isNullOrEmptyMap(Map <? , ?> map) {
        return (map == null || map.isEmpty());
    }

}
