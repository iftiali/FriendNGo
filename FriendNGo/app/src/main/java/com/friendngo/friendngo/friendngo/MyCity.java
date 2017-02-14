package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyCity extends AppCompatActivity {
    String current_city;
    TextView my_city_country_name;
    Button my_city_next;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        current_city = getIntent().getExtras().getString("currentCity");
        my_city_country_name = (TextView)findViewById(R.id.my_city_country_name);
        my_city_country_name.setText(current_city);
        Log.w("New_City",current_city);
        my_city_next = (Button)findViewById(R.id.my_city_next);
        my_city_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCity.this.finish();
            }
        });
    }

}
