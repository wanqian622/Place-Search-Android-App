package com.example.wanqianhw9;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    private final String APIKEY = "AIzaSyA0TgG8WO6h1YnWcc41_kkS_xFD7tFT1dw";
    private String placeId;
    private Bundle bundle;
    private String getTitle;

    private int[] tabIcons = {
            R.mipmap.ic_info_outline_white_24dp,
            R.mipmap.ic_insert_photo_white_24dp,
            R.mipmap.ic_directions_white_24dp,
            R.mipmap.ic_rate_review_white_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        mReviewsFragment = new ReviewsFragment();
        mInfoFragment = new InfoFragment();
        getTitle = intent.getExtras().getString("PlaceName");
        placeId = intent.getExtras().getString("PlaceID");
        bundle = new Bundle();
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
        adapter.addFragment(new PhotosFragment(), "PHOTOS");
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
                        String getAddress = data.getString("formatted_address");
                        if(getAddress != null && getAddress != ""){

                            bundle.putString("address",getAddress);
                        }
                        String getPhone = data.getString("formatted_phone_number");
                        if(getPhone != null && getPhone != ""){
                            bundle.putString("phone",getPhone);
                        }
                        String getWebsite = data.getString("website");
                        if(getWebsite != null && getWebsite != ""){
                            bundle.putString("website",getWebsite);
                        }
                        String getGooglePage = data.getString("url");
                        if(getGooglePage != null && getGooglePage != ""){
                            bundle.putString("googlePage",getGooglePage);
                        }

                        Integer level = new Integer(data.getInt("price_level"));
                        if(level != null){
                            String priceLevel = "$";
                            for(int i = 0; i < level; i++){
                                priceLevel += "$";
                            }
                            bundle.putString("price",priceLevel);
                        }
                        Double getRating = data.getDouble("rating");
                        if(getRating != null){
                            bundle.putDouble("rating",getRating);
                        }

                        mInfoFragment.setArguments(bundle);
                        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setTitle(getTitle);

                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


                        viewPager = (ViewPager) findViewById(R.id.containerDetails);
                        setupViewPager(viewPager);

                        tabLayout = (TabLayout) findViewById(R.id.tabsDetails);
                        tabLayout.setupWithViewPager(viewPager);
                        setupTabIcons();


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

                    } else{
//                        mErr.setVisibility(View.VISIBLE);
                        Log.d("err","error");
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


}
