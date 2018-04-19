package com.example.wanqianhw9;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    private Spinner mReviewsSpinner;
    private Spinner mOrderSpinner;
    private View mView;


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
        String defaultOrder = "Default orde";
        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_array, android.R.layout.simple_spinner_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderSpinner.setAdapter(orderAdapter);
        int orderSpinnerPosition = orderAdapter.getPosition(defaultOrder);
        mOrderSpinner.setSelection(orderSpinnerPosition);
        mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return mView;
    }

}
