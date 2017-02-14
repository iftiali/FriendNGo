package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WhatDoYouWantToDoToday extends AppCompatActivity {

    ListView masterListView;
    Button saveButton;
    private ImageView sports_category;
    private ImageView sports_category_check;
    private Button jogging_button;
    private ImageView jogging_button_check;
    private Button tennis_button;
    private ImageView tennis_button_check;
    private Button hockey_button;
    private ImageView hockey_button_check;

    private ImageView nightlife_category;
    private ImageView nightlife_category_check;
    private Button dancing_button;
    private ImageView dancing_button_check;
    private Button jazz_button;
    private ImageView jazz_button_check;
    private Button pubs_button;
    private ImageView pubs_button_check;

    private ImageView arts_category;
    private ImageView arts_category_check;
    private Button museum_button;
    private ImageView museum_button_check;
    private Button gallery_button;
    private ImageView gallery_button_check;
    private Button city_tour_button;
    private ImageView city_tour_button_check;
    private Button save_button;
    String current_city;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_do_you_want_to_do_today2);


        //Sets the top bar text
        getSupportActionBar().setTitle("What do you want to do today?");
        current_city = getIntent().getExtras().getString("currentCity");
        if(MainActivity.cheat_mode==true){
            Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
            intent.putExtra("currentCity", current_city);
            WhatDoYouWantToDoToday.this.startActivity(intent);
            WhatDoYouWantToDoToday.this.finish();
        }

        saveButton = (Button)findViewById(R.id.save_categories_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
                intent.putExtra("currentCity", current_city);
                WhatDoYouWantToDoToday.this.startActivity(intent);
                WhatDoYouWantToDoToday.this.finish();
            }
        });

        masterListView = (ListView) findViewById(R.id.category_and_activity_list_view);

        MasterListAdapter masterListAdapter = new MasterListAdapter(getApplicationContext());
        masterListView.setAdapter(masterListAdapter);

    }
}
