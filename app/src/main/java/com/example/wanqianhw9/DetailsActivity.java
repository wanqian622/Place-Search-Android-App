package com.example.wanqianhw9;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ReviewsFragment mReviewsFragment;
    private InfoFragment mInfoFragment;
    private PhotosFragment mPhotosFragment;
    private final String APIKEY = "AIzaSyA0TgG8WO6h1YnWcc41_kkS_xFD7tFT1dw";
    private String placeId;
    private double placeLat;
    private double placeLng;
    private String placeImg;
    private String placeAddress;
    private Bundle bundle;
    private String getTitle;
    private ImageView mFav;
    private ImageView mShareTwitter;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    public static List<PlacePhotoMetadata> photosList;
    public static List<Reviews> googleReviews;
    public static List<Reviews> yelpReviews;
    private String zipCode;
    private String country;
    private String city;
    private String state;
    private String getAddress;
    private String bestId;
    private String getGooglePage;
    private String getWebsite;


    private int[] tabIcons = {
            R.drawable.info_outline,
            R.drawable.photos,
            R.drawable.maps,
            R.drawable.review
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mReviewsFragment = new ReviewsFragment();
        mInfoFragment = new InfoFragment();
        mPhotosFragment = new PhotosFragment();
        photosList = new ArrayList<PlacePhotoMetadata>();
        googleReviews = new ArrayList<Reviews>();
        yelpReviews = new ArrayList<Reviews>();
        mFav= (ImageView) findViewById(R.id.favorite_img);
        mShareTwitter = (ImageView) findViewById(R.id.share_img);
        getTitle = intent.getExtras().getString("PlaceName");
        placeId = intent.getExtras().getString("PlaceID");
        placeLat = intent.getExtras().getDouble("placeLat");
        placeLng = intent.getExtras().getDouble("placeLng");
        placeAddress= intent.getExtras().getString("placeAddress");
        placeImg= intent.getExtras().getString("placeImgUrl");
        bundle = new Bundle();
        getWebsite = "";
        getGooglePage = "";
        zipCode = "";
        country = "";
        city= "";
        state = "";
        getAddress = "";
        bestId = "";
        if(SharedPreferenceManager.getInstance(getApplicationContext()).isFavourite(placeId)){
            mFav.setImageResource(R.drawable.heart_fill_white);
        } else{
            mFav.setImageResource(R.drawable.heart_outline_white);
        }

        mShareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                String twit_url = "";
                String twitterMessage = "Check out " + getTitle;
                try {
                    twitterMessage += " located at " + placeAddress;
                    twit_url = getWebsite;
                    if (twit_url.equals("")) {
                        twit_url = getGooglePage;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                twitterMessage += ". Website: ";
                String twitterURL = GetURLS.TWITTER + "?text=" + Uri.encode(twitterMessage) + "&url=" + Uri.encode(twit_url) + "&hashtags=TravelAndEntertainmentSearch";
                intent.setData(Uri.parse(twitterURL));
                startActivity(intent);
            }
        });

        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toastText = getTitle;
                if(SharedPreferenceManager.getInstance(getApplicationContext()).isFavourite(placeId)){
                    toastText += " was removed from favorites";
                    mFav.setImageResource(R.drawable.heart_outline_white);
                    SharedPreferenceManager.getInstance(getApplicationContext()).removeFavourite(placeId);
                } else{
                    toastText += " was added to favorites";
                    mFav.setImageResource(R.drawable.heart_fill_white);
                    SearchResults newOne = new SearchResults(placeImg,getTitle,placeAddress,placeId,placeLat,placeLng);
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(newOne);
                    SharedPreferenceManager.getInstance(getApplicationContext()).setFavourite(placeId,jsonInString);
                }
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        requestForDetails();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mInfoFragment, "INFO");
        adapter.addFragment( mPhotosFragment, "PHOTOS");
        adapter.addFragment(new MapFragment(), "MAP");
        adapter.addFragment(mReviewsFragment, "REVIEWS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    // get results
    private void requestForDetails(){


        String queryUrl = GetURLS.PLACEDETAILS + placeId + "&key=" + APIKEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getJSONObject("result") != null){
                        JSONObject data = jObj.getJSONObject("result");
                        try{

                            getAddress = data.getString("formatted_address");
                            if(getAddress != null && getAddress != ""){
                                bundle.putString("address",getAddress);
                            }

                        }
                        catch(Exception e){
                            bundle.putString("address",null);
                        }

                        try{
                            String getPhone = data.getString("formatted_phone_number");
                            if(getPhone != null && getPhone != ""){
                                bundle.putString("phone",getPhone);
                            }

                        } catch(Exception e){
                            bundle.putString("phone",null);
                        }

                        try{
                            getWebsite = data.getString("website");
                            if(getWebsite != null && getWebsite != ""){
                                bundle.putString("website",getWebsite);
                            }

                        } catch(Exception e){

                            bundle.putString("website",null);
                        }

                        try{

                            getGooglePage = data.getString("url");
                            if(getGooglePage != null && getGooglePage != ""){
                                bundle.putString("googlePage",getGooglePage);
                            }

                        } catch(Exception e){
                            bundle.putString("googlePage",null);
                        }

                        try{
                            Integer level = new Integer(data.getInt("price_level"));
                            if(level != null){
                                String priceLevel = "$";
                                for(int i = 0; i < level; i++){
                                    priceLevel += "$";
                                }
                                bundle.putString("price",priceLevel);
                            }

                        } catch(Exception e){
                            bundle.putString("price",null);
                        }

                        try{
                            Double getRating = data.getDouble("rating");
                            if(getRating != null){
                                bundle.putDouble("rating",getRating);
                            }

                        } catch(Exception e){
                            bundle.putDouble("rating",-1);
                        }

                        try{
                            JSONArray array = data.getJSONArray("reviews");
                            if(array != null && array.length() > 0){
                                for(int i = 0; i < array.length();i++){
                                    JSONObject obj = array.getJSONObject(i);
                                    String author = obj.getString("author_name");
                                    String author_url = obj.getString("author_url");
                                    String profile = obj.getString("profile_photo_url");
                                    double rate = obj.getDouble("rating");
                                    String text = obj.getString("text");
                                    long timeStamp = obj.getLong("time") * 1000;
                                    String time = getDate(timeStamp,"yyyy-MM-dd HH:mm:ss");
                                    Reviews res = new Reviews(profile,author,author_url,rate,text,time);
                                    googleReviews.add(res);
                                }

                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        try{
                            JSONArray add_component = data.getJSONArray("address_components");
                            if(add_component != null && add_component.length() > 0){
                                for(int k = 0; k < add_component.length(); k++){
                                    JSONObject obj1 = add_component.getJSONObject(k);
                                    JSONArray obj2 = obj1.getJSONArray("types");
                                    String addType = obj2.getString(0);
                                    if(addType.equals("country")){
                                        country = obj1.getString("short_name");
                                    }
                                    if(addType.equals("postal_code")){
                                        zipCode= obj1.getString("short_name");
                                    }
                                    if(addType.equals("locality")){
                                        city = obj1.getString("short_name");
                                    }
                                    if(addType.equals("administrative_area_level_1")){
                                        state = obj1.getString("short_name");
                                    }
                                }
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        mInfoFragment.setArguments(bundle);
                        requestForYelp();
                    } else{
//                        mErr.setVisibility(View.VISIBLE);
                        requestForYelp();
                        Log.d("err","error");
                    }

                } catch (Exception e) {
//                    mErr.setVisibility(View.VISIBLE);
                    requestForYelp();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mErr.setVisibility(View.VISIBLE);
                requestForYelp();
                error.printStackTrace();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "info");
    }

    private  String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    // photoMetadata
    public void getPhotoMetadata() {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                PlacePhotoMetadataResponse photos = task.getResult();
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                for(PlacePhotoMetadata photoMetadata : photoMetadataBuffer){
                    photosList.add(photoMetadata.freeze());
                    //photoMetadataBuffer.get(0).freeze()
                }
                photoMetadataBuffer.release();
                toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(getTitle);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                viewPager = (ViewPager) findViewById(R.id.containerDetails);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.tabsDetails);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }
        });
    }

    private void requestForYelp(){
        String[] add = getAddress.split(",");
        String matchAddress1 = add[0];
        String matchAddress2 = add[1] + "," + add[2];
        String yelpURL = GetURLS.YELPMATCH + "matchName=" + Uri.encode(getTitle) + "&matchAddress1=" + Uri.encode(matchAddress1) + "&matchAddress2=" + Uri.encode(matchAddress2) + "&matchCity=" + Uri.encode(city) + "&matchState=" + state + "&matchCountry=" + country + "&matchZipCode=" + zipCode;
        Log.d("yelpUrl",yelpURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, yelpURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getString("bestId") != null && jObj.getInt("error") == 0){
                        bestId = jObj.getString("bestId");
                        requestForYelpReviews();
                    }

                } catch (Exception e) {
//                    mErr.setVisibility(View.VISIBLE);
                    getPhotoMetadata();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mErr.setVisibility(View.VISIBLE);
                getPhotoMetadata();
                error.printStackTrace();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "yelp");

    }

    private void requestForYelpReviews(){
        String yelpUrl = GetURLS.YELPREVIEWS +bestId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, yelpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getInt("error") == 0 && jObj.getJSONArray("data") != null && jObj.getJSONArray("data").length() > 0){
                        JSONArray arr = jObj.getJSONArray("data");
                        for(int i = 0; i < arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);
                            String author_url = obj.getString("url");
                            String text = obj.getString("text");
                            double rate = obj.getDouble("rating");
                            String time = obj.getString("time_created");
                            JSONObject obj2 = obj.getJSONObject("user");
                            String author = obj2.getString("name");
                            String profile = obj2.getString("image_url");
                            Reviews res = new Reviews(profile,author,author_url,rate,text,time);
                            yelpReviews.add(res);
                        }
                    }

                } catch (Exception e) {
//                    mErr.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mErr.setVisibility(View.VISIBLE);
                error.printStackTrace();
            }
        }) {

        };
        getPhotoMetadata();
        AppController.getInstance().addToRequestQueue(stringRequest, "yelpReviews");
    }


}
