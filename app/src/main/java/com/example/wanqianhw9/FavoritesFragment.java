package com.example.wanqianhw9;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements FavoriteListAdapter.RecyclerViewClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mErr;
    private View mView;




    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.favorite_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mErr = (TextView) mView.findViewById(R.id.favoriteErrorMessage);
        getFavoritesItem();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavoritesItem();
    }

    private void getFavoritesItem(){
        Map<String,?> favList = SharedPreferenceManager.getInstance(getContext().getApplicationContext()).getAll();
        List<SearchResults> favorites = new ArrayList<SearchResults>();
        if(favList.isEmpty() || favList.size() < 1){
            mErr.setVisibility(View.VISIBLE);
        } else{
            mErr.setVisibility(View.GONE);
            for(Map.Entry<String,?> entry : favList.entrySet()){
                if(entry.getValue() instanceof String){
                    String value = (String) entry.getValue();
                    Gson gson = new Gson();
                    SearchResults res = gson.fromJson(value,SearchResults.class);
                    favorites.add(res);
                }
            }
        }
        setUpFavorites(favorites);
    }

    private void setUpFavorites(List<SearchResults> fav){
        mAdapter = new FavoriteListAdapter(fav,getContext(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void recyclerViewListClicked(){
        getFavoritesItem();
    }


}
