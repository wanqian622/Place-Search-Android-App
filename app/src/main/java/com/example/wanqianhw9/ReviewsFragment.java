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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    private Spinner mReviewsSpinner;
    private Spinner mOrderSpinner;
    private View mView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Reviews> googleReviews;
    private List<Reviews> yelpReviews;


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


        // review spinner
        String defaultValue = "Google reviews";
        ArrayAdapter<CharSequence> reviewsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.reviews_array, android.R.layout.simple_spinner_item);
        reviewsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReviewsSpinner.setAdapter(reviewsAdapter);
        int reviewsSpinnerPosition = reviewsAdapter.getPosition(defaultValue);
        mReviewsSpinner.setSelection(reviewsSpinnerPosition);
        mReviewsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //order spinner
        // review spinner
        String defaultOrder = "Default order";
        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_array, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderSpinner.setAdapter(orderAdapter);
        int orderSpinnerPosition = orderAdapter.getPosition(defaultOrder);
        mOrderSpinner.setSelection(orderSpinnerPosition);
        mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("google",googleReviews.get(0).getAuthor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return mView;
    }

    public void onGetReviews(List<Reviews> reviews){
        googleReviews = reviews;
    }




}
