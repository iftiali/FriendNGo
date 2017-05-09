package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        mMap.setMyLocationEnabled(true);

    }
}