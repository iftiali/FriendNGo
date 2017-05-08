package com.friendngo.friendngo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    String current_city;
    private String statusName=null;
    TextView new_city_country_name_text_view;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);
        new_city_country_name_text_view = (TextView)findViewById(R.id.new_city_country_name_text_view);

        //get permission
       // getLocationPermission();
        new_city_country_name_text_view.setText(hereLocation(FacebookLogin.clat,FacebookLogin.clon));
        //Sets the top bar text
        //getSupportActionBar().setTitle("New City");

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
                statusName = "Resident";
                residentButton.setActivated(true);
                residentButton.setTextColor(Color.WHITE);
                residentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        migrantButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = MIGRANT;
                disableOtherButtons();
                statusName = "Migrant";
                migrantButton.setActivated(true);
                migrantButton.setTextColor(Color.WHITE);
                migrantButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        touristButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = TOURIST;
                disableOtherButtons();
                statusName = "Tourist";
                touristButton.setActivated(true);
                touristButton.setTextColor(Color.WHITE);
                touristButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        studentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = STUDENT;
                disableOtherButtons();
                statusName = "Student";
                studentButton.setActivated(true);
                studentButton.setTextColor(Color.WHITE);
                studentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });
        current_city = new_city_country_name_text_view.getText().toString();
        final String[] currentCityArray = current_city.split(",");
        new_city_country_name_text_view.setText(currentCityArray[0]);
        //The next button triggers the map activity
        nextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (status == 0){
                    Toast.makeText(getApplicationContext(),"Please Choose A Status",Toast.LENGTH_LONG).show();
                }else{
                    AsyncHttpClient client = new AsyncHttpClient();
                    if(SignIn.static_token != null) {
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
                            Log.w("POST STATUS SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            try{
                                Log.w("POST STATUS SUCCESS2", response.getString("status"));
                            }catch (JSONException e){
                                Log.w("POST STATUS FAIL",e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST STATUS ARRSUCCESS", statusCode + ": " + timeline.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Log.w("POST STATUS RETRY",""+ retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                            Log.w("POST STATUS FAILURE", "Error Code: " + error_code);
                        }
                    });

                    AsyncHttpClient clientCity = new AsyncHttpClient();
                    if (SignIn.static_token != null) {
                        clientCity.addHeader("Authorization", "Token " + SignIn.static_token);
                    }

                    RequestParams paramsCity = new RequestParams();
                    paramsCity.setUseJsonStreamer(true);
                    params.put("home_city",currentCityArray[0]);
                    if(currentCityArray[0].equals("")){
                        Toast.makeText(NewCity.this,"My city field is empty",Toast.LENGTH_LONG).show();
                    }else {
                        clientCity.post(MainActivity.base_host_url + "api/postHomeCity/", params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.w("POST PROFILE CITY", statusCode + ": " + "Response = " + response.toString());
                                // MapActivity.other_user_location.setText("Student, "+currentCityArray[0]);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                                Log.w("POST PROFILE CITY", statusCode + ": " + timeline.toString());
                                // NewWhoAreYouActivity.this.finish();
                            }

                            @Override
                            public void onRetry(int retryNo) {
                                // called when request is retried
                                Log.w("POST PROFILE CITY RETRY", "" + retryNo);
                            }

                            @Override
                            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                Log.w("POST PROFILE  CITY FAIL", "Error Code: " + error_code + "," + text);
                            }

                            @Override
                            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                                Log.w("MY PROFILE CITY FAIL", "Error Code: " + error_code + ",  " + json.toString());
                            }
                        });
                        //MapActivity.other_user_location.setText(statusName +", "+ currentCityArray[0]);
                        //check edit profile path
                        SharedPreferences pref = getSharedPreferences("EditPath", MODE_PRIVATE); // 0 - for private mode
                        String editprofilePath= pref.getString("edit_path", null);
                        Log.d("True",editprofilePath);
                        if(editprofilePath.equals("true")){

                        Intent intent = new Intent(NewCity.this,MySpecialGroup.class);
                        NewCity.this.startActivity(intent);
                        NewCity.this.finish();
                        }
                        else{
                            NewCity.this.finish();
                        }
                    }

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

    public String hereLocation(double lat,double lon){
        String current_city =null;

        Geocoder geocoder = new Geocoder(NewCity.this,Locale.getDefault());
        List<Address> addresses;
        try{
             addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                current_city = addresses.get(0).getLocality();
                if(current_city == null){
                    current_city = addresses.get(0).getSubLocality() +", "+ addresses.get(0).getCountryName();
                }else{
                    current_city = current_city+", "+ addresses.get(0).getCountryName();
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return  current_city;
    }
}

