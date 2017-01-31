package com.friendngo.friendngo.friendngo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.ActivityCompat;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class WhoAreYou extends AppCompatActivity {
    CircularImageView circularImageView;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Who Are you?");

       // isStoragePermissionGranted();
        setContentView(R.layout.activity_who_are_you);

         get_UserInfo();
        //TODO: Make a GET request to get the stored values for the user's profile

        circularImageView = (CircularImageView)findViewById(R.id.profilepicture);
        continueButton = (Button)findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  //TODO: Make HTTP POST call to update the user's profile
                                                  WhoAreYou.this.finish();
                                              }
                                          }
        );
    }
    private void get_UserInfo() {
        //GET the user info
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }else{
            Log.w("TOKEN ERROR","What happened to the token :(");
        }
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("User info", statusCode + ": " + "Response = " + response.toString());
                try {
                    Log.w("User info", response.getString("lastCity"));
                } catch (JSONException e) {
                    Log.w("User info ", e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                Log.w("User info", statusCode + "- JSON ARRAY: " + responseArray.toString());

//asdfas
                //Cycle through the list of activities
                /*for (int i=0; i<responseArray.length(); i++){
                    try {
                        JSONObject activity = responseArray.getJSONObject(i);
                        String name = activity.getString("activity_name");
                        String creator = activity.getString("creator");
                        int maxUsers = activity.getInt("max_users");
                        String activityTimeString = activity.getString("activity_time");
                        Log.w("parth",activityTimeString);
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
            */}

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("User info", "TRYING AGAIN");
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("User info", "Error Code: " + error_code);
                Log.w("User info",text);
            }
        });
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("tag", "Permission is granted");
                return true;
            } else {

                Log.v("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("tag", "Permission is granted");
            return true;
        }
    }

}
