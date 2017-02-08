package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Request extends AppCompatActivity {

    ListView list;
    ActivityRequestListAdapter adapter;

    public static ArrayList requestList = new ArrayList();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list=(ListView)findViewById(R.id.request_list);

        // Getting adapter by passing xml data ArrayList
        requestList.add("Parth");

        adapter=new ActivityRequestListAdapter(this);
        list.setAdapter(adapter);



    }

}
