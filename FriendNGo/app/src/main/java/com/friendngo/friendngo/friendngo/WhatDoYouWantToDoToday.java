package com.friendngo.friendngo.friendngo;

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

public class WhatDoYouWantToDoToday extends AppCompatActivity {

    ListView masterListView;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_do_you_want_to_do_today2);


        //Sets the top bar text
        getSupportActionBar().setTitle("What do you want to do today?");

        if(MainActivity.cheat_mode==true){
            Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
            WhatDoYouWantToDoToday.this.startActivity(intent);
            WhatDoYouWantToDoToday.this.finish();
        }

        saveButton = (Button)findViewById(R.id.save_categories_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
                WhatDoYouWantToDoToday.this.startActivity(intent);
                WhatDoYouWantToDoToday.this.finish();
            }
        });

        masterListView = (ListView) findViewById(R.id.category_and_activity_list_view);

        MasterListAdapter masterListAdapter = new MasterListAdapter(getApplicationContext());
        masterListView.setAdapter(masterListAdapter);

    }
}
