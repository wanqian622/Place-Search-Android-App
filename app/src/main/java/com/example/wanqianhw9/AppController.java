package com.example.wanqianhw9;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anwanqi on 4/13/18.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

//    public static ArrayList<String> requestForAutoCompleteList(String symbol) {
//        String paras = "?symbol=" + symbol;
//        final ArrayList<String> autoCompleteItems = new ArrayList<>();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppURLs.AUTOCOMPLETE + paras, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jObj = null;
//                try {
//                    Log.d("response from autocomplete", response);
//                    jObj = new JSONObject(response);
//
//                    int error = jObj.getInt("error");
//                    if (error == 0) {
//                        JSONArray jsonArray = jObj.getJSONArray("data");
//                        int size = jsonArray.length();
//                        if (size > 5) {
//                            size = 5;
//                        }
//                        for (int i = 0; i < size; i++) {
//                            JSONObject items = jsonArray.getJSONObject(i);
//                            String symbol = items.getString("Symbol");
//                            String name = items.getString("Name");
//                            String exchange = items.getString("Exchange");
//                            autoCompleteItems.add(new AutoCompleteItem(symbol, name, exchange).toString());
//                        }
//                    } else {
//
//                    }
//
//
//                } catch (JSONException e) {
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//        };
//        AppController.getInstance().addToRequestQueue(stringRequest, "AutoComplete");
//        return autoCompleteItems;
//    }
}
