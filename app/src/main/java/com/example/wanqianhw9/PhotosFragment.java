package com.example.wanqianhw9;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {
    private View mView;
    private String placeId;
    private List<PlacePhotoMetadata> getPhotosList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mErr;



    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_photos, container, false);
        getPhotosList = DetailsActivity.photosList;
        mErr = (TextView) mView.findViewById(R.id.photosErrorMessage);
        recyclerView = (RecyclerView) mView.findViewById(R.id.photos_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        setUpPhotos();
        if(getPhotosList == null || getPhotosList.size() < 1){
            mErr.setVisibility(View.VISIBLE);
        }
        return mView;
    }


    private void setUpPhotos(){
        mAdapter = new PhotosListAdapter(getPhotosList,getContext());
        recyclerView.setAdapter(mAdapter);
    }

}
