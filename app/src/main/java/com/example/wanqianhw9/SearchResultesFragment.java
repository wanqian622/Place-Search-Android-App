package com.example.wanqianhw9;



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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




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
    private String pageTokens;
    private Button mPreviousButton;
    private Button mNextButton;





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
        pageTokens = "";
        mPreviousButton = view.findViewById(R.id.previous);
        mNextButton = view.findViewById(R.id.next);
        mPreviousButton.setEnabled(false);
        mNextButton.setEnabled(false);

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GetURLS.CURGEO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.getString("status").equals("success")){
                        geoLoc.add(jObj.getDouble("lat"));
                        geoLoc.add(jObj.getDouble("lon"));
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
        AppController.getInstance().addToRequestQueue(stringRequest, "Here");
    }


    // get other lat and lng
    private void requestForOther(String s){
        String str = s.replaceAll(", "," ");
        String query = GetURLS.OTHERGEO + Uri.encode(str);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.getInt("error") == 0){
                        geoLoc.add(data.getDouble("lat"));
                        geoLoc.add(data.getDouble("lng"));
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
                        setUpResults(results.subList(0,20));
                        mPreviousButton.setEnabled(false);
                        if(jObj.has("nextPageToken")){
                            pageTokens = jObj.getString("nextPageToken");
                            count = count + 1;  // 2
                            mNextButton.setEnabled(true);
                            mNextButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    askForNextPage(pageTokens);
                                }
                            });

                        } else{
                            mNextButton.setEnabled(false);
                        }
                    } else{
                        setUpResults(new ArrayList<SearchResults>());
                        mErr.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    setUpResults(new ArrayList<SearchResults>());
                    mErr.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setUpResults(new ArrayList<SearchResults>());
                mErr.setVisibility(View.VISIBLE);
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "results1");
    }

    private void askForNextPage(final String pageToken){
        String queryUrl = GetURLS.RES + "pageToken=" + pageToken;
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
                                setUpResults(results.subList(20,40));
                            } else{
                                setUpResults(results.subList(40,60));
                            }
                            mPreviousButton.setEnabled(true);
                            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    count = count - 1;  // 1  2
                                    if(count == 1){
                                        setUpResults(results.subList(0,20));
                                    } else{
                                        setUpResults(results.subList(20,40));
                                    }

                                }
                            });
                            if(jObj.has("nextPageToken") &&  count < 3){
                                count = count + 1;  // 3
                                pageTokens = jObj.getString("nextPageToken");
                                mNextButton.setEnabled(true);
                                mNextButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        askForNextPage(pageTokens);
                                    }
                                });
                            } else{
                                mNextButton.setEnabled(false);
                            }

                    } else{
                        setUpResults(new ArrayList<SearchResults>());
                        mErr.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    setUpResults(new ArrayList<SearchResults>());
                    mErr.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setUpResults(new ArrayList<SearchResults>());
                mErr.setVisibility(View.VISIBLE);
            }
        }) {

        };
        String tag = "results" + count;
        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void setUpResults(List<SearchResults> result){

        mAdapter = new SearchResultsListAdapter(result,getContext());

        recyclerView.setAdapter(mAdapter);
    }


}
