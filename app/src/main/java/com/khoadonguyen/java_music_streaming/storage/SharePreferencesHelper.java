package com.khoadonguyen.java_music_streaming.storage;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharePreferencesHelper {
     static String prefname = "MusicStorage";
    private static final String logTag = "SharePreferencesHelper";

    /**
     * init sharedpreferences
     *
     * @param context
     * @return
     */
    private static SharedPreferences initSharedPreferences(Context context) {

        return context.getSharedPreferences(prefname, context.MODE_PRIVATE);
    }

    public static void setString(String key, String value, Context context) {
        try {
            SharedPreferences sharedPreferences = initSharedPreferences(context);

            sharedPreferences.edit().putString(key, value).apply();
            Log.d(logTag, "Save successfully key :" + key + "value :" + value);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String key, Context context) {
        try {
            SharedPreferences sharedPreferences = initSharedPreferences(context);
            String value = sharedPreferences.getString(key, null);
            Log.d(logTag, "get successfully key :" + key + "value :" + value);
            return value;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setInt(String key, Integer value, Context context) {
        try {
            SharedPreferences sharedPreferences = initSharedPreferences(context);

            sharedPreferences.edit().putInt(key, value).apply();
            Log.d(logTag, "Save successfully key :" + key + "value :" + value);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getInt(String key, Context context) {
        try {
            SharedPreferences sharedPreferences = initSharedPreferences(context);

            int value = sharedPreferences.getInt(key, 0);
            Log.d(logTag, "get successfully key :" + key + "value :" + value);
            return value;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
