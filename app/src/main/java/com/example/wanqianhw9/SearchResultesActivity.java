package com.example.wanqianhw9;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SearchResultesActivity extends AppCompatActivity {

    private Fragment mResFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resultes);
        toolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mResFragment = new SearchResultesFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.relativelayout_searchRes, mResFragment).commit();


    }
}
