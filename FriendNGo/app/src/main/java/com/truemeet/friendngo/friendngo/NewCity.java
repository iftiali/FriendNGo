package com.truemeet.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    private double last_latitude;
    private double last_longitude;
    private boolean last_location_ready = false;
    private boolean is_GPS_ready = false;
    private double gps_latitude;
    private double gps_longitude;
    private int distance_trigger_km = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

        //Sets the top bar text
        getSupportActionBar().setTitle("New City");

        if(SignIn.static_token != null) {
            String token = SignIn.static_token;
            Log.w("TOKEN TEST", token);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(SignIn.static_username,SignIn.static_token);

        RequestParams params = new RequestParams();

        client.post(MainActivity.base_host_url + "api/getLastLocation/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                //TODO: Test and implement statusCode handler for developers and graceful degradation
                Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                try{
                    Log.w("HTTP SUCCESS: ", response.get("lastLocation").toString());
                    last_latitude = response.getDouble("lastLatitude");
                    last_longitude = response.getDouble("lastLongitude");
                    last_location_ready = true;
                    if(is_GPS_ready == true){
                        calculateDisplacement();
                    }

                }catch (JSONException e){
                    Log.w("HTTP FAIL: ",e.getMessage().toString());
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
        });
    }

    private void calculateDisplacement() {

        double delta_lat = gps_latitude-last_latitude;
        double square_lat = delta_lat*delta_lat;
        double delta_lon = gps_longitude - last_longitude;
        double square_lon = delta_lon * delta_lon;
        double distance = Math.sqrt(square_lat + square_lon);
        Log.w("DISTANCE CALCULATED", String.valueOf(distance));
        if(distance < distance_trigger_km){
            // User is in the same city
            //TODO: Call the map activity
        }else{
            //User is not in the same city, allow the user to change his city preferences
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_LONG).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }else{
                getGPSLocation();
            }

        }else{
            //Do what you need if not enabled...
            Toast.makeText(getApplicationContext(),"Please Activate Your GPS to use FriendNGo", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
        }

    }

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
                    is_GPS_ready = true;
                    if (last_location_ready == true) {
                        calculateDisplacement();
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
}
