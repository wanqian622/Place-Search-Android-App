package com.example.wanqianhw9;



import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Double> geoLoc;
    private TextView mErr;
    private  List<SearchResults> results;
    private int count;
    private List<String> pageTokens;
    private Button mPreviousButton;
    private Button mNextButton;
    private ProgressDialog progressDialog;

    public SearchResultesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_resultes, container, false);
        mErr = (TextView) view.findViewById(R.id.resultsErrorMessage);
        geoLoc = new ArrayList<Double>();
        recyclerView = (RecyclerView) view.findViewById(R.id.result_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        results = new ArrayList<SearchResults>();
        count = 1;
        pageTokens = new ArrayList<String>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.setCancelable(true);
        progressDialog.show();
        mPreviousButton = view.findViewById(R.id.previous);
        mNextButton = view.findViewById(R.id.next);
        mPreviousButton.setEnabled(false);
        mNextButton.setEnabled(false);
        mPreviousButton.setVisibility(View.GONE);
        mNextButton.setVisibility(View.GONE);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                if (count == 2) {
                    if (results.size() > 20 && results.size() <= 40) {
                        mErr.setVisibility(View.GONE);
                        setUpResults(results.subList(20, results.size()));
                    } else if (results.size() > 40) {
                        mErr.setVisibility(View.GONE);
                        setUpResults(results.subList(20, 40));
                    } else {
                        progressDialog.setMessage("Fetching next page");
                        progressDialog.show();
                        askForNextPage();
                    }
                } else if (count == 3) {
                    if (results.size() > 40) {
                        mErr.setVisibility(View.GONE);
                        setUpResults(results.subList(40, results.size()));
                    } else {
                        progressDialog.setMessage("Fetching next page");
                        progressDialog.show();
                        askForNextPage();
                    }
                }
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count -= 1;
                if (count == 1) {
                    setUpResults(results.subList(0, 20));
                } else if (count == 2) {
                    setUpResults(results.subList(20, 40));
                }
            }
        });

        String getLocation = getActivity().getIntent().getExtras().getString("otherLoc");
        if(getLocation.equals("here")){
            requestForHere();
        } else{
            requestForOther(getLocation);
        }


        return view;
    }



    // get here lat and lng
    private void requestForHere(){
        double lat = getActivity().getIntent().getExtras().getDouble("lat");
        double lng = getActivity().getIntent().getExtras().getDouble("lng");
        geoLoc.add(lat);
        geoLoc.add(lng);
        requestForResults();

    }


    // get other lat and lng
    private void requestForOther(String s){
        String str = s.replaceAll(", "," ");
        String query = GetURLS.OTHERGEO + Uri.encode(str);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                Log.d("gwRes",response);
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getInt("error") == 0){
                        geoLoc.add(jObj.getDouble("lat"));
                        geoLoc.add(jObj.getDouble("lng"));
                        requestForResults();

                    } else{
                        mErr.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    mErr.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mErr.setVisibility(View.VISIBLE);
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "Other");
    }

    // get results
    private void requestForResults(){
        String keyword = getActivity().getIntent().getExtras().getString("keyword");
        String distance =getActivity().getIntent().getExtras().getString("distance");
        String type = getActivity().getIntent().getExtras().getString("type");
        Double radius =(Double) 1609.344 * Integer.parseInt(distance);


        String newType = Uri.encode(type);
        String newKeyword = Uri.encode(keyword);
        String queryUrl = GetURLS.RES +"lat="+ geoLoc.get(0) + "&lng=" + geoLoc.get(1) + "&radius=" + radius
                + "&type=" + newType + "&keyword=" + newKeyword;
        Log.d("response four", queryUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getInt("error") == 0){
                        JSONArray jsonArray =jObj.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject item = jsonArray.getJSONObject(i);
                            String placeUrl = item.getString("category_icon");
                            String placeName = item.getString("place_name");
                            String placeAddress = item.getString("place_address");
                            String placeId = item.getString("place_id");
                            Double placeLat = item.getDouble("place_lat");
                            Double placeLng = item.getDouble("place_lng");
                            SearchResults res = new SearchResults(placeUrl,placeName,placeAddress,placeId,placeLat,placeLng);
                            results.add(res);
                        }
                        setUpResults(results);
                        progressDialog.dismiss();
                        mPreviousButton.setVisibility(View.VISIBLE);
                        mNextButton.setVisibility(View.VISIBLE);
                        if(jObj.has("nextPageToken")){
                            pageTokens.add(jObj.getString("nextPageToken"));
                            mNextButton.setEnabled(true);
                            mNextButton.setTextColor(Color.BLACK);
                        } else{
                            mNextButton.setEnabled(false);
                            mNextButton.setTextColor(Color.GRAY);
                        }
                    } else{
                        setUpResults(new ArrayList<SearchResults>());
                        mErr.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    setUpResults(new ArrayList<SearchResults>());
                    mErr.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setUpResults(new ArrayList<SearchResults>());
                mErr.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "results1");
    }

    private void askForNextPage(){
        String queryUrl = GetURLS.RES + "pageToken=";
        mNextButton.setEnabled(false);
        if(count == 2){
            queryUrl += pageTokens.get(0);
        } else{
            queryUrl += pageTokens.get(1);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getInt("error") == 0){
                        JSONArray jsonArray =jObj.getJSONArray("data");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject item = jsonArray.getJSONObject(i);
                                String placeUrl = item.getString("category_icon");
                                String placeName = item.getString("place_name");
                                String placeAddress = item.getString("place_address");
                                String placeId = item.getString("place_id");
                                Double placeLat = item.getDouble("place_lat");
                                Double placeLng = item.getDouble("place_lng");
                                SearchResults res = new SearchResults(placeUrl,placeName,placeAddress,placeId,placeLat,placeLng);
                                results.add(res);
                            }
                            if(count == 2){
                                setUpResults(results.subList(20,results.size()));
                            } else{
                                setUpResults(results.subList(40,results.size()));
                            }
                            progressDialog.dismiss();


                        if(jObj.has("nextPageToken")){
                                pageTokens.add(jObj.getString("nextPageToken"));
                                mNextButton.setEnabled(true);
                                mNextButton.setTextColor(Color.BLACK);
                            } else{
                                mNextButton.setEnabled(false);
                                mNextButton.setTextColor(Color.GRAY);
                            }

                    } else{
                        setUpResults(new ArrayList<SearchResults>());
                        mErr.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    setUpResults(new ArrayList<SearchResults>());
                    mErr.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setUpResults(new ArrayList<SearchResults>());
                mErr.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        }) {

        };
        String tag = "results" + count;
        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void setUpResults(List<SearchResults> result){
        if(count == 1){
            mPreviousButton.setEnabled(false);
            mPreviousButton.setTextColor(Color.GRAY);
            if(pageTokens.size() > 0){
                mNextButton.setEnabled(true);
                mNextButton.setTextColor(Color.BLACK);
            } else{
                mNextButton.setEnabled(false);
                mNextButton.setTextColor(Color.GRAY);
            }
        } else if(count == 2){
            mPreviousButton.setEnabled(true);
            mPreviousButton.setTextColor(Color.BLACK);
            if(pageTokens.size() > 1){
                mNextButton.setEnabled(true);
                mNextButton.setTextColor(Color.BLACK);
            } else{
                mNextButton.setEnabled(false);
                mNextButton.setTextColor(Color.GRAY);
            }
        } else{
            mPreviousButton.setEnabled(true);
            mPreviousButton.setTextColor(Color.BLACK);
            mNextButton.setEnabled(false);
            mNextButton.setTextColor(Color.GRAY);
        }

        mAdapter = new SearchResultsListAdapter(result,getContext());
        recyclerView.setAdapter(mAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        if(count == 1){
            if(results.size() < 20){
                setUpResults(results.subList(0,results.size()));
            } else{
                setUpResults(results.subList(0,20));
            }
        } else if(count == 2){
            if(results.size() < 40){
                setUpResults(results.subList(20,results.size()));
            } else{
                setUpResults(results.subList(20,40));
            }
        } else{
            setUpResults(results.subList(40,results.size()));
        }
    }







}
