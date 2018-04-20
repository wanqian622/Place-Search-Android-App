package com.example.wanqianhw9;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.PlaceBufferResponse;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.PlaceDetectionClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    //    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
    private String placeId;
    private View mView;
    private TextView mAddressTextView;
    private TextView mPhoneTextView;
    private TextView mPriceTextView;
    private RatingBar mRatingTextView;
    private TextView mGooglePageTextView;
    private TextView mWebsiteView;
    private TextView mErr;
    private LinearLayout mAddressLinearLayout;
    private LinearLayout mPhoneLinearLayout;
    private LinearLayout mPriceLinearLayout;
    private LinearLayout mRatingLinearLayout;
    private LinearLayout mGooglePageLinearLayout;
    private LinearLayout mWebsiteLinearLayout;
//    private final String APIKEY = "AIzaSyA0TgG8WO6h1YnWcc41_kkS_xFD7tFT1dw";
    private List<Reviews> googleReviews;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_info, container, false);
        mAddressTextView = (TextView) mView.findViewById(R.id.info_address);
        mPhoneTextView = (TextView) mView.findViewById(R.id.info_phone_number);
        mPriceTextView = (TextView) mView.findViewById(R.id.info_price_level);
        mRatingTextView = (RatingBar) mView.findViewById(R.id.info_ratingBar);
        mGooglePageTextView = (TextView) mView.findViewById(R.id.info_google_page);
        mWebsiteView = (TextView) mView.findViewById(R.id.info_website);
        mErr = (TextView) mView.findViewById(R.id.infoErrorMessage) ;
        mAddressLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_address);
        mPhoneLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_phone);
        mPriceLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_price);
        mRatingLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_rating);
        mGooglePageLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_googlePage);
        mWebsiteLinearLayout = (LinearLayout) mView.findViewById(R.id.info_title_website);
        googleReviews = new ArrayList<Reviews>();

        placeId = getActivity().getIntent().getExtras().getString("PlaceID");
        String address = this.getArguments().getString("address");
        Log.d("address",address);

        Bundle bundle = getArguments();
        if(bundle != null){
            mErr.setVisibility(View.GONE);
            String getAddress = bundle.getString("address");
            if(getAddress != null){
                mAddressLinearLayout.setVisibility(View.VISIBLE);
                mAddressTextView.setText(getAddress);
            }

            String getPhone = bundle.getString("phone");
            if(getPhone != null){
                mPhoneLinearLayout.setVisibility(View.VISIBLE);
                mPhoneTextView.setText(getPhone);
            }

            String getWebsite = bundle.getString("website");
            if(getWebsite != null){
                mWebsiteLinearLayout.setVisibility(View.VISIBLE);
                mWebsiteView.setText(getWebsite);
            }

            String getGooglePage = bundle.getString("googlePage");
            if(getGooglePage != null){
                mGooglePageLinearLayout.setVisibility(View.VISIBLE);
                mGooglePageTextView.setText(getGooglePage);
            }

            String priceLevel = bundle.getString("price");
            if(priceLevel != null){
                mPriceLinearLayout.setVisibility(View.VISIBLE);
                mPriceTextView.setText(priceLevel);
            }

            Double rating = bundle.getDouble("rating");
            if(rating != null){
                mRatingLinearLayout.setVisibility(View.VISIBLE);
                mRatingTextView.setRating(rating.floatValue());
            }

        } else{
            mErr.setVisibility(View.VISIBLE);
        }

        


        return mView;
    }


//    OnReviewsGetList mCallback;
//
//    public interface OnReviewsGetList{
//        public void onReviewsGetter(List<Reviews> reviews);
//    }
//
//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//        try {
//            mCallback = (OnReviewsGetList) context;
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    // get results
//    private void requestForDetails(){
//
//
//        String queryUrl = GetURLS.PLACEDETAILS + placeId + "&key=" + APIKEY;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jObj = null;
//                try {
//                    jObj = new JSONObject(response);
//                    if(jObj.getJSONObject("result") != null){
//                        JSONObject data = jObj.getJSONObject("result");
//                        String getAddress = data.getString("formatted_address");
//                        if(getAddress != null && getAddress != ""){
//                            mAddressLinearLayout.setVisibility(View.VISIBLE);
//                            mAddressTextView.setText(getAddress);
//                        }
//                        String getPhone = data.getString("formatted_phone_number");
//                        if(getPhone != null && getPhone != ""){
//                            mPhoneLinearLayout.setVisibility(View.VISIBLE);
//                            mPhoneTextView.setText(getPhone);
//                        }
//                        String getWebsite = data.getString("website");
//                        if(getWebsite != null && getWebsite != ""){
//                            mWebsiteLinearLayout.setVisibility(View.VISIBLE);
//                            mWebsiteView.setText(getWebsite);
//                        }
//                        String getGooglePage = data.getString("url");
//                        if(getGooglePage != null && getGooglePage != ""){
//                            mGooglePageLinearLayout.setVisibility(View.VISIBLE);
//                            mGooglePageTextView.setText(getGooglePage);
//                        }
//
//                        Integer level = new Integer(data.getInt("price_level"));
//                        if(level != null){
//                            String priceLevel = "$";
//                            for(int i = 0; i < level; i++){
//                                priceLevel += "$";
//                            }
//                            mPriceLinearLayout.setVisibility(View.VISIBLE);
//                            mPriceTextView.setText(priceLevel);
//                        }
//                        Double getRating = data.getDouble("rating");
//                        if(getRating != null){
//                            mRatingLinearLayout.setVisibility(View.VISIBLE);
//                            mRatingTextView.setRating((float)data.getDouble("rating"));
//                        }
//
//                        JSONArray array = data.getJSONArray("reviews");
//                        if(array != null && array.length() > 0){
//                            for(int i = 0; i < array.length();i++){
//                                JSONObject obj = array.getJSONObject(i);
//                                String author = obj.getString("author_name");
//                                String author_url = obj.getString("author_url");
//                                String profile = obj.getString("profile_photo_url");
//                                double rate = obj.getDouble("rating");
//                                String text = obj.getString("text");
//                                long timeStamp = obj.getLong("time");
//                                String time = getDate(timeStamp,"yyyy-MM-dd HH:mm:ss");
//                                Reviews res = new Reviews(profile,author,author_url,rate,text,time);
//                                googleReviews.add(res);
//                            }
//                            mCallback.onReviewsGetter(googleReviews);
//
//                        } else{
//                            mCallback.onReviewsGetter(googleReviews);
//                        }
//
//                    } else{
//                        mErr.setVisibility(View.VISIBLE);
//                    }
//
//                } catch (Exception e) {
////                    mErr.setVisibility(View.VISIBLE);
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mErr.setVisibility(View.VISIBLE);
//            }
//        }) {
//
//        };
//        AppController.getInstance().addToRequestQueue(stringRequest, "info");
//    }
//
//    private  String getDate(long milliSeconds, String dateFormat)
//    {
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//        return formatter.format(calendar.getTime());
//    }

}
