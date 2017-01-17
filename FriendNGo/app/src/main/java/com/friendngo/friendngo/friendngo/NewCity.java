package com.friendngo.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.location.LocationManager.GPS_PROVIDER;

public class NewCity extends AppCompatActivity {

    private Button residentButton;
    private Button migrantButton;
    private Button touristButton;
    private Button studentButton;
    private Button nextButton;

    private int status = 0;
    private final int RESIDENT = 1;
    private final int MIGRANT = 2;
    private final int TOURIST = 3;
    private final int STUDENT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

        //Sets the top bar text
        getSupportActionBar().setTitle("New City");

        //Get the button instances
        residentButton = (Button) findViewById(R.id.resident_button);
        migrantButton = (Button) findViewById(R.id.migrant_button);
        touristButton = (Button) findViewById(R.id.tourist_button);
        studentButton = (Button) findViewById(R.id.student_button);
        nextButton = (Button) findViewById(R.id.next_button);

        //Set what happens when you click the buttons
        residentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = RESIDENT;
                disableOtherButtons();
                residentButton.setActivated(true);
                residentButton.setTextColor(Color.WHITE);
                residentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        migrantButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = MIGRANT;
                disableOtherButtons();
                migrantButton.setActivated(true);
                migrantButton.setTextColor(Color.WHITE);
                migrantButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        touristButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = TOURIST;
                disableOtherButtons();
                touristButton.setActivated(true);
                touristButton.setTextColor(Color.WHITE);
                touristButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        studentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = STUDENT;
                disableOtherButtons();
                studentButton.setActivated(true);
                studentButton.setTextColor(Color.WHITE);
                studentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });


        //The next button triggers the map activity
        nextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (status == 0){
                    Toast.makeText(getApplicationContext(),"Please Choose A Status",Toast.LENGTH_LONG).show();
                }else{
                    AsyncHttpClient client = new AsyncHttpClient();
                    if(SignIn.static_token != null) {
//                      client.setBasicAuth(SignIn.static_username, SignIn.static_token);
                        client.addHeader("Authorization","Token "+SignIn.static_token);
                    }

                    //Set the status type into the message for the server
                    RequestParams params = new RequestParams();
//                    params.setUseJsonStreamer(true);
                    switch(status) {
                        case (1):
                            params.put("status", "resident");
                            break;
                        case (2):
                            params.put("status", "migrant");
                            break;
                        case (3):
                            params.put("status", "tourist");
                            break;
                        case (4):
                            params.put("status", "student");
                            break;
                        default:
                            Log.w("SWITCH ERROR: ", "" + status);
                    }


                    client.post(MainActivity.base_host_url + "api/postStatus/",params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            //TODO: Test and implement statusCode handler for developers and graceful degradation
                            Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                            try{
                                Log.w("STATUS SUCCESS: ", response.getString("status"));
                            }catch (JSONException e){
                                Log.w("HTTP LOCATION FAIL: ",e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("JSON ARRAY???: ", statusCode + ": " + timeline.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                            Log.w("HTTP FAILURE:", "Error Code: " + error_code);
                        }
                    });
                    NewCity.this.finish();
                }
            }

        });
    }


    //Helper function called when a button is pressed
    private void disableOtherButtons() {

        migrantButton.setActivated(false);
        migrantButton.setBackgroundResource(R.drawable.white_button);
        migrantButton.setTextColor(Color.BLACK);

        residentButton.setActivated(false);
        residentButton.setBackgroundResource(R.drawable.white_button);
        residentButton.setTextColor(Color.BLACK);

        touristButton.setActivated(false);
        touristButton.setBackgroundResource(R.drawable.white_button);
        touristButton.setTextColor(Color.BLACK);

        studentButton.setActivated(false);
        studentButton.setBackgroundResource(R.drawable.white_button);
        studentButton.setTextColor(Color.BLACK);
    }
}