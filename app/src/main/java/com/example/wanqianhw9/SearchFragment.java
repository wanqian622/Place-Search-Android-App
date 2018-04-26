package com.example.wanqianhw9;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText mKeywordEditText;
    private EditText mDistanceEditText;
    //    private EditText mOtherLocEditText;
    private AutoCompleteTextView mSearchPlace;
    private Button mSearchButton;
    private Button mClearButton;
    private TextView mErrKeyword;
    private TextView mErrLoc;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private LocationTracker mLocationTracker;
    private double latitude;
    private double longitude;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(24.7433195, -124.7844079), new LatLng(49.3457868, -66.9513812));
    private String otherLoc;
    private String type;
//    private GeoDataClient geoDataClient;
//    private String otherLoc;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        mKeywordEditText = (EditText) view.findViewById(R.id.editTextKeyword);
        mDistanceEditText = (EditText) view.findViewById(R.id.editTextDistance);
//        mOtherLocEditText = (EditText) view.findViewById(R.id.editTextLoc);
        mSearchPlace = (AutoCompleteTextView) view.findViewById(R.id.search_place);
        mErrKeyword = (TextView) view.findViewById(R.id.errorKeyword);
        mClearButton = (Button) view.findViewById(R.id.clear);
        mErrKeyword.setVisibility(View.GONE);
        mErrLoc = (TextView) view.findViewById(R.id.errorLoc);
        mErrLoc.setVisibility(View.GONE);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mSearchButton = (Button) view.findViewById(R.id.search);
        spinner = (Spinner) view.findViewById(R.id.category_spinner);
//        mDistanceEditText.setText("10");
        mLocationTracker = new LocationTracker(getActivity());
        mLocationTracker.getLocation();
        latitude = mLocationTracker.getLatitude();
        longitude = mLocationTracker.getLongitude();
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGeoDataClient, LAT_LNG_BOUNDS, null);
        mSearchPlace.setAdapter(mAdapter);
        mSearchPlace.setEnabled(false);
        otherLoc = "";
        type = "Default";


        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywordEditText.getText().clear();
                mDistanceEditText.getText().clear();
                mSearchPlace.getText().clear();
                mErrKeyword.setVisibility(View.GONE);
                mErrLoc.setVisibility(View.GONE);
                spinner.setSelection(0);
                radioGroup.check(R.id.radio_here);
                mSearchPlace.setEnabled(false);
                //mSearchPlace.setInputType(InputType.TYPE_NULL);
//                mSearchPlace.setFocusable(false);
//                mKeywordEditText.setFocusable(true);
                mSearchPlace.clearFocus();
            }
        });


        mSearchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                otherLoc = item.getFullText(null).toString();
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mKeywordEditText.getText().toString();
                if (str.matches("^[a-zA-z\\s,]+$")) {
                    mErrKeyword.setVisibility(View.GONE);
                } else {
                    mErrKeyword.setVisibility(View.VISIBLE);
                }


                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_other) {
                    String str2 = otherLoc;
                    Log.d("dddd",otherLoc);
                    if (str2.trim().matches("^[a-zA-z\\s,]+$")) {
                        mErrLoc.setVisibility(View.GONE);
                    } else {
                        mErrLoc.setVisibility(View.VISIBLE);
                    }
                } else {
                    mErrLoc.setVisibility(View.GONE);
                }

                mSearchPlace.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        String str = mSearchPlace.getText().toString();
                        if (str.length() == 0) {
                            mErrLoc.setVisibility(View.VISIBLE);
                        } else {
                            mErrLoc.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mErrLoc.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = mSearchPlace.getText().toString();
                        if (str.length() == 0) {
                            mErrLoc.setVisibility(View.VISIBLE);
                        } else {
                            mErrLoc.setVisibility(View.GONE);
                        }

                    }
                });


                mKeywordEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        String str = mKeywordEditText.getText().toString();
                        if (str.length() == 0) {
                            mErrKeyword.setVisibility(View.VISIBLE);
                        } else {
                            mErrKeyword.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mErrKeyword.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = mKeywordEditText.getText().toString();
                        if (str.length() == 0) {
                            mErrKeyword.setVisibility(View.VISIBLE);
                        } else {
                            mErrKeyword.setVisibility(View.GONE);
                        }

                    }
                });


                Bundle bundle = new Bundle();
                String keyword = mKeywordEditText.getText().toString();
                String distance;
                if (mDistanceEditText.getText() != null && !Objects.equals(mDistanceEditText.getText().toString(), "")) {
                    distance = mDistanceEditText.getText().toString();
                } else {
                    distance = "10";
                }


//                String type = spinner.getSelectedItem().toString();
                int getId = radioGroup.getCheckedRadioButtonId();
                if (getId == R.id.radio_other) {
                    bundle.putString("otherLoc", otherLoc);
                } else {
                    bundle.putString("otherLoc", "here");
                }
                if(latitude != 0 && longitude != 0){
                    bundle.putDouble("lat", latitude);
                    bundle.putDouble("lng", longitude);
                } else{
                    bundle.putDouble("lat", 34.0266);
                    bundle.putDouble("lng", -118.283);
                }
                bundle.putInt("getId", getId);
                bundle.putString("keyword", keyword);
                bundle.putString("distance", distance);
                bundle.putString("type", type);

                if (mErrKeyword.getVisibility() == View.GONE && mErrLoc.getVisibility() == View.GONE) {
                    Intent searchResultsIntent = new Intent(getActivity(), SearchResultesActivity.class);
                    searchResultsIntent.putExtras(bundle);
                    startActivity(searchResultsIntent);
                } else{
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                }


            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(type);
        spinner.setSelection(spinnerPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup.check(R.id.radio_here);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_here:
                        mSearchPlace.setEnabled(false);
                        //mSearchPlace.setInputType(InputType.TYPE_NULL);
                        mErrLoc.setVisibility(View.GONE);
//                        mSearchPlace.setFocusable(false);
                        break;
                    case R.id.radio_other:
                        mSearchPlace.setEnabled(true);
                        //mSearchPlace.setInputType(InputType.TYPE_CLASS_TEXT);
                        //mSearchPlace.setFocusable(true);
                        break;
                }
            }
        });

        return view;
    }

}


//                mKeywordEditText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        String str = mKeywordEditText.getText().toString();
//                        if(str.matches("^[a-zA-z]+$")){
//                            mErrKeyword.setVisibility(View.GONE);
//                        } else{
//                            mErrKeyword.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//                });
