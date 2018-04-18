package com.example.wanqianhw9;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchResultesActivity extends AppCompatActivity {

    private Fragment mResFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resultes);

        mResFragment = new SearchResultesFragment();

        getSupportFragmentManager().beginTransaction().
                add(R.id.relativelayout_searchRes, mResFragment).commit();


    }
}
