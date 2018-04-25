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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mMapView;
    private Spinner spinner;
    private View mView;
    private String APIKEY = "AIzaSyA0TgG8WO6h1YnWcc41_kkS_xFD7tFT1dw";
    private AutoCompleteTextView searchPlace;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(24.7433195, -124.7844079), new LatLng(49.3457868, -66.9513812));
    private String origin;
    private String originTitle;
    private String defaultValue;
    private String originPlaceId;
    private String desPlaceId;
    private GoogleMap googleMap;
    private double startLat;
    private double startLng;
    private double lat;
    private double lng;
    private boolean firstSetMarker;
    private String name;
    private LatLngBounds mapBound;



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
        originPlaceId = "";
        desPlaceId = "";
        startLat = 0;
        startLng = 0;
        mapBound = LAT_LNG_BOUNDS;
        lat = getActivity().getIntent().getExtras().getDouble("placeLat");
        lng = getActivity().getIntent().getExtras().getDouble("placeLng");
        name = getActivity().getIntent().getExtras().getString("PlaceName");
        firstSetMarker = true;
        spinner = (Spinner) mView.findViewById(R.id.travel_model_spinner);
        defaultValue = "Driving";
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.travel_mode_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(defaultValue);
        spinner.setSelection(spinnerPosition);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map_view);
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
        googleMap = GoogleMap;

        //         Create marker on google map
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lat, lng)).title(name);

//         Add marker to google map
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(15).build();


        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        searchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                origin = item.getFullText(null).toString();
                originTitle = item.getPrimaryText(null).toString();
                originPlaceId = item.getPlaceId();
                desPlaceId = getActivity().getIntent().getExtras().getString("PlaceID");
                googleMap.clear();
                firstSetMarker = false;
                requestForMapDirections();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                defaultValue = parent.getItemAtPosition(position).toString();
                googleMap.clear();
                requestForMapDirections();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void requestForMapDirections(){
        String mapUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=place_id:" + originPlaceId + "&destination=place_id:" + desPlaceId + "&mode=" + defaultValue.toLowerCase() +"&key=" + APIKEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mapUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getString("status").equals("OK") && jObj.getJSONArray("routes") != null && jObj.getJSONArray("routes").length() > 0){
                        JSONArray arr = jObj.getJSONArray("routes");
                        JSONObject obj = arr.getJSONObject(0);
                        JSONObject bound = obj.getJSONObject("bounds");
                        JSONObject northeast = bound.getJSONObject("northeast");
                        JSONObject southwest = bound.getJSONObject("southwest");
                        mapBound = new LatLngBounds(new LatLng(southwest.getDouble("lat"), southwest.getDouble("lng")), new LatLng(northeast.getDouble("lat"), northeast.getDouble("lng")));

                        JSONObject path = obj.getJSONObject("overview_polyline");
                        JSONArray legs = obj.getJSONArray("legs");
                        Log.d("getMap",legs.getJSONObject(0).toString());
                        JSONObject endLoc = legs.getJSONObject(0).getJSONObject("end_location");
                        JSONObject startLoc = legs.getJSONObject(0).getJSONObject("start_location");
                        startLat = startLoc.getDouble("lat");
                        startLng = startLoc.getDouble("lng");
                        setMarker(path);
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
        AppController.getInstance().addToRequestQueue(stringRequest, "mapDirections");
    }

    private void setMarker(JSONObject path){
        if(firstSetMarker){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(startLat,startLng)).title(originTitle));
            firstSetMarker = false;
        } else{
            googleMap.addMarker(new MarkerOptions().position(new LatLng(startLat,startLng)).title(originTitle));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(name));
        }
        try{
            List<LatLng> decodedPath = PolyUtil.decode(path.getString("points"));
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(mapBound, padding);
            googleMap.animateCamera(cu);

            googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
        } catch(Exception e){
            e.printStackTrace();
        }
    }



}
