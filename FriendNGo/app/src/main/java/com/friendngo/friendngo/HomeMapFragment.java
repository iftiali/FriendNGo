package com.friendngo.friendngo;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private final int STARTING_ZOOM = 15;
    private RelativeLayout alpha_layer;
    private String checkOnlineToast = null;
    private static final int POLLING_PERIOD = 5;
    private Map markerMap = new HashMap();
    private FrameLayout markup_layout;
    private Button activityDetailsButton, participateButton;
    private ImageView profilePicture;
    private TextView name;
    private TextView creator;
    private TextView status;
    private TextView homeCity;
    private ImageView nationality;
    private TextView points;
    private ImageView category;
    private ImageView clock;
    private TextView dateTime;
    private ImageView flagImageOne;
    private ImageView flagImageTwo;
    private ImageView flagImageThree;
    public HomeMapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);


        //Init xml
        initXmlView(view);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        alpha_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markup_layout.setVisibility(View.GONE);
                alpha_layer.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        //Create the map pin for each activity in the list
        //Here is where we schedule the polling of our activities
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

            //This happens in a seperate thread
            public void run() {
                //Now hop back onto main thread to do the actual work
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                       addPinsOnMap();
                    }
                });
            }
        }, 0, POLLING_PERIOD, TimeUnit.SECONDS);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (FacebookLogin.clon != 0 && FacebookLogin.clat != 0) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(FacebookLogin.clat, FacebookLogin.clon), STARTING_ZOOM)); //TODO: Also do this once for Last Known Location at startup
        mMap.setMyLocationEnabled(true);

        }else{
            Log.w("LOCATION ERROR", "Last Known Location is null!!!");
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

          Log.w("MAP PINS","Pin clicked!");
        int i = (int) markerMap.get(marker.getTitle());
        if(i != -1) {
            alpha_layer.setVisibility(View.VISIBLE);
            markup_layout.setVisibility(View.VISIBLE);

          //  Activate the buttons
            activityDetailsButton.setEnabled(true);
            participateButton.setEnabled(true);

            final UserActivity act = (UserActivity) MapActivity.activitiesList.get(i);
            final int j = i;
            participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    postApiParticipate((int) act.getActivity_pk());
                }
            });

            activityDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (act.getisPaid()) {
                        Intent intent = new Intent(getActivity(), ActivityDetailPaidEvent.class);
                        intent.putExtra("Activity Index", j);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(getActivity(), ActivityDetails.class);
                        intent.putExtra("Activity Index", j);
                        startActivity(intent);

                    }
                }
            });

            if(act.getRequest_state()== 0 || act.getRequest_state() == 1 || act.getRequest_state() == 2){
                participateButton.setBackgroundResource(R.drawable.activity_markup_participate_button_grey);
                participateButton.setEnabled(false);
            }else{
                if(MapActivity.userID == act.getcreator_PK()){
                    participateButton.setBackgroundResource(R.drawable.activity_markup_participate_button_grey);
                    participateButton.setEnabled(false);
                }else{
                    participateButton.setBackgroundResource(R.drawable.activity_markup_participate_button);
                    participateButton.setEnabled(true);
                }
            }
            //Connect the Views To their XML
            profilePicture = (ImageView) markup_layout.findViewById(R.id.banner_profilepicture);
            creator = (TextView) markup_layout.findViewById(R.id.banner_created_text);
            status = (TextView) markup_layout.findViewById(R.id.banner_status_text);
            homeCity = (TextView) markup_layout.findViewById(R.id.banner_home_city_text);
            flagImageOne = (ImageView)markup_layout.findViewById(R.id.banner_country_flag_one);
            flagImageTwo = (ImageView)markup_layout.findViewById(R.id.banner_country_flag_two);
            flagImageThree = (ImageView)markup_layout.findViewById(R.id.banner_country_flag_three);
            points = (TextView) markup_layout.findViewById(R.id.banner_points);
            category = (ImageView) markup_layout.findViewById(R.id.banner_activity_type);
            name = (TextView) markup_layout.findViewById(R.id.banner_activity_name);
            clock = (ImageView) markup_layout.findViewById(R.id.banner_clock_image);
            dateTime = (TextView) markup_layout.findViewById(R.id.banner_activity_time);

            name.setText(act.getName());
            name.setTextColor(Color.GRAY);

            if(act.getisPaid()){
                Log.d("Picture",MainActivity.base_host_url+act.getOrganization_logo());
                creator.setText("Created by " + act.getOrganization_name());
                creator.setTextColor(Color.BLACK);
                Picasso.with(getApplicationContext())
                        .load(MainActivity.base_host_url+act.getOrganization_logo())
                        .placeholder(R.drawable.empty_profile)
                        .error(R.drawable.empty_profile)
                        .into(profilePicture);
                flagImageOne.setVisibility(View.GONE);
                flagImageTwo.setVisibility(View.GONE);
                flagImageThree.setVisibility(View.GONE);
             }else {

                flagImageOne.setVisibility(View.GONE);
                flagImageTwo.setVisibility(View.GONE);
                flagImageThree.setVisibility(View.GONE);
               creator.setText("Created by " + act.getCreator());
                creator.setTextColor(Color.BLACK);
                Picasso.with(getApplicationContext())
                        .load(MainActivity.base_host_url+act.getProfilePicURL())
                        .placeholder(R.drawable.empty_profile)
                        .error(R.drawable.empty_profile)
                        .into(profilePicture);
                getLanguages(act.getcreator_PK());

            }
            status.setText( act.getuserStatus()+ ", ");
            status.setTextColor(Color.GRAY);
            homeCity.setText(act.getHomeCity());
            homeCity.setTextColor(Color.GRAY);
           // nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
            points.setText(act.getPoints()+"pts");
            clock.setImageResource(R.drawable.clock);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, HH:mma");
            dateTime.setText(dateFormat.format(act.getActivityTime()));
            dateTime.setTextColor(Color.GRAY);
           // flagImageSelection(act.get)

            switch(act.getCategory()){
                case "Art & Culture":

                    category.setImageResource(R.drawable.art_exposition);
                    break;
                case "Nightlife":
                    category.setImageResource(R.drawable.music);
                    break;
                case "Sports":
                    category.setImageResource(R.drawable.running);
                    break;
                case "Professional & Networking":

                    category.setImageResource(R.drawable.coworking); //TODO: Update when properly spliced
                    break;
                case "Fun & Crazy":
                    category.setImageResource(R.drawable.naked_run);
                    break;
                case "Games":
                    category.setImageResource(R.drawable.billard);
                    break;
                case "Nature & Outdoors":

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

                    category.setImageResource(R.drawable.art_exposition);
            }

        }
        return false;
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

    private void initXmlView(View view){
        //Setup the Map
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Gray layer
        alpha_layer = (RelativeLayout) view.findViewById(R.id.alpha_layer);
        markup_layout = (FrameLayout) view.findViewById(R.id.markup_layout);

        activityDetailsButton = (Button) view.findViewById(R.id.banner_activity_details);
        activityDetailsButton.setEnabled(false);
        participateButton = (Button) view.findViewById(R.id.banner_participate);
        participateButton.setEnabled(false);


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
                        markerMap.put(activity.getName(), i);
                        mMap.addMarker(marker);
            }
        }
    }
    private void postApiParticipate(int j){

        if(ValidationClass.checkOnline(getApplicationContext())){
        AsyncHttpClient client = new AsyncHttpClient();
                    if(SignIn.static_token != null) {
                        client.addHeader("Authorization","Token "+SignIn.static_token);
                    }
                    RequestParams params = new RequestParams();
                    params.put("activity_id",j);
                    params.put("request_state",0);
                    client.post(MainActivity.base_host_url + "api/postActivityRequest/",params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            //Toast.makeText(ActivityDetails.this, "Request Sent", Toast.LENGTH_LONG).show();
                            //TODO: Test and implement statusCode handler for developers and graceful degradation
                            Log.w("POST AR SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            try{
                                Log.w("POST AR SUCCESS2", response.getString("status"));
                                Toast.makeText(getApplicationContext(),"Reset to participant sent",Toast.LENGTH_SHORT).show();
                                participateButton.setBackgroundResource(R.drawable.activity_markup_participate_button_grey);
                                participateButton.setEnabled(false);
                            }catch (JSONException e){
                                Log.w("POST AR FAIL",e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST AR ARRSUCCESS", statusCode + ": " + timeline.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Log.w("POST AR RETRY",""+ retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                            Log.w("POST AR FAILURE", "Error Code: " + error_code+", text: "+text);
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }
}
