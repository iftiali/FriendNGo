package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Popular extends AppCompatActivity {

    private Button imageButton;

    private GridView grid;
    private GridAdapter gridAdapter;
    private TextView participantsNumber;

    public static List categoryList = new ArrayList<Category>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Creates fullscreen effect for older phones -> before setContentView()
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_popular);

        //Creates fullscreen effect for newer phones
        if (Build.VERSION.SDK_INT >= 16) {

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
//            getSupportActionBar().hide();
        }

        participantsNumber = (TextView)findViewById(R.id.activity_number);
        participantsNumber.setText("9");
        gridAdapter = new GridAdapter(getApplicationContext());
        grid = (GridView) findViewById(R.id.activity_grid_view);
        grid.setAdapter(gridAdapter);

        //SETUP GET Categories
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }
        //GET categories
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

                String updateText = String.valueOf(categoryJSONArray.length());
                participantsNumber.setText(updateText);

                int activitySum;
                for (int i =0; i < categoryJSONArray.length(); i++){
                    try {
                        JSONObject categoryJSONObject = categoryJSONArray.getJSONObject(i);
                        Category category = new Category();
                        category.setName(categoryJSONObject.getString("name"));
//                        Log.w("JSON PARSE DEBUG", "Category = " + categoryJSONObject.getString("name"));
                        JSONArray activitiesJSONArray = categoryJSONObject.getJSONArray("activity_type");

                        //Take just the first ActivityType in each category for this page

//                        for (int j=0; j<1; j++){
                      for (int j=0; j< activitiesJSONArray.length(); j++){ //Alternative to loop through every one
                            String activityType = activitiesJSONArray.getJSONObject(j).getString("name");
//                            Log.w("JSON PARSE DEBUG", "ActivityType = " + activityType + ", " + j);
                            category.addActivityType(activityType);
                        }
                        categoryList.add(category);
                    } catch (JSONException e) {
                        Log.w("GET CATEGORY PARSE FAIL", e.getMessage().toString());
                    }
                }
                gridAdapter.notifyDataSetChanged();

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



        imageButton = (Button) findViewById(R.id.got_it_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Popular.this,MapActivity.class);
                Popular.this.startActivity(intent);
//                Popular.this.finish();
            }
            });

        Intent intent = new Intent(Popular.this,MapActivity.class);
        Popular.this.startActivity(intent);

    }
}
