package com.example.wanqianhw9;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    private Spinner mReviewsSpinner;
    private Spinner mOrderSpinner;
    private View mView;
    private TextView mErr;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Reviews> googleReviews;
    private List<Reviews> defaultGoogleReviews;
    private List<Reviews> defaultYelpReviews;
    private List<Reviews> yelpReviews;
    private String getReviewsType;
    private String getOrderType;
    private static final String GOOGLE = "Google reviews";
    private static final String YELP = "Default order";
    private static final String DEFAULT_ORDER = "Default order";
    private static final String HIGHEST_RATING = "Highest rating";
    private static final String LOWEST_RATING = "Lowest rating";
    private static final String MOST_RECENT = "Most recent";
    private static final String LEAST_RECENT = "Least recent";


    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mReviewsSpinner= mView.findViewById(R.id.reviews_spinner);
        mOrderSpinner = mView.findViewById(R.id.order_spinner);
        recyclerView = mView.findViewById(R.id.reviews_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mErr = mView.findViewById(R.id.reviewsErrorMessage);
        googleReviews = DetailsActivity.googleReviews;
        defaultGoogleReviews = new ArrayList<>(DetailsActivity.googleReviews);
        yelpReviews = DetailsActivity.yelpReviews;
        defaultYelpReviews = new ArrayList<>(DetailsActivity.yelpReviews);
        getReviewsType = GOOGLE;
        getOrderType = DEFAULT_ORDER;


        // review spinner
        ArrayAdapter<CharSequence> reviewsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.reviews_array, android.R.layout.simple_spinner_item);
        reviewsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReviewsSpinner.setAdapter(reviewsAdapter);
        int reviewsSpinnerPosition = reviewsAdapter.getPosition(getReviewsType);
        mReviewsSpinner.setSelection(reviewsSpinnerPosition);
        // order spinner
        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_array, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderSpinner.setAdapter(orderAdapter);
        int orderSpinnerPosition = orderAdapter.getPosition(getOrderType);
        mOrderSpinner.setSelection(orderSpinnerPosition);


        mReviewsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getReviewsType = parent.getItemAtPosition(position).toString();
                    if(getReviewsType != null){
                        switch(getReviewsType) {
                            case GOOGLE:
                                if(defaultGoogleReviews.size() < 1 || googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                }
                                else if(getOrderType.equals(DEFAULT_ORDER)){
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(defaultGoogleReviews);
                                } else if(getOrderType.equals(HIGHEST_RATING)){
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? -1 : 1;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(googleReviews);
                                } else if(getOrderType.equals(LOWEST_RATING)){

                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? 1 : -1;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(googleReviews);
                                } else if(getOrderType.equals(MOST_RECENT)){
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? -1 : 1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(googleReviews);

                                } else{

                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? 1 : -1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(googleReviews);
                                }
                                break;
                            case YELP:
                                if(defaultYelpReviews.size() < 1 || yelpReviews.size() < 1){
                                    mErr.setVisibility(View.VISIBLE);
                                    setUpReviews(defaultYelpReviews);
                                } else if(getOrderType.equals(DEFAULT_ORDER)){
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(defaultYelpReviews);
                                } else if(getOrderType.equals(HIGHEST_RATING)){
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? -1 : 1;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(yelpReviews);
                                } else if(getOrderType.equals(LOWEST_RATING)){

                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? 1 : -1;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(yelpReviews);
                                } else if(getOrderType.equals(MOST_RECENT)){
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? -1 : 1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(yelpReviews);

                                } else{

                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? 1 : -1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(yelpReviews);
                                }
                                break;
                        }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getOrderType = parent.getItemAtPosition(position).toString();
                if(getOrderType != null){
                    switch (getOrderType){
                        case DEFAULT_ORDER:
                            if(getReviewsType.equals(GOOGLE)){
                                if(defaultGoogleReviews.size() < 1 || googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(defaultGoogleReviews);
                                }
                            } else{
                                if(defaultYelpReviews.size() < 1 || yelpReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    setUpReviews(defaultYelpReviews);
                                }
                            }
                            break;
                        case HIGHEST_RATING:
                            if(getReviewsType.equals(GOOGLE)){
                                if(googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? -1 : 1;
                                        }
                                    });
                                    setUpReviews(googleReviews);
                                }
                            } else{
                                if(yelpReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? -1 : 1;
                                        }
                                    });
                                    setUpReviews(yelpReviews);
                                }
                            }
                            break;
                        case LOWEST_RATING:
                            if(getReviewsType.equals(GOOGLE)){
                                if(googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? 1 : -1;
                                        }
                                    });
                                    setUpReviews(googleReviews);
                                }
                            } else{
                                if(yelpReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            if(o1.getRate() == o2.getRate()){
                                                return 0;
                                            }

                                            return o1.getRate() > o2.getRate() ? 1 : -1;
                                        }
                                    });
                                    setUpReviews(yelpReviews);
                                }
                            }
                            break;
                        case MOST_RECENT:
                            if(getReviewsType.equals(GOOGLE)){
                                if(googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? -1 : 1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    setUpReviews(googleReviews);
                                }
                            } else{
                                if(yelpReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? -1 : 1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    setUpReviews(yelpReviews);
                                }
                            }
                            break;
                        case LEAST_RECENT:
                            if(getReviewsType.equals(GOOGLE)){
                                if(googleReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(googleReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? 1 : -1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    setUpReviews(googleReviews);
                                }

                            } else{
                                if(yelpReviews.size() < 1){
                                    setUpReviews(new ArrayList<Reviews>());
                                    mErr.setVisibility(View.VISIBLE);
                                } else{
                                    mErr.setVisibility(View.GONE);
                                    Collections.sort(yelpReviews, new Comparator<Reviews>() {
                                        @Override
                                        public int compare(Reviews o1, Reviews o2) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try{
                                                Date date1 = format.parse(o1.getReview_time());
                                                Date date2 = format.parse(o2.getReview_time());
                                                if(date1.compareTo(date2) == 0){
                                                    return 0;
                                                }

                                                return date1.compareTo(date2) > 0 ? 1 : -1;
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });
                                    setUpReviews(yelpReviews);
                                }
                            }
                            break;
                    }
                } else{
                    setUpReviews(new ArrayList<Reviews>());
                    mErr.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return mView;
    }

    private void setUpReviews(List<Reviews> reviews){

        mAdapter = new ReviewsListAdapter(reviews,getContext());
        recyclerView.setAdapter(mAdapter);
    }




}
