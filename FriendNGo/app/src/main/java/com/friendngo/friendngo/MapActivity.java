package com.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.params.Face;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import static android.location.LocationManager.GPS_PROVIDER;
//death crash
public class MapActivity extends AppCompatActivity implements

        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    //Constants
    private static final String My_TAG ="Author:Parth";
    TextView other_account;
    public static CircularImageView other_user_picture;
    public static String selfIdentify=null;
    public static int versionNumber = 3;
    public static String selfName=null;
    public static TextView other_user_name,other_user_age,other_user_about,other_user_location;
    ImageView my_profile_dots;
    private static final int POLLING_PERIOD = 5;
    private final int STARTING_ZOOM = 15;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    //Map and location variables
    private GoogleMap mMap;
    private String last_city;
    private String current_city;
    private boolean current_location_ready = false;
    private double current_gps_latitude;
    private double current_gps_longitude;
    private boolean last_location_ready = false;
    private boolean gettingGPS = true;



    //Layout instances
    FrameLayout markup_layout;
    RelativeLayout alpha_layer;
    ImageView profilePicture;
    TextView name;
    TextView creator;
    TextView status;
    TextView homeCity;
    ImageView nationality;
    TextView points;
    ImageView category;
    ImageView clock;
    TextView dateTime;
    Button activityDetailsButton, participateButton;

    //new updates


    //Data Model and Adapters
    public static List activitiesList = new ArrayList<UserActivity>();
    Map markerMap = new HashMap();
    public static List categoryList = new ArrayList<Category>();
    BottomNavigationView bottomNavigationView;
    private static boolean run_once = true;


    //Fonts Script
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map); //NOTE: Drawer View is setup later

        //Set top bar and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendNGo");

        Log.i(My_TAG,"onCreate invoked Map");
        //nav drawer
        other_user_location = (TextView)findViewById(R.id.other_user_location);
        other_user_picture = (CircularImageView)findViewById(R.id.other_profile_image);
        other_user_name = (TextView)findViewById(R.id.other_user_name);
        other_user_age = (TextView)findViewById(R.id.other_user_age);
        other_user_about = (TextView)findViewById(R.id.other_user_about);
        //Initialize Layout views from their XML
        my_profile_dots = (ImageView)findViewById(R.id.my_profile_dots);
        other_account = (TextView)findViewById(R.id.other_account);
        alpha_layer = (RelativeLayout) findViewById(R.id.alpha_layer);
        markup_layout = (FrameLayout) findViewById(R.id.markup_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        activityDetailsButton = (Button) findViewById(R.id.banner_activity_details);
        activityDetailsButton.setEnabled(false);
        participateButton = (Button) findViewById(R.id.banner_participate);
        participateButton.setEnabled(false);
        //death crash
        checkForNewVersion();
        getActivity();
        getSelfIdentify();
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        other_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this,WhatDoYouWantToDoToday.class);
                startActivity(intent);
            }
        });

        //navigate to my profile
        my_profile_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this,MyProfileActivity.class);
                startActivity(intent);
            }
        });

        //OnClick listeners for bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.list_icon:
                                Intent intent = new Intent(MapActivity.this, ActivityListActivity.class);
                                MapActivity.this.startActivity(intent);
                                break;
                            case R.id.calendar_icon:
                                Log.w("BOTTOM NAV","Calendar Icon Pressed");
                                Toast.makeText(getApplicationContext(), "Calarndar Not Available in Beta", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.notification_icon:
                                Log.w("BOTTOM NAV","Notifications Icon Pressed");
                                Intent seeRequest = new Intent(getApplicationContext(), ActivityNotifications.class);
                                startActivity(seeRequest);
                                break;
                            case R.id.message_icon:
                                Intent seeMessage = new Intent(getApplicationContext(), ActivityMessage.class);
                                startActivity(seeMessage);
                                Log.w("BOTTOM NAV","Message Icon Pressed");
                                break;
                            case R.id.settings_icon:
                                Toast.makeText(getApplicationContext(), "Settings Not Available in Beta", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.w("NAV DEBUG", "Default called on nav switch... what on earth are you doing???");
                                break;
                        }
                        return true;
                    }
                });

        //TODO: Click should not disable the banner when you click on the banner.
        alpha_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markup_layout.setVisibility(View.GONE);
                alpha_layer.setVisibility(View.GONE);
            }
        });


        //GET user profile
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            //GET user profile
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                Log.w("Response",response.toString());
                String langName = null;
                try {
                    String firstNameString = response.getString("first_name");
                    MapActivity.other_user_name.setText(firstNameString);

                    int age = response.getInt("age");
                    if(age > 0) {
                        MapActivity.other_user_age.setText(age + " y-o");
                    } else if(age <= 0)
                    {
                        MapActivity.other_user_age.setText("X y-o");
                    }

                    String bio = response.getString("bio");
                    MapActivity.other_user_about.setText(bio);

                    String cityString = response.getString("home_city");
                    other_user_location.setText("Resident, " + cityString);
                } catch (JSONException e) {
                    Log.w("JSON EXCEPTION", e.getMessage());
                }

                String pictureURL="";
                //GET Profile image from backend if not available from Facebook
                if(FacebookLogin.facebook_profile_pic == null) {
                    //GET The image file at the pictureURL
                    AsyncHttpClient client = new AsyncHttpClient();
                    try {
                        pictureURL = response.getString("picture");
                    } catch (JSONException e) {
                        Log.w("GET PROFILE JSON FAIL", e.getMessage().toString());
                    }
                    client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File response) {
                            Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                            //Use the downloaded image as the profile picture
                            Uri uri = Uri.fromFile(response);
                            MapActivity.other_user_picture.setImageURI(uri);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            Log.w("GET IMAGE FAIL", "Could not retrieve image");
                        }
                    });
                }
            }
        });

        if(FacebookLogin.facebook_profile_pic != null) {
            MapActivity.other_user_picture.setImageURI(FacebookLogin.facebook_profile_pic);
        }

        //Setup the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //GET last known location setup
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }
        //death crash
        //GET last known location
        client.get(MainActivity.base_host_url + "api/getLastLocation/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET LASTLOC SUCCESS", statusCode + ": " + "Response = " + response.toString());

                try {
                    Log.w("GET LOCATION: ", statusCode + ", " + response.getString("last_city"));
                    last_city = response.getString("last_city");
                    last_location_ready = true;

                    if (current_location_ready == true) {
                        if(run_once==true){
                            Log.w("Location","2");
                            update_city();
                        run_once=false;
                        }
                    }
                } catch (JSONException e) {
                    Log.w("GET LASTLOC FAIL: ", e.getMessage().toString());
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
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET LASTLOC FAILURE2:", "Error Code: " + error_code + ", Text:" + text);
            }
        });

        //REMOVE THIS BUTTON ONCE WE HAVE A FACEBOOK LOGOUT BUTTON


        //Adds the action bar for the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SETUP GET user profile
        AsyncHttpClient client2 = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client2.addHeader("Authorization","Token "+SignIn.static_token);
        }
       //Here is where we schedule the polling of our activities
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

            //This happens in a seperate thread
            public void run() {
                //Now hop back onto main thread to do the actual work
                runOnUiThread(new Runnable() {
                    //death crash
                    @Override
                    public void run() {
                        //death crash
                        update_activities();
                    }
                });
            }
        }, 0, POLLING_PERIOD, TimeUnit.SECONDS);
    }

    private void getSelfIdentify() {

        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }

        client.get(MainActivity.base_host_url + "api/getSelfIdentity", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET identity SUCCESS", statusCode + ": " + "Response = " + response.toString());

                try {
                    selfIdentify = response.getString("id");
                    selfName = response.getString("first_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {
                Log.w("GET identity SUCCESS-2", statusCode + ": " + chatJsonArray.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET idetity  RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET identity FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("GET identity FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });
    }

    ////////////////// GETs the activities list and processes them ////////////////////////////////////
    private void update_activities() {
        //GET the activities list
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            //death crash
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        } else {
            //death crash
            Log.w("TOKEN ERROR", "What happened to the token :(");
        }
        //death crash
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
                activitiesList.clear();

                Log.w("ACTIVITIES LIST", responseArray.toString());
                //Cycle through the list of activities and add them to a list
                for (int i = 0; i < responseArray.length(); i++) {
                    try {
                        //Parse all the JSON for this activity
                        JSONObject activity = responseArray.getJSONObject(i);
                        String pictureURL = activity.getString("picture");
                        String name = activity.getString("activity_name");
                        String categoryString = activity.getString("category");
                        String activityType = activity.getString("activity_type");
                        String description = activity.getString("description");
                        double latitude = activity.getDouble("activity_lat");
                        double longitude = activity.getDouble("activity_lon");
                        String address = activity.getString("address");

                        //Parse all the JSON for the creator
                        String creator = activity.getString("creator");
                        String creator_age = activity.getString("creator_age");
                        String creator_status = activity.getString("status");
                        int maxUsers = activity.getInt("max_users");
                        String home_city = activity.getString("home_city");
                        String home_nationality = activity.getString("home_nationality");
                        String points = activity.getString("points");
                        long creator_pk = activity.getLong("creator_pk");
                        long activity_pk = activity.getLong("id");
                        JSONArray attendingJSONArray = activity.getJSONArray("attending");
                        JSONArray requests_received = activity.getJSONArray("requests_received");
                        int request_state = -1;
                        if(requests_received.length()>0){
                            JSONObject request = requests_received.getJSONObject(0);
                            request_state = request.getInt("request_state");
                        } else{
                           // Log.w("REQUESTS EMPTY", "Yup... that happened... ");
                        }


                        //Date parsed seperately
                        String activityTimeString = activity.getString("activity_time");
                        SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        Date activityTime = new Date();
                        try {
                            activityTime = activityTimeFormat.parse(activityTimeString);
                        } catch (ParseException p) {
//                            Log.w("PARSE EXCEPTION", "Something went wrong with DATE parsing"); //TODO: Why is this failing
                        }

                        //Calculate the distance from the user to the activity
                        double km;
                        int Radius = 6371;

                        double lat1 = FacebookLogin.clat;//StartP.latitude;
                        double lat2 = latitude;//EndP.latitude;
                        double lon1 = FacebookLogin.clon;//StartP.longitude;
                        double lon2 = longitude;//EndP.longitude;
                        double dLat = Math.toRadians(lat2 - lat1);
                        double dLon = Math.toRadians(lon2 - lon1);
                        boolean isPaid = activity.getBoolean("is_paid");
                        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                + Math.cos(Math.toRadians(lat1))
                                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                                * Math.sin(dLon / 2);
                        double c = 2 * Math.asin(Math.sqrt(a));
                        double valueResult = Radius * c;
                        km = valueResult / 1;
                        if(km<0.1){
                            km=0.1;
                        }

                        DecimalFormat df = new DecimalFormat("#.#");
                        String distance = df.format(km);

                        List attendingList = new ArrayList<JSONObject>();

                        //Parse the JSONArray of attending users
                        for(int j =0; j<attendingJSONArray.length(); j++){
                            JSONObject json_j = attendingJSONArray.getJSONObject(j);
                            attendingList.add(json_j);
                        }

                        //Create new UserActivity instance with the data
                        UserActivity userActivity = new UserActivity(
                                home_city,
                                home_nationality,
                                name,
                                creator,
                                creator_age,
                                creator_status,
                                maxUsers,
                                activityTime,
                                address,
                                description,
                                distance,
                                points,
                                categoryString,
                                activityType,
                                latitude,
                                longitude,
                                creator_pk,
                                activity_pk,
                                activityTime, //TODO: Put the end time instead of a copy of the start time
                                pictureURL,
                                isPaid,
                                attendingList,
                                request_state);

                        activitiesList.add(userActivity);

                        int height = 75;
                        int width = 75;

                        //Create the map pin for each activity in the list
                        BitmapDrawable bitmapdraw;
                        switch (categoryString) {
                            case "Art & Culture":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.art_exposition_pin);
                                break;
                            case "Nightlife":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.concert_pin);
                                break;
                            case "Sports":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.running_pin);
                                break;
                            case "Professional & Networking":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.coworking_pin);
                                break;
                            case "Fun & Crazy":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.naked_run_pin);
                                break;
                            case "Games":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.billiard_pin);
                                break;
                            case "Nature & Outdoors":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.backpack_pin);
                                break;
                            case "Travel & Road-Trip":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.camping_pin);
                                break;
                            case "Social Activities":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.grab_drink_pin);
                                break;
                            case "Help & Association":
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.handshake_pin);
                                break;
                            default:
                                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.canada_icon);
                                break;
                        }

                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(name)
                                .snippet(activityType)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        markerMap.put(name, i);
                        mMap.addMarker(marker);
                    } catch (JSONException e) {
                        Log.w("JSON EXCEPTION:", "Error parsing the getActivities response");
                    }
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET ACTIVITIES RETRY", "TRYING AGAIN");
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                //death crash
                Log.w("GET ACTIVITIES FAIL2", "Error Code: " + error_code+ ", Text: "+text);
            }

            //death crash
            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                if(json!=null){
                    Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                }
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

        if (id == R.id.add_activity) {
            Intent intent = new Intent(MapActivity.this, CreateActivity.class);
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

        //ZOOM Camera to the last known location
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);

        if (location != null) {
            //Zoom to last known location if we don't have GPS
            if (gettingGPS) {
             // current_gps_latitude = location.getLatitude();
               // current_gps_longitude = location.getLongitude();
               current_gps_latitude = FacebookLogin.clat;
               current_gps_longitude = FacebookLogin.clon;
                Log.i(My_TAG,current_gps_latitude+":"+current_gps_longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_gps_latitude,current_gps_longitude),STARTING_ZOOM)); //TODO: Also do this once for Last Known Location at startup
                current_location_ready = true;
                if (last_location_ready == true) {
                    if(run_once==true) {
                        Log.w("Location","3");
                        update_city();
                        run_once=false;
                    }
                }
                mMap.setMyLocationEnabled(true);
            }
        }else if(FacebookLogin.clon != 0 && FacebookLogin.clat != 0){
            if (gettingGPS) {
                current_gps_latitude = FacebookLogin.clat;
                current_gps_longitude = FacebookLogin.clon;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_gps_latitude,current_gps_longitude),STARTING_ZOOM)); //TODO: Also do this once for Last Known Location at startup
                current_location_ready = true;
                if (last_location_ready == true) {

                    if(run_once==true) {
                        Log.w("Location","4");
                        update_city();
                        run_once=false;
                    }
                }
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            Log.w("LOCATION ERROR", "Last Known Location is null!!!");
        }

    }


////////////////////////////////// GPS ACQUIRE SIGNAL CODE//////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Trigger the check for GPS even before we load the page
   /* @Override
    public void onStart() {
        Log.w(My_TAG,"onStart map");

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
            final LocationListener locationListener = new LocationListener() {

////////////////////////////////// USING GPS CODE //////////////////////////////////////////////////
                //Here is where we receive the location update
                public void onLocationChanged(Location location) {

                    current_gps_latitude = location.getLatitude();
                    current_gps_longitude = location.getLongitude();
                    Log.i(My_TAG,current_gps_latitude+":"+current_gps_longitude);
                    if (gettingGPS) {
                      // Toast.makeText(getApplicationContext(), "GPS Coordinates = " + current_gps_latitude + "," + current_gps_longitude, Toast.LENGTH_LONG).show();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_gps_latitude,current_gps_longitude),STARTING_ZOOM)); //TODO: Also do this once for Last Known Location at startup
                        gettingGPS = false;
                        current_location_ready = true;
                        if (last_location_ready == true) {
                            if(run_once==true) {
                                Log.w("Location","1");
                                update_city();
                                run_once=false;
                            }
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
*/
    private String calculate_Distance(String strAddress){
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        double km = 0;
        DecimalFormat df = new DecimalFormat("#.#");
        try {
            address = coder.getFromLocationName(strAddress,5); //TODO: This needs to be ASYNCHRONOUS
            if (address==null || address.size()==0) {
                return "0";
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            int Radius = 6371;
            double lat1 = FacebookLogin.clat;//StartP.latitude;
            double lat2 = location.getLatitude();//EndP.latitude;
            double lon1 = FacebookLogin.clon;//StartP.longitude;
            double lon2 = location.getLongitude();//EndP.longitude;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;
            km = valueResult / 1;
            if(km<0.1){
                km=0.1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return df.format(km);
    }

    //Helper function to calculate the distance from the last known location
    //This could possibly be replaced by an address poll with a city name parser
    private void update_city() {
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(current_gps_latitude, current_gps_longitude, 1);
            if (addresses.size() > 0) {
                current_city = addresses.get(0).getLocality();
                if(current_city == null){
                    current_city = addresses.get(0).getSubLocality() +", "+ addresses.get(0).getCountryName();
                }else{
                    current_city = current_city+", "+ addresses.get(0).getCountryName();
                }

                String montreal_center_point_address="5430 Chemin de la Côte-de-Liesse\n" +
                        "Mont-Royal, QC H4P 1A6";

                String distanceFromCityCenter = calculate_Distance(montreal_center_point_address);
                Log.w("GPS CITY RESULT", distanceFromCityCenter);

                //TODO: Make this dynamic when we expand to more cities or go public
//                if(Double.valueOf(distanceFromCityCenter)>=30) {
                  if(true){

                    //POST Location
                    AsyncHttpClient client = new AsyncHttpClient();
                    if (SignIn.static_token != null) {
                        client.addHeader("Authorization", "Token " + SignIn.static_token);
                    }

                    RequestParams params = new RequestParams();
                    if (MainActivity.cheat_mode == false) {
                        params.put("last_city", last_city);
                    } else {
                        params.put("last_city", "Toronto");
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

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                            Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                        }
                    });
                    Intent intent;

                    Log.w("GPS CITY RESULT", "New City Detected");
                        if (MainActivity.cheat_mode == true) {
                            intent = new Intent(MapActivity.this, NewCity.class);
                        } else {

                            if (MainActivity.new_user == true) {
                                intent = new Intent(MapActivity.this, NewCity.class);
                                MapActivity.this.startActivity(intent);
                            } else {
                                Log.w("PROFILE DEBUG", "PROFILE ALREADY CREATED");
                            }
                        }
                    }
                else{
                        Toast.makeText(getApplicationContext(), current_city, Toast.LENGTH_LONG).show();
                        Log.w("GPS CITY RESULT", "Not in a new city");
                    }

            //OLD LOCATION OF GET ACTIVITIES
            } else {
                Log.w("GPS LOCATION FAIL", "FAIL");
            }
        } catch (IOException e){
            Log.w("GPS CITY RESULT", "FAIL");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
      //  Log.w("MAP PINS","Pin clicked!");
        int i = (int) markerMap.get(marker.getTitle());
        if(i != -1) {
            alpha_layer.setVisibility(View.VISIBLE);
            markup_layout.setVisibility(View.VISIBLE);


            //Activate the buttons
            activityDetailsButton.setEnabled(true);
            participateButton.setEnabled(true);

            final int j = i;
            //Set On Click Listeners
            activityDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Intent intent = new Intent(MapActivity.this,ActivityDetails.class);
                    intent.putExtra("Activity Index",j);
                 MapActivity.this.startActivity(intent);
                }
            });

            //Set On Click Listeners
            participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MapActivity.this,ActivityDetails.class);
                    intent.putExtra("Activity Index",j);
                    MapActivity.this.startActivity(intent);
                }
            });

            UserActivity act = (UserActivity) activitiesList.get(i);
            //Log.w("address",act.getAddress());

            //Connect the Views To their XML
            profilePicture = (ImageView) markup_layout.findViewById(R.id.banner_profilepicture);
            creator = (TextView) markup_layout.findViewById(R.id.banner_created_text);
            status = (TextView) markup_layout.findViewById(R.id.banner_status_text);
            homeCity = (TextView) markup_layout.findViewById(R.id.banner_home_city_text);
            nationality = (ImageView) markup_layout.findViewById(R.id.banner_country_flag);
            points = (TextView) markup_layout.findViewById(R.id.banner_points);
            category = (ImageView) markup_layout.findViewById(R.id.banner_activity_type);
            name = (TextView) markup_layout.findViewById(R.id.banner_activity_name);
            clock = (ImageView) markup_layout.findViewById(R.id.banner_clock_image);
            dateTime = (TextView) markup_layout.findViewById(R.id.banner_activity_time);

            name.setText(act.getName());
            name.setTextColor(Color.GRAY);
            creator.setText("Created by "+act.getCreator());
            creator.setTextColor(Color.GRAY);
            profilePicture.setImageResource(R.drawable.empty_profile);
            status.setText("Resident" + ", ");
            status.setTextColor(Color.GRAY);
            homeCity.setText(act.getHomeCity());
            homeCity.setTextColor(Color.GRAY);
            nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
            points.setText(act.getPoints()+"pts");
            clock.setImageResource(R.drawable.clock);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, HH:mma");
            dateTime.setText(dateFormat.format(act.getActivityTime()));
            dateTime.setTextColor(Color.GRAY);

            Log.w("CREATING BANNER",act.getCategory());
            switch(act.getCategory()){
                case "Art & Culture":
                 //   Log.w("CREATING BANNER","IN ART");
                    category.setImageResource(R.drawable.art_exposition);
                    break;
                case "Nightlife":
                    category.setImageResource(R.drawable.music);
                    break;
                case "Sports":
                    category.setImageResource(R.drawable.running);
                    break;
                case "Professional & Networking":
                  //  Log.w("CREATING BANNER","IN Networking");
                    category.setImageResource(R.drawable.coworking); //TODO: Update when properly spliced
                    break;
                case "Fun & Crazy":
                    category.setImageResource(R.drawable.naked_run);
                    break;
                case "Games":
                    category.setImageResource(R.drawable.billard);
                    break;
                case "Nature & Outdoors":
                    //Log.w("CREATING BANNER","IN OUTDOORS");
                    category.setImageResource(R.drawable.backpack);
                    break;
                case "Travel & Road-Trip":
                    category.setImageResource(R.drawable.camping);
                    break;
                case "Social Activities":
                    category.setImageResource(R.drawable.grab_drink);
                    break;
                case "Help & Association":
                    category.setImageResource(R.drawable.handshake);
                    break;
                default:
                    //Log.w("CREATING BANNER","IN DEFAULT");
                    category.setImageResource(R.drawable.art_exposition);
            }

            //GET The image file at the pictureURL
            AsyncHttpClient client = new AsyncHttpClient();

            String pictureURL = ((UserActivity)activitiesList.get(j)).getProfilePicURL();

            client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    Log.w("GET IMAGE SUCCESS1","Successfully Retrieved The Image");
                    //Use the downloaded image as the profile picture
                    Uri uri = Uri.fromFile(response);
//                    profilePicture = (ImageView) markup_layout.findViewById(R.id.banner_profilepicture);
                    profilePicture.setImageURI(uri);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Log.w("GET IMAGE FAIL","Could not retrieve image");
                }
            });
        }
        return false;
    }

    public static void centerOnActivity(String name) {
    //TODO: When you click on a list item, then you should go to its marker on the map... however this is not what the UI suggests therefore leave for later!
        //TODO: Basic solution would be register every marker as a dictionary (or hash map in Java) so that you can reference it by name :)
    }
    public void getActivity(){
        //get Activity name by category
        //clear category list
        //death crash
        categoryList.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        //death crash
        if(SignIn.static_token != null) {
            //death crash
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }else{
            Log.i(My_TAG,"token null"+"Map");
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

                for (int i =0; i < categoryJSONArray.length(); i++){

                    try {
                        JSONObject categoryJSONObject = categoryJSONArray.getJSONObject(i);
                        Category category = new Category();
                        category.setName(categoryJSONObject.getString("name"));
                        JSONArray activitiesJSONArray = categoryJSONObject.getJSONArray("activity_type");

                        for (int j=0; j< activitiesJSONArray.length(); j++){
                            String activityType = activitiesJSONArray.getJSONObject(j).getString("name");
                            category.addActivityType(activityType);
                        }
                        categoryList.add(category);
                    } catch (JSONException e) {
                        Log.w("GET CATEGORY PARSE FAIL", e.getMessage().toString());
                    }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(My_TAG,"onResume invoked map");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(My_TAG,"onPause invoked map");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(My_TAG,"onStop invoked map");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(My_TAG,"onRestart invoked map ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(My_TAG,"onDestroy invoked map");
    }
    public boolean checkForNewVersion(){
        AsyncHttpClient client = new AsyncHttpClient();
        //death crash
        if(SignIn.static_token != null) {
            //death crash
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }else{
            Log.i(My_TAG,"token null"+"Map");
        }
        client.get(MainActivity.base_host_url + "api/getVersionStatus/"+versionNumber, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET NEW UPDATE READY", statusCode + ": " + "Response = " + response.toString());
                try{

                    //versionNumber = response.getDouble("must_update");

                    if(response.getBoolean("is_deprecated")){
                        /*Keys for version_code
                        created_at_timestamp,is_deprecated,application_notes*/
                       // Log.w("VERSION NUMBER",response.getString("application_notes"));
                        Toast.makeText(getApplicationContext(),response.getString("application_notes"),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Log.w("GET NEW UPDATE READY",e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray categoryJSONArray) {
                Log.w("JSON NEW UPDATE READY", statusCode + ": " + categoryJSONArray.toString());



            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("POST NEW UPDATE READY", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("MY NEW UPDATE READY", "Error Code: " + error_code + ",  " + json.toString());
            }
        });
        return true;
    }
}
//

