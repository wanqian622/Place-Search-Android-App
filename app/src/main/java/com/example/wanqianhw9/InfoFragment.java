package com.example.wanqianhw9;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private String placeId;
    private View mView;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_info, container, false);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
        placeId = getActivity().getIntent().getExtras().getString("PlaceID");


//        mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
//                if (task.isSuccessful()) {
//                    PlaceBufferResponse places = task.getResult();
//                    Place myPlace = places.get(0);
//
//                    places.release();
//                } else {
//
//                }
//            }
//        });


        return mView;
    }

}
