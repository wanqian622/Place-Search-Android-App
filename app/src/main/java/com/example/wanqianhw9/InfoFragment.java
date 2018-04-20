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


        placeId = getActivity().getIntent().getExtras().getString("PlaceID");
        String address = this.getArguments().getString("address");
        Log.d("address",address);

        Bundle bundle = getArguments();
        if(bundle != null){
            mErr.setVisibility(View.GONE);
            String getAddress = bundle.getString("address");
            Log.d("getAdd",getAddress);
            if(getAddress !=  null){
                mAddressLinearLayout.setVisibility(View.VISIBLE);
                mAddressTextView.setText(getAddress);
            } else{
                mAddressLinearLayout.setVisibility(View.GONE);
            }

            String getPhone = bundle.getString("phone");
            if(getPhone != null){
                mPhoneLinearLayout.setVisibility(View.VISIBLE);
                mPhoneTextView.setText(getPhone);
            }else{
                mPhoneLinearLayout.setVisibility(View.GONE);
            }

            String getWebsite = bundle.getString("website");
            if(getWebsite != null){
                mWebsiteLinearLayout.setVisibility(View.VISIBLE);
                mWebsiteView.setText(getWebsite);
            }else{
                mWebsiteLinearLayout.setVisibility(View.GONE);
            }

            String getGooglePage = bundle.getString("googlePage");
            if(getGooglePage != null){
                mGooglePageLinearLayout.setVisibility(View.VISIBLE);
                mGooglePageTextView.setText(getGooglePage);
            }else{
                mGooglePageLinearLayout.setVisibility(View.GONE);
            }

            String priceLevel = bundle.getString("price");
            if(priceLevel != null){
                mPriceLinearLayout.setVisibility(View.VISIBLE);
                mPriceTextView.setText(priceLevel);
            }else{
                mPriceLinearLayout.setVisibility(View.GONE);
            }

            double rating = bundle.getDouble("rating");
            if(rating != -1){
                mRatingLinearLayout.setVisibility(View.VISIBLE);
                mRatingTextView.setRating((float)rating);
            }else{
                mRatingLinearLayout.setVisibility(View.GONE);
            }

        } else{
            mErr.setVisibility(View.VISIBLE);
        }

        return mView;
    }

}
