package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WhatDoYouWantToDoToday extends AppCompatActivity {

    ListView masterListView;
    Button saveButton;
    public static List categoryList = new ArrayList<Category>();
<<<<<<< HEAD
    MasterListAdapter masterListAdapter;
=======
>>>>>>> origin/dev6
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_do_you_want_to_do_today2);
        //Horizontal recycle view
        Log.w("Hello","Hello");
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }
        //GET last known location
        client.get(MainActivity.base_host_url + "api/getCategories/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET CATEGORY SUCCESS", statusCode + ": " + "Response = " + response.toString());
                try{
                    Log.w("GET CATEGORY", statusCode + ", " + response.getString("last_city"));
                }catch (JSONException e){
                    Log.w("GET CATEGORY",e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray categoryJSONArray) {
                Log.w("JSON CATEGORY ARRAY", statusCode + ": " + categoryJSONArray.toString());
<<<<<<< HEAD
                String updateText = String.valueOf(categoryJSONArray.length());
=======

                String updateText = String.valueOf(categoryJSONArray.length());


>>>>>>> origin/dev6
                int activitySum;
                for (int i =0; i < categoryJSONArray.length(); i++){
                    try {
                        JSONObject categoryJSONObject = categoryJSONArray.getJSONObject(i);
                        Category category = new Category();
                        category.setName(categoryJSONObject.getString("name"));
//                        Log.w("JSON PARSE DEBUG", "Category = " + categoryJSONObject.getString("name"));
                        JSONArray activitiesJSONArray = categoryJSONObject.getJSONArray("activity_type");

<<<<<<< HEAD
=======
                        //Take just the first ActivityType in each category for this page

>>>>>>> origin/dev6
//                        for (int j=0; j<1; j++){
                        for (int j=0; j< activitiesJSONArray.length(); j++){ //Alternative to loop through every one
                            String activityType = activitiesJSONArray.getJSONObject(j).getString("name");
//                            Log.w("JSON PARSE DEBUG", "ActivityType = " + activityType + ", " + j);
                            category.addActivityType(activityType);
<<<<<<< HEAD
                        }
=======

                        }

>>>>>>> origin/dev6
                        categoryList.add(category);

                    } catch (JSONException e) {
                        Log.w("GET CATEGORY PARSE FAIL", e.getMessage().toString());
                    }
                }
<<<<<<< HEAD
                masterListAdapter.notifyDataSetChanged();
=======


>>>>>>> origin/dev6
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                Log.w("GET LASTLOC FAILURE2:", "Error Code: " + error_code + ",  " + text);
            }
        });

        //Sets the top bar text
       // getSupportActionBar().setTitle("What do you want to do today?");

        if(MainActivity.cheat_mode==true){
            Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
            WhatDoYouWantToDoToday.this.startActivity(intent);
            WhatDoYouWantToDoToday.this.finish();
        }

        saveButton = (Button)findViewById(R.id.save_categories_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                Intent intent = new Intent(WhatDoYouWantToDoToday.this,WhoAreYou.class);
=======
                Intent intent = new Intent(WhatDoYouWantToDoToday.this,MapActivity.class);
>>>>>>> origin/dev6
                WhatDoYouWantToDoToday.this.startActivity(intent);
                WhatDoYouWantToDoToday.this.finish();
            }
        });
        masterListView = (ListView) findViewById(R.id.category_and_activity_list_view);
<<<<<<< HEAD
        masterListAdapter = new MasterListAdapter(getApplicationContext());
=======
        MasterListAdapter masterListAdapter = new MasterListAdapter(getApplicationContext());
>>>>>>> origin/dev6
        masterListView.setAdapter(masterListAdapter);
    }
}
