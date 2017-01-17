package com.friendngo.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
//    private String apiKey = "AIzaSyBsrd4IbitFz96ey3GTh-p0-9GyrybN1Ac";
    private String last_city;
    private String current_city;

    private boolean current_location_ready = false;
    private double current_gps_latitude;
    private double current_gps_longitude;

    private boolean last_location_ready = false;

    private boolean runOnce = true;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    public static List activitiesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendNGo");

//        listView = (ListView)findViewById(R.id.list)

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //HTTP GET Request to get the last known location of the user
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }

        //GET Last Location callbacks
        client.get(MainActivity.base_host_url + "api/getLastLocation/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("HTTP SUCCESS1: ", statusCode + ": " + "Response = " + response.toString());

                try{
                    Log.w("GET LOCATION: ", statusCode + ", " + response.getString("last_city"));
                    last_city = response.getString("last_city");
                    last_location_ready = true;

                    if(current_location_ready == true){
                        update_city();
                    }
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
                    Log.w("HTTP SUCCESS2: ", token.toString());


                } catch (JSONException e) {
                    Log.w("HTTP FAIL1: ", e.getMessage().toString());
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                Log.w("HTTP FAILURE2:", "Error Code: " + error_code);
            }
        });


        //REMOVE THIS BUTTON ONCE WE HAVE A FACEBOOK LOGOUT BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logOut();

                Snackbar.make(view, "Logged Out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Creates the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng montreal = new LatLng(45.467206,-73.612096);
        mMap.addMarker(new MarkerOptions().position(montreal).title("Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(montreal));
    }



////////////////////////////////// GPS ACQUIRE SIGNAL CODE//////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    //If the user granted permission, then go on to get the location, otherwise remind gim that we need the GPS for his benefit
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("GPS CHECK SUCCESS4: ", "AWESOME!");
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

    //Code to request GPS updates
    private void getGPSLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                current_gps_latitude = location.getLatitude();
                current_gps_longitude = location.getLongitude();
//                current_location_ready = true;
            }

            LocationListener locationListener = new LocationListener() {


////////////////////////////////// USING GPS CODE //////////////////////////////////////////////////
                //Here is where we receive the location update
                public void onLocationChanged(Location location) {
                    current_gps_latitude = location.getLatitude();
                    current_gps_longitude = location.getLongitude();

                    if (runOnce) {
                        Toast.makeText(getApplicationContext(), "GPS Coordinates = " + current_gps_latitude + "," + current_gps_longitude, Toast.LENGTH_LONG).show();
                        runOnce = false;
                        current_location_ready = true;

                        if (last_location_ready == true) {
                            update_city();
                        }

                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    //Helper function to calculate the distance from the last known location
    //This could possibly be replaced by an address poll with a city name parser
    private void update_city() {
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(current_gps_latitude, current_gps_longitude, 1);
            if (addresses.size() > 0) {
                current_city = addresses.get(0).getLocality();

                Log.w("GPS CITY RESULT", current_city);
                if(last_city.equalsIgnoreCase(current_city) != true){

                        //POST Location
                        AsyncHttpClient client = new AsyncHttpClient();
                        if (SignIn.static_token != null) {
                            client.addHeader("Authorization", "Token " + SignIn.static_token);
                        }

                        RequestParams params = new RequestParams();
                        params.put("last_city", "montreal");
                        client.post(MainActivity.base_host_url + "api/postLocation/", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                Log.w("HTTP SUCCESS5", statusCode + ": " + "Response = " + response.toString());
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
                            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                Log.w("HTTP FAILURE4:", "Error Code: " + error_code);
                            }
                        });

                        Log.w("GPS CITY RESULT", "New City Detected");
                        Intent intent = new Intent(MapActivity.this,NewCity.class);
                        MapActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),current_city, Toast.LENGTH_LONG).show();
                    Log.w("GPS CITY RESULT", "Not in a new city");
                }

                //GET the activities list
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

                RequestParams params = new RequestParams();
                params.put("last_city", "montreal");
                client.get(MainActivity.base_host_url + "api/getActivities/", new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Log.w("HTTP SUCCESS6: ", statusCode + ": " + "Response = " + response.toString());
                        try {
                            Log.w("ACTIVITIES SUCCESS7: ", response.getString("lastCity"));
                        } catch (JSONException e) {
                            Log.w("HTTP LOCATION FAIL: ", e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                        Log.w("HTTP SUCCESS10", statusCode + "- JSON ARRAY: " + responseArray.toString());

                        //Cycle through the list of activities
                        for (int i=0; i<responseArray.length(); i++){
                            try {
                                JSONObject activity = responseArray.getJSONObject(i);
                                String name = activity.getString("activity_name");
                                int creator = activity.getInt("creator");
                                int maxUsers = activity.getInt("max_users");
                                String activityTimeString = activity.getString("activity_time");
                                SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                                Date activityTime = new Date();
                                try {
                                    activityTime = activityTimeFormat.parse(activityTimeString);
                                }catch (ParseException p){
                                    Log.w("PARSE EXCEPTION","Something went wrong with DATE parsing");
                                }
                                String type = activity.getString("activity_type");
                                double latitude = activity.getDouble("activity_lat");
                                double longitude = activity.getDouble("activity_lon");

                                UserActivity userActivity = new UserActivity(name,
                                        creator,
                                        maxUsers,
                                        activityTime,
                                        type,
                                        latitude,
                                        longitude );

                                activitiesList.add(userActivity);
                            } catch (JSONException e){
                                Log.w("JSON EXCEPTION:", "Error parsing the getActivities response");
                            }

                        }

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        Log.w("HTTP RETRY", "TRYING AGAIN");
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Log.w("HTTP FAILURE3", "Error Code: " + error_code);
                    }
                });

            } else {
                Log.w("GPS LOCATION FAIL", "FAIL");
            }
        } catch (IOException e){
            Log.w("GPS CITY RESULT: ", "FAIL");
        }
    }
}