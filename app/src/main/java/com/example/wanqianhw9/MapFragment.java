package com.example.wanqianhw9;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mMapView;
    private Spinner spinner;
    private View mView;
    private String APIKET = "AIzaSyA0TgG8WO6h1YnWcc41_kkS_xFD7tFT1dw";
    private AutoCompleteTextView searchPlace;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(25, -73), new LatLng(49, -125));
    private String origin;
    private String originTitle;
    private String defaultValue;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        origin = "";
        originTitle = "";
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);
        searchPlace = (AutoCompleteTextView) mView.findViewById(R.id.map_search_place);
        mAdapter = new PlaceAutocompleteAdapter(getActivity(),mGeoDataClient,LAT_LNG_BOUNDS,null);
        searchPlace.setAdapter(mAdapter);

        spinner = (Spinner) mView.findViewById(R.id.travel_model_spinner);
        defaultValue = "Driving";
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.travel_mode_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(defaultValue);
        spinner.setSelection(spinnerPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map_view);
        Log.d("map","getmap");
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();// needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap GoogleMap) {
        MapsInitializer.initialize(getContext());
        final GoogleMap googleMap = GoogleMap;
        final double lat = getActivity().getIntent().getExtras().getDouble("placeLat");
        final double lng = getActivity().getIntent().getExtras().getDouble("placeLng");
        String name = getActivity().getIntent().getExtras().getString("PlaceName");
        final String des = getActivity().getIntent().getExtras().getString("PlaceName") + ", " + getActivity().getIntent().getExtras().getString("PlaceName");




        searchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                origin = item.getFullText(null).toString();
                originTitle = item.getPrimaryText(null).toString();

                DateTime now = new DateTime();
                try{
                    final DirectionsResult result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin(origin).destination(des).departureTime(now).await();
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(result.routes[0].legs[0].startLocation.lat,result.routes[0].legs[0].startLocation.lng)).title(originTitle));
                    //googleMap.addMarker(new MarkerOptions().position(new LatLng(result.routes[0].legs[0].endLocation.lat,result.routes[0].legs[0].endLocation.lng)).title(originTitle));
                    addPolyline(result,googleMap);
                } catch (Exception e){
                    e.printStackTrace();
                }



            }
        });

//         Create marker on google map
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lat, lng)).title(name);

//         Add marker to google map
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(15).build();


        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
//        return geoApiContext.setQueryRateLimit(3).setApiKey(APIKET).setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS);
          return geoApiContext.setApiKey(APIKET);
    }


    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }


}
