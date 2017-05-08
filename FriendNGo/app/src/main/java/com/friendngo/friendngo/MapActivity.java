package com.friendngo.friendngo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapActivity extends AppCompatActivity
    //  implements
    //  NavigationView.OnNavigationItemSelectedListener,
    //  OnMapReadyCallback,
    //  GoogleMap.OnMarkerClickListener
        {

    //Constants
    private static final String My_TAG ="Author:Parth";
            //nav header
   // RelativeLayout buttonClick;
   // public static CircularImageView other_user_picture;
   //public static TextView other_user_name,other_user_age,other_user_about,other_user_location,other_user_citizenship,other_user_points;

    public static String selfIdentify=null;
    public static int versionNumber = 10;
    public static String selfName=null;
    public static boolean checkStateMapOrList = false;
    TextView user_account;
    private static final int POLLING_PERIOD = 5;



    //Map and location variables

    private String last_city;
    private String current_city;
    private boolean current_location_ready = false;
    private double current_gps_latitude;
    private double current_gps_longitude;
    private boolean last_location_ready = false;
    private boolean gettingGPS = true;
    private ImageView createActivityImageView;
    private ImageView filterActivityImageView;
    private ImageView listActivityImageView;

    //Layout instances
    private BottomNavigationView bottomNavigationView;
    private TextView wish_for_today;


    //new updates
    android.support.v4.app.FragmentManager manager;

    //Data Model and Adapters
    public static List activitiesList = new ArrayList<UserActivity>();

    public static List categoryList = new ArrayList<Category>();

    private static boolean run_once = true;
    public  static int userID=0;

    //Fonts Script
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_map); //NOTE: Drawer View is setup later

        //Set top bar and toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendNGo");
        //FireBase Token
        String tkn = FirebaseInstanceId.getInstance().getToken();
        //Log.d("App", "Token ["+tkn+"]");
        postFirebaseToken(tkn);
        //nav drawer
//        other_user_location = (TextView)findViewById(R.id.other_user_location);
//        other_user_picture = (CircularImageView)findViewById(R.id.other_profile_image);
//        other_user_name = (TextView)findViewById(R.id.other_user_name);
//        other_user_age = (TextView)findViewById(R.id.other_user_age);
//        other_user_about = (TextView)findViewById(R.id.other_user_about);
//        other_user_points = (TextView)findViewById(R.id.other_user_points);
//        other_user_citizenship = (TextView)findViewById(R.id.other_user_citizenship);
        //Initialize Layout views from their XML
        user_account = (TextView) findViewById(R.id.user_account);
        wish_for_today = (TextView)findViewById(R.id.wish_for_today);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        createActivityImageView = (ImageView)findViewById(R.id.addImageView);
        filterActivityImageView = (ImageView)findViewById(R.id.filterImage);
        listActivityImageView = (ImageView)findViewById(R.id.listImage);
        filterActivityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, WhatDoYouWantToDoToday.class);
                startActivity(intent);
            }
        });
        listActivityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityListFragment activityListFragment = new ActivityListFragment();
                manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentView, activityListFragment, activityListFragment.getTag()).commit();

            }
        });
        createActivityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
       // buttonClick = (RelativeLayout)findViewById(R.id.buttonClick);
        //Top bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.rgb(255, 117, 0));
//        buttonClick.setBackground(gd);
//        buttonClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapActivity.this,WhatDoYouWantToDoToday.class);
//                startActivity(intent);
//            }
//        });

        //navigate to my profile
//        user_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapActivity.this,MyProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        //default Fragment is Map Fragment
        HomeMapFragment homeMapFragment = new HomeMapFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentView,homeMapFragment,homeMapFragment.getTag()).commit();

        //OnClick listeners for bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.list_icon:
//                               if(checkStateMapOrList == false) {
//                                    ActivityListFragment activityListFragment = new ActivityListFragment();
//                                    manager = getSupportFragmentManager();
//                                    manager.beginTransaction().replace(R.id.fragmentView, activityListFragment, activityListFragment.getTag()).commit();
//                                    checkStateMapOrList = true;
//                                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.home_grey);
//                                    bottomNavigationView.getMenu().getItem(0).setTitle(R.string.home_icon_text);
//                                }else {
//                                    HomeMapFragment homeMapFragment1 = new HomeMapFragment();
//                                    manager = getSupportFragmentManager();
//                                    manager.beginTransaction().replace(R.id.fragmentView, homeMapFragment1, homeMapFragment1.getTag()).commit();
//                                    checkStateMapOrList = false;
//                                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.hamburger);
//                                    bottomNavigationView.getMenu().getItem(0).setTitle(R.string.list_icon_text);
//                                }
                                HomeMapFragment homeMapFragment1 = new HomeMapFragment();
                                manager = getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.fragmentView, homeMapFragment1, homeMapFragment1.getTag()).commit();

                                break;
                            case R.id.calendar_icon:
                              //  Log.w("BOTTOM NAV","Calendar Icon Pressed");
                                Toast.makeText(getApplicationContext(), "Calendar Not Available in Beta", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.notification_icon:
                                NotificationFragment notificationFragment = new NotificationFragment();
                                manager = getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.fragmentView,notificationFragment,notificationFragment.getTag()).commit();
                                break;
                            case R.id.message_icon:
                                ChatFragment chatFragment = new ChatFragment();
                                manager = getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.fragmentView,chatFragment,chatFragment.getTag()).commit();
                                break;
                            case R.id.settings_icon:
                                //Toast.makeText(getApplicationContext(), "Settings Not Available in Beta", Toast.LENGTH_LONG).show();
                                //Intent seeSeeting = new Intent(getApplicationContext(), ReportIssue.class);
                                //startActivity(seeSeeting);
                                SettingFragment settingFragment = new SettingFragment();
                                manager = getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.fragmentView,settingFragment,settingFragment.getTag()).commit();
//                              break;
                            default:
                                //Log.w("NAV DEBUG", "Default called on nav switch... what on earth are you doing???");
                                break;
                        }
                        return true;
                    }
                });


        //Adds the action bar for the drawer
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        //SETUP GET user profile
        AsyncHttpClient client2 = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client2.addHeader("Authorization","Token "+SignIn.static_token);
        }


        //getUserProfile();
        checkForNewVersion();
        getActivity();
        getSelfIdentify();
        //call what you want to do activity each time user lunch our application.
        Intent whatYouWantTODoIntent = new Intent(getApplicationContext(),WhatDoYouWantToDoToday.class);
        startActivity(whatYouWantTODoIntent);
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
                    update_activities();
                    }
                });
            }
        }, 0, POLLING_PERIOD, TimeUnit.SECONDS);
        Log.d("State",String.valueOf(MainActivity.new_user));
        if(MainActivity.new_user == true){
            Intent intent = new Intent(MapActivity.this,NewWhoAreYouActivity.class);
            startActivity(intent);
        }
        /*
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
                    last_city = response.getString("last_city");
                    last_location_ready = true;

                    if (current_location_ready == true) {
                        if(run_once==true){
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




       */
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
                Log.w("GET identity FAIL", "Error Code: " + error_code + ",  " + json);
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
                        String eventPictureURl =  activity.getString("event_picture");
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
                        String userStatus = activity.getString("status");
                        DateFormat converter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        converter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Date activityTime = new Date();
                        TimeZone utc = TimeZone.getTimeZone("UTC");
                        try {
                            activityTimeFormat.setTimeZone(utc);
                            activityTime = activityTimeFormat.parse(activityTimeString);
                        } catch (ParseException p) {
                           Log.w("PARSE EXCEPTION", "Something went wrong with DATE parsing"+p.toString()); //TODO: Why is this failing
                        }
                        String activityEndTimeString = activity.getString("activity_end_time");
                        SimpleDateFormat activityEndTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Date activityEndTime = new Date();
                        try {
                            activityEndTimeFormat.setTimeZone(utc);
                            activityEndTime = activityEndTimeFormat.parse(activityEndTimeString);
                        } catch (ParseException p) {
                            Log.w("PARSE EXCEPTION", "Something went wrong with DATE parsing"+p.toString()); //TODO: Why is this failing
                        }

                        //Calculate the distance from the user to the activity
                        double km;
                        int Radius = 6371;
                        boolean isPaid = activity.getBoolean("is_paid");
                        String organization_logo = activity.getString("organization_logo");
                        String organization_name = activity.getString("organization_name");
                        Boolean is_too_light;
                        if(isPaid){
                        is_too_light = activity.getBoolean("is_too_light");}
                        else{
                        is_too_light = false;
                        }
                        double lat1 = FacebookLogin.clat;//StartP.latitude;
                        double lat2 = latitude;//EndP.latitude;
                        double lon1 = FacebookLogin.clon;//StartP.longitude;
                        double lon2 = longitude;//EndP.longitude;
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
                                activityEndTime,
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
                                request_state,
                                eventPictureURl,
                                userStatus,
                                organization_logo,
                                organization_name,
                                is_too_light);

                        activitiesList.add(userActivity);

                    } catch (JSONException e) {
                        Log.w("JSON EXCEPTION:", "Error parsing the getActivities response"+e.toString());
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

//        if (id == R.id.add_activity) {
//            Intent intent = new Intent(MapActivity.this, CreateActivity.class);
//            MapActivity.this.startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }



////////////////////////////////// GPS ACQUIRE SIGNAL CODE//////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


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
                                intent = new Intent(MapActivity.this, NewWhoAreYouActivity.class);
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



    public static void centerOnActivity(String name) {
    //TODO: When you click on a list item, then you should go to its marker on the map... however this is not what the UI suggests therefore leave for later!
        //TODO: Basic solution would be register every marker as a dictionary (or hash map in Java) so that you can reference it by name :)
    }
    public void getActivity(){
        //get Activity name by category
        //clear category list

        categoryList.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        //death crash
        if(SignIn.static_token != null) {

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

        if(SignIn.static_token != null) {

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

//    private void getUserProfile(){
//        //GET user profile
//        AsyncHttpClient client = new AsyncHttpClient();
//        if (SignIn.static_token != null) {
//            client.addHeader("Authorization", "Token " + SignIn.static_token);
//        }
//        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {
//
//            //GET user profile
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
//                Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
//
//                try {
//                    userID = response.getInt("id");
//                    String firstNameString = response.getString("first_name");
//                    other_user_name.setText(firstNameString);
//                    other_user_points.setText(response.getString("points")+"pts");
//                    int age = response.getInt("age");
//                    if(age > 0) {
//                        other_user_age.setText(age + " y-o");
//                    } else if(age <= 0)
//                    {
//                        other_user_age.setText("X y-o");
//                    }
//                    other_user_citizenship.setText(response.getString("home_nationality"));
//                    String bio = response.getString("bio");
//                    other_user_about.setText(bio);
//
//                    String cityString = response.getString("home_city");
//                    other_user_location.setText(response.getString("status")+", " + cityString);
//                    Boolean needs_submit_promoter_score = response.getBoolean("needs_submit_promoter_score");
//                    if(needs_submit_promoter_score){
//                        Intent intent = new Intent(getApplicationContext(),Recommend.class);
//                        startActivity(intent);
//                    }
//                } catch (JSONException e) {
//                    Log.w("JSON EXCEPTION", e.getMessage());
//                }
//
//                String pictureURL="";
//                //GET Profile image from backend if not available from Facebook
//                if(FacebookLogin.facebook_profile_pic == null) {
//                    //GET The image file at the pictureURL
//                    AsyncHttpClient client = new AsyncHttpClient();
//                    try {
//                        pictureURL = response.getString("picture");
//                    } catch (JSONException e) {
//                        Log.w("GET PROFILE JSON FAIL", e.getMessage().toString());
//                    }
//                    client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, File response) {
//                            Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
//                            //Use the downloaded image as the profile picture
//                            Uri uri = Uri.fromFile(response);
//                            other_user_picture.setImageURI(uri);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                            Log.w("GET IMAGE FAIL", "Could not retrieve image");
//                        }
//                    });
//                }
//            }
//        });
//
//        if(FacebookLogin.facebook_profile_pic != null) {
//            other_user_picture.setImageURI(FacebookLogin.facebook_profile_pic);
//        }
//    }
    private void postFirebaseToken(String token){
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }
        RequestParams params = new RequestParams();
        params.put("registration_id", token);
      //  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
       // telephonyManager.getDeviceId();
        params.put("device_id","3452352352345");
       // Log.d("device_id", telephonyManager.getDeviceId());
        client.post(MainActivity.base_host_url + "api/registerNotifications/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("POST TOKEN SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.w("POST TOKEN SUCCESS2", statusCode + ": " + timeline.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("POST TOKEN RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("POST TOKEN FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("MY TOKEN FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });
    }
}
//


