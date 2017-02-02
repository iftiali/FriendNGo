package com.friendngo.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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

    public ListView listView;
    public static List activitiesList = new ArrayList<UserActivity>();
    private static ActivityListAdapter adapter;
    Marker currLocationMarker;
    LatLng currentPosition;

    Map markerMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map); //NOTE: Drawer View is setup later

        //Set top bar and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendNGo");

        //Setup the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //GET last known location setup
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }
        //GET last known location
        client.get(MainActivity.base_host_url + "api/getLastLocation/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET LASTLOC SUCCESS", statusCode + ": " + "Response = " + response.toString());

                try{
                    Log.w("GET LOCATION: ", statusCode + ", " + response.getString("last_city"));
                    last_city = response.getString("last_city");
                    last_location_ready = true;

                    if(current_location_ready == true){
                        update_city();
                    }
                }catch (JSONException e){
                    Log.w("GET LASTLOC FAIL: ",e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.w("JSON ARRAY... FIX IT!: ", statusCode + ": " + timeline.toString());
                try {
                    JSONObject firstEvent = timeline.getJSONObject(0);
                    String token = firstEvent.getString("token");
                    Log.w("GET LASTLOC SUCCESS2", token.toString());


                } catch (JSONException e) {
                    Log.w("GET LASTLOC FAIL1: ", e.getMessage().toString());
                }
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

        //REMOVE THIS BUTTON ONCE WE HAVE A FACEBOOK LOGOUT BUTTON
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                LoginManager.getInstance().logOut();
//
//                Snackbar.make(view, "Logged Out", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Adds the action bar for the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set the custom adapter
        activitiesList.add(new UserActivity("Get breakfast","t2@t2.com",2,new Date(2017,01,10), "Drinks", "Eating",1.99,1.99));
        activitiesList.add(new UserActivity("Fun stuff","t2@t2.com",2,new Date(2017,01,10), "Business", "Eating",1.99,1.99));
        adapter = new ActivityListAdapter(getApplicationContext());
        listView = (ListView)findViewById(R.id.activity_list);

        if (listView == null) {
            Log.w("LIST VIEW ERROR", "List view is null!");
        } else {
            Log.w("LIST SUCCESS", "The List view it's alive!!!");
            listView.setAdapter(adapter);

            //Here is where we schedule the polling of our activities
            ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                        //This happens in a seperate thread
                        public void run() {
                            //Now hop back onto main thread to do the actual work
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    update_activities();
                                }
                            });

                        }
                    }, 0, 3, TimeUnit.SECONDS);
        }
    }

    private void update_activities() {
        //GET the activities list
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }else{
            Log.w("TOKEN ERROR","What happened to the token :(");
        }

        client.get(MainActivity.base_host_url + "api/getActivities/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET ACTIVITIES SUCCESS", statusCode + ": " + "Response = " + response.toString());
                try {
                    Log.w("GET ACTIVITIES SUCCESS2", response.getString("lastCity"));
                } catch (JSONException e) {
                    Log.w("GET ACTIVITIES FAIL: ", e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
//                Log.w("GET ACTIVITIES SUCCESS3", statusCode + "- JSON ARRAY: " + responseArray.toString());
                activitiesList.clear();

                //Cycle through the list of activities
                for (int i=0; i<responseArray.length(); i++){
                    try {
                        JSONObject activity = responseArray.getJSONObject(i);
                        String name = activity.getString("activity_name");
                        String creator = activity.getString("creator");
                        int maxUsers = activity.getInt("max_users");
                        String activityTimeString = activity.getString("activity_time");
//                        Log.w("parth",activityTimeString);
                        SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        Date activityTime = new Date();
                        //TODO: Improve timzezones for multi-city support
                        try {

                            activityTime = activityTimeFormat.parse(activityTimeString);
                        }catch (ParseException p){
                            Log.w("PARSE EXCEPTION","Something went wrong with DATE parsing");
                        }
                        String activityType = activity.getString("activity_type");
                        double latitude = activity.getDouble("activity_lat");
                        double longitude = activity.getDouble("activity_lon");

                        UserActivity userActivity = new UserActivity(name,
                                creator,
                                maxUsers,
                                activityTime,
                                "Business",
                                activityType,
                                latitude,
                                longitude );

                        activitiesList.add(userActivity);

                        int height = 75;
                        int width = 75;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.canada_icon);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude,longitude))
                                .title(name)
                                .snippet(activityType)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        markerMap.put(name,i);
                        mMap.addMarker(marker);


                    } catch (JSONException e){
                        Log.w("JSON EXCEPTION:", "Error parsing the getActivities response");
                    }

                }

                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET ACTIVITIES RETRY", "TRYING AGAIN");
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET ACTIVITIES FAIL2", "Error Code: " + error_code);
                Log.w("ERROR MESSAGE",text);
            }
        });
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

        if (id == R.id.add_activity){
            Intent intent = new Intent(MapActivity.this,CreateActivity.class);
            MapActivity.this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
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

                    if (currLocationMarker != null) {
                        currLocationMarker.remove();
                    }
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(currentPosition);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    currLocationMarker = mMap.addMarker(markerOptions);
                    markerMap.put("Current Position",-1);

                    if (runOnce) {
                        Toast.makeText(getApplicationContext(), "GPS Coordinates = " + current_gps_latitude + "," + current_gps_longitude, Toast.LENGTH_LONG).show();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_gps_latitude,current_gps_longitude),15));
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
                        if(MainActivity.cheat_mode==false){
                        params.put("last_city", "montreal");
                        }else {
                            params.put("last_city", "middle of nowhere");
                        }

                        client.post(MainActivity.base_host_url + "api/postLocation/", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                Log.w("POST LOCATION SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                                Log.w("POST LOCATION SUCCESS2", statusCode + ": " + timeline.toString());
                            }

                            @Override
                            public void onRetry(int retryNo) {
                                // called when request is retried
                                Log.w("POST LOCATION RETRY", "" + retryNo);
                            }

                            @Override
                            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                Log.w("POST LOCATION FAIL", "Error Code: " + error_code + "," + text);
                            }
                        });

                        Log.w("GPS CITY RESULT", "New City Detected");

                        Intent intent;
                        if(MainActivity.cheat_mode==true){
                            intent = new Intent(MapActivity.this, WhatDoYouWantToDoToday.class);
                        }else {
                            intent = new Intent(MapActivity.this, NewCity.class);
                        }
                        MapActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),current_city, Toast.LENGTH_LONG).show();
                    Log.w("GPS CITY RESULT", "Not in a new city");
                }

//OLD LOCATION OF GET ACTIVITIES

            } else {
                Log.w("GPS LOCATION FAIL", "FAIL");
            }
        } catch (IOException e){
            Log.w("GPS CITY RESULT: ", "FAIL");
        }
    }

    View banner;
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.w("MAP PINS","Pin clicked!");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.app_bar_map);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        banner = layoutInflater.inflate(R.layout.activity_list_row_item,null,true);
        banner.setTranslationY(height - 215);

        int i = (int) markerMap.get(marker.getTitle());

        // Check to see if this is the user's location pin (-1)
        if(i != -1) {
            UserActivity act = (UserActivity) activitiesList.get(i);

            switch (act.getCategory()) {
                case "Art & Culture":
                    ImageView imageView = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView.setImageResource(R.drawable.arts_and_culture);
                    break;
                case "Nightlife":
                    ImageView imageView2 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView2.setImageResource(R.drawable.nightlife);
                    break;
                case "Sports":
                    ImageView imageView3 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView3.setImageResource(R.drawable.sports);
                    break;
                case "Business":
                    ImageView imageView4 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView4.setImageResource(R.drawable.handshake);
                    break;
                case "Date":
                    ImageView imageView5 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView5.setImageResource(R.drawable.wink);
                    break;
                case "Pool":
                    ImageView imageView6 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView6.setImageResource(R.drawable.pool);
                    break;
                case "Outdoors":
                    ImageView imageView7 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView7.setImageResource(R.drawable.backpack);
                    break;
                case "Camping":
                    ImageView imageView8 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView8.setImageResource(R.drawable.camping);
                    break;
                case "Drinks":
                    ImageView imageView9 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView9.setImageResource(R.drawable.cup);
                    break;
                case "Meetup":
                    ImageView imageView10 = (ImageView) banner.findViewById(R.id.activity_type);
                    imageView10.setImageResource(R.drawable.three);
            }
//        (ImageView) banner.findViewById(R.id.profile_picture);
//        (TextView) convertView.findViewById(R.id.created_text);
//        (TextView) convertView.findViewById(R.id.status_text);
//        (TextView) convertView.findViewById(R.id.home_city_text);
//        (ImageView) convertView.findViewById(R.id.country_flag);
//        (ImageView) convertView.findViewById(R.id.points);
//        (ImageView) convertView.findViewById(R.id.activity_type);
            ((TextView) banner.findViewById(R.id.activity_name)).setText(marker.getTitle());
//        (ImageView) convertView.findViewById(R.id.clock_image);
//        (TextView) convertView.findViewById(R.id.activity_time);
//        (ImageView) convertView.findViewById(R.id.pin_image);
//        (TextView) convertView.findViewById(R.id.distance);
//        (RelativeLayout) convertView.findViewById(R.id.row_item);


//        banner.setBottom(height); //Did not work as expected
            layout.addView(banner);
        }
        return false;
    }

    public static void centerOnActivity(String name) {
    //TODO: When you click on a list item, then you should go to its marker on the map... however this is not what the UI suggests therefore leave for later!
        //TODO: Basic solution would be register every marker as a dictionary (or hash map in Java) so that you can reference it by name :)
    }
}
