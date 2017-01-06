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

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    private int status = 0;
    private final int RESIDENT = 1;
    private final int MIGRANT = 2;
    private final int TOURIST = 3;
    private final int STUDENT = 4;

    private String last_city;
    private double last_latitude;
    private double last_longitude;
    private boolean last_location_ready = false;
    private boolean is_GPS_ready = false;
    private double gps_latitude;
    private double gps_longitude;
    private int distance_trigger_km = 30;
    private boolean runOnce = true;

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


                    Intent mainIntent = new Intent(NewCity.this,MapActivity.class);
                    NewCity.this.startActivity(mainIntent);
                    NewCity.this.finish();
                }
            }

        });

        //TODO: Check permissions structure in the backend code for a user using email login
        //TODO: Check permissions structure in the backend code for a user using Facebook login

        //HTTP Request to get the last known location of the user
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
//            client.setBasicAuth(SignIn.static_username, SignIn.static_token);
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }
//        RequestParams params = new RequestParams();
        client.get(MainActivity.base_host_url + "api/getLastLocation/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //TODO: Test and implement statusCode handler for developers and graceful degradation
                Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                try{
                    Log.w("LOCATION SUCCESS: ", response.getString("last_city"));
                    last_city = response.getString("last_city");
                    last_location_ready = true;
                    if(is_GPS_ready == true){
                        int distance = calculateDisplacement();
                        if(distance >= distance_trigger_km){
                            //TODO: This check should really be done in the map activity...
                        }else{
                        }
                    }

                    //TODO: If either the GPS or the getLastKnownLocation fails then we'll get a deadlock... implement timed check to avoid this

                }catch (JSONException e){
                    Log.w("HTTP LOCATION FAIL: ",e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.w("JSON ARRAY... FIX IT!: ", statusCode + ": " + timeline.toString());
                try {
                    JSONObject firstEvent = timeline.getJSONObject(0);
                    String token = firstEvent.getString("token");
                    Log.w("HTTP SUCCESS: ", token.toString());


                } catch (JSONException e) {
                    Log.w("HTTP FAIL: ", e.getMessage().toString());
                }
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


    //Give a little thank you message for users who are kind enough to enable GPS for us :)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_LONG).show();
    }


    //Trigger the check for GPS even before we load the page
    @Override
    public void onStart() {
        super.onStart();
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(GPS_PROVIDER)) {

            //Get the user's permission to use the GPS
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }else{
                getGPSLocation();
            }
        }else{
            //Get the user to activate his GPS
            Toast.makeText(getApplicationContext(),"Please Activate Your GPS to use FriendNGo", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
        }
    }

    //Code to request GPS updates
    private void getGPSLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if(location!=null) {
                gps_latitude = location.getLatitude();
                gps_longitude = location.getLongitude();
                is_GPS_ready = true;
                if (last_location_ready == true) {
                    calculateDisplacement();
                }
            }

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    gps_latitude = location.getLatitude();
                    gps_longitude = location.getLongitude();

                    if(runOnce){
                        Toast.makeText(getApplicationContext(),"GPS Coordinates = " + gps_latitude + "," + gps_longitude,Toast.LENGTH_LONG).show();
                        runOnce=false;

                        //Now Post The Location To The Server
                        //HTTP Request to get the last known location of the user
                        AsyncHttpClient client = new AsyncHttpClient();
                        if(SignIn.static_token != null) {
//                      client.setBasicAuth(SignIn.static_username, SignIn.static_token);
                            client.addHeader("Authorization","Token "+SignIn.static_token);
                        }

                        RequestParams params = new RequestParams();
//                        params.setUseJsonStreamer(true);
                        params.put("last_city", "montreal");
                        client.post(MainActivity.base_host_url + "api/postLocation/",params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                //TODO: Test and implement statusCode handler for developers and graceful degradation
                                Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                                try{
                                    Log.w("LOCATION SUCCESS: ", response.getString("lastCity"));
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

                    }

//                    Log.w("GPS COORDINATES: ", "Lat = " + gps_latitude + " Lon = " + gps_longitude);
                    is_GPS_ready = true;
                    if (last_location_ready == true) {
//                        calculateDisplacement(); //TODO: Here's the function call to calculate, not needed yet
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    //If the user granted permission, then go on to get the location, otherwise remind gim that we need the GPS for his benefit
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("GPS CHECK SUCCESS: ", "AWESOME!");
                    getGPSLocation();
                } else {

                    Toast.makeText(this,"FriendNGo needs your location to serve you exciting local events",Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //Helper function to calculate the distance from the last known location
    //This could possibly be replaced by an address poll with a city name parser
    private int calculateDisplacement() {
        double delta_lat = gps_latitude-last_latitude;
        double square_lat = delta_lat*delta_lat;
        double delta_lon = gps_longitude - last_longitude;
        double square_lon = delta_lon * delta_lon;
        double distance = Math.sqrt(square_lat + square_lon);
        Log.w("DISTANCE CALCULATED", String.valueOf(distance));

        return (int)Math.round(distance);
    }
}
