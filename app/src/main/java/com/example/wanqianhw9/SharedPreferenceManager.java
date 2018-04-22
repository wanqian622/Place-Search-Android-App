package com.example.wanqianhw9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

/**
 * Created by anwanqi on 4/21/18.
 */

public class SharedPreferenceManager {
    // Shared preferences file name
    private static final String PREF_NAME = "AndroidFavouriteList";
    // LogCat tag
    private static String TAG = SharedPreferenceManager.class.getSimpleName();
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static SharedPreferenceManager instance;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static SharedPreferenceManager getInstance(Context context){
        if(instance == null){
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private SharedPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setFavourite(String symbol, String json_string) {
        editor.putString(symbol, json_string);
        editor.commit();
        Log.d(TAG, "User set favourites");

    }


    public boolean isFavourite(String symbol) {
        return pref.contains(symbol);
    }

    public void removeFavourite(String symbol) {
        editor.remove(symbol);
        editor.commit();
        Log.d(TAG, "User remove favourites");


    }

    public Map<String, ?> getAll() {
        return pref.getAll();
    }

    public String getFavourite(String symbol) {
        return pref.getString(symbol, "-1");
    }
}
