package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityDetails extends FragmentActivity implements OnMapReadyCallback {

    private final int STARTING_ZOOM = 15;
    TextView activityName;
    ImageView creatorPhoto;
    TextView creatorName;
    TextView creatorAge;
    TextView creatorStatus;
    TextView creatorHome;
    ImageView creatorFlag;
    TextView activityDate;
    TextView activityTime;
    TextView activityDescription;
    TextView activityAddress;
    Button sendRequestButton;
    TextView activity_detail_points;
    private GoogleMap mMap;
    private double mMapLat = 0;
    private double mMapLot = 0;
    private ImageView flagImageOne;
    private ImageView flagImageTwo;
    private ImageView flagImageThree;
    RecyclerView participantsRecycler;
    private RecyclerView.LayoutManager mHorizontallayoutManager;
    private RecyclerView.Adapter mHorizontalAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final int activity_index = getIntent().getIntExtra("Activity Index", 0);
        final long activity_pk = ((UserActivity) MapActivity.activitiesList.get(activity_index)).getActivity_pk();
        sendRequestButton = (Button) findViewById(R.id.send_request_button);
        flagImageOne = (ImageView)findViewById(R.id.event_country_flag_one);
        flagImageTwo = (ImageView)findViewById(R.id.event_country_flag_two);
        flagImageThree = (ImageView)findViewById(R.id.event_country_flag_three);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_detail_map);
        mapFragment.getMapAsync(this);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }
                RequestParams params = new RequestParams();
                params.put("activity_id", activity_pk);
                params.put("request_state", 0);
                client.post(MainActivity.base_host_url + "api/postActivityRequest/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(ActivityDetails.this, "Request Sent", Toast.LENGTH_LONG).show();
                        //TODO: Test and implement statusCode handler for developers and graceful degradation
                        Log.w("POST AR SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        try {
                            Log.w("POST AR SUCCESS2", response.getString("status"));
                            sendRequestButton.setBackgroundResource(R.drawable.submit_button_grey);
                            sendRequestButton.setEnabled(false);
                            sendRequestButton.setText("Request sent");
                        } catch (JSONException e) {
                            Log.w("POST AR FAIL", e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST AR ARRSUCCESS", statusCode + ": " + timeline.toString());
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        Log.w("POST AR RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Log.w("POST AR FAILURE", "Error Code: " + error_code + ", text: " + text);
                    }
                });
                ActivityDetails.this.finish();
            }
        });


        UserActivity activity = (UserActivity) MapActivity.activitiesList.get(activity_index);

        if (MapActivity.userID == activity.getcreator_PK()) {
            sendRequestButton.setBackgroundResource(R.drawable.submit_button_grey);
            sendRequestButton.setEnabled(false);
        } else {
            if (activity.getRequest_state() == 0 || activity.getRequest_state() == 1 || activity.getRequest_state() == 2) {
                sendRequestButton.setBackgroundResource(R.drawable.submit_button_grey);
                sendRequestButton.setEnabled(false);
                sendRequestButton.setText("Request sent");
            } else {
                sendRequestButton.setBackgroundResource(R.drawable.submit_button);
                sendRequestButton.setEnabled(true);
            }
        }
        //GET The image file at the pictureURL
        AsyncHttpClient client = new AsyncHttpClient();
        String pictureURL = activity.getProfilePicURL();
        creatorPhoto = (ImageView) this.findViewById(R.id.creator_image);
        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                //Use the downloaded image as the profile picture
                Uri uri = Uri.fromFile(response);
//                    profilePicture = (ImageView) markup_layout.findViewById(R.id.banner_profilepicture);
                creatorPhoto.setImageURI(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL", "Could not retrieve image");
            }
        });

        //Get the XML instances for each of the headings
        activity_detail_points = (TextView) this.findViewById(R.id.activity_detail_points);
        activity_detail_points.setText(activity.getPoints() + "pts");
        activityName = (TextView) this.findViewById(R.id.activity_detail_name);
        activityName.setText(activity.getName());
        creatorName = (TextView) this.findViewById(R.id.activity_detail_creator_name);
        creatorName.setText(activity.getCreator());
        creatorAge = (TextView) this.findViewById(R.id.activity_detail_creator_age);
        creatorAge.setText(activity.getCreatorAge() + " y-o ");

        creatorStatus = (TextView) this.findViewById(R.id.activity_detail_creator_status);
        creatorStatus.setText(activity.getCreatorStatus() + "," + activity.getHomeCity());

//        creatorHome = (TextView) this.findViewById(R.id.activity_detail_creator_home);
//        creatorHome.setText(" " + activity.getHomeCity());

//        creatorFlag = (ImageView) this.findViewById(R.id.activity_detail_creator_flag);
//        creatorFlag.setImageResource(R.drawable.canada);

        activityTime = (TextView) this.findViewById(R.id.activity_detail_time);
        activityTime.setText(ValidationClass.getFormattedTime(activity.getActivityTime()) + " - " + ValidationClass.getFormattedTime(activity.getActivityEndTime()));
        activityDate = (TextView) this.findViewById(R.id.activity_detail_date);

        activityDate.setText(ValidationClass.getFormattedDate(activity.getActivityTime()));
        activityDescription = (TextView) this.findViewById(R.id.activity_detail_description_text);
        activityDescription.setText(activity.getDescription());

        activityAddress = (TextView) this.findViewById(R.id.activity_type_address_text);
        activityAddress.setText(activity.getAddress());
        mMapLat = activity.getLatitude();
        mMapLot = activity.getLongitude();
        flagImageOne.setVisibility(View.GONE);
        flagImageTwo.setVisibility(View.GONE);
        flagImageThree.setVisibility(View.GONE);
        getLanguages(activity.getcreator_PK());
        participantsRecycler = (RecyclerView) this.findViewById(R.id.participants_recycler_view);
        mHorizontallayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        participantsRecycler.setLayoutManager(mHorizontallayoutManager);
        mHorizontalAdapter = new AttendingHorizontalRow((UserActivity) MapActivity.activitiesList.get(activity_index), getApplicationContext());
        participantsRecycler.setAdapter(mHorizontalAdapter);
        participantsRecycler.setHasFixedSize(true);
        //TODO: Build The Layout Adapter

        //TODO: Figure out how to get the images


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(mMapLat, mMapLot);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMapLat, mMapLot), STARTING_ZOOM)); //TODO: Also do this once for Last Known Location at startup
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        addPinsOnMap();
        mMap.setMyLocationEnabled(true);

    }
    private void addPinsOnMap(){
        BitmapDrawable bitmapdraw;
        if(MapActivity.activitiesList.size()<1){
            Log.d("ZERO EVENT","0 EVENTS");
        }else
        {
            for(int i=0;i<MapActivity.activitiesList.size();i++){
                UserActivity activity = (UserActivity) MapActivity.activitiesList.get(i);

                int height = 75;
                int width = 75;

                if(activity.getisPaid()){

                    switch (activity.getCategory()) {
                        case "Art & Culture":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.art_verified);
                            break;
                        case "Nightlife":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.concert_verified);
                            break;
                        case "Sports":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.running_verified);
                            break;
                        case "Professional & Networking":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.coworking_verified);
                            break;
                        case "Fun & Crazy":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.naked_run_verified);
                            break;
                        case "Games":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.billiard_pin);
                            break;
                        case "Nature & Outdoors":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.backpacking_verified);
                            break;
                        case "Travel & Road-Trip":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.camping_verified);
                            break;
                        case "Social Activities":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.grab_drink_verified);
                            break;
                        case "Help & Association":
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.handshake_verified);
                            break;
                        default:
                            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.canada_icon);
                            break;
                    }
                }else {
                    //Create the map pin for each activity in the list

                    switch (activity.getCategory()) {
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
                }
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(activity.getLatitude(), activity.getLongitude()))
                        .title(activity.getName())
                        .snippet(activity.getActivityType())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                mMap.addMarker(marker);
            }
        }

    }
    private void getLanguages(long creator_pk) {
        AsyncHttpClient client = new AsyncHttpClient();

        if(SignIn.static_token != null) {

            client.addHeader("Authorization","Token "+SignIn.static_token);
        }else{

        }
        client.get(MainActivity.base_host_url + "api/getProfilePK/"+creator_pk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET Profile PK", statusCode + ": " + "Response = " + response.toString());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray categoryJSONArray) {
                Log.w("JSON Profile PK", statusCode + ": " + categoryJSONArray.toString());

                try {
                    JSONObject languagesObject = categoryJSONArray.getJSONObject(0);
                    JSONArray languagesArray = languagesObject.getJSONArray("languages");
                    Log.d("Langiage-3",languagesArray.toString());
                    for(int x = 0;x<languagesArray.length();x++){
                        JSONObject languageNames = languagesArray.getJSONObject(x);
                        String languageString = languageNames.getString("name");
                        Log.d("language",languageString);
                        if(x==0){
                            Log.d("language-1",languageString);
                            flagImageOne.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                flagImageOne.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                flagImageOne.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                flagImageOne.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                flagImageOne.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                flagImageOne.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                flagImageOne.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                flagImageOne.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                flagImageOne.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                flagImageOne.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                flagImageOne.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                flagImageThree.setImageResource(R.drawable.it);
                            }else{
                                flagImageOne.setVisibility(View.GONE);
                            }
                        }if(x==1){
                            flagImageTwo.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                flagImageTwo.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                flagImageTwo.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                flagImageTwo.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                flagImageTwo.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                flagImageTwo.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                flagImageTwo.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                flagImageTwo.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                flagImageTwo.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                flagImageTwo.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                flagImageTwo.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                flagImageThree.setImageResource(R.drawable.it);
                            }else{
                                flagImageTwo.setVisibility(View.GONE);
                            }
                        }if(x==2){
                            flagImageThree.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                flagImageThree.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                flagImageThree.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                flagImageThree.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                flagImageThree.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                flagImageThree.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                flagImageThree.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                flagImageThree.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                flagImageThree.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                flagImageThree.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                flagImageThree.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                flagImageThree.setImageResource(R.drawable.it);
                            }else{
                                flagImageThree.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET Profile PK", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){

                Log.w("GET Profile PK", "Error Code: " + error_code + ",  " + json.toString());
            }
        });

    }

}