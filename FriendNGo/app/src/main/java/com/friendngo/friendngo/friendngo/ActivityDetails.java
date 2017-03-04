package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityDetails extends AppCompatActivity {
    FrameLayout requestFrame;
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
        final int i = getIntent().getIntExtra("Activity Index", 0);
        final long activity_pk = ((UserActivity)MapActivity.activitiesList.get(i)).getActivity_pk();
        sendRequestButton = (Button)findViewById(R.id.send_request_button);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }RequestParams params = new RequestParams();
                params.put("activity_id",activity_pk);
                params.put("request_state",0);
                client.post(MainActivity.base_host_url + "api/postActivityRequest/",params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        //TODO: Test and implement statusCode handler for developers and graceful degradation
                        Log.w("POST AR SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        try{
                            Log.w("POST AR SUCCESS2", response.getString("status"));
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
                ActivityDetails.this.finish();
            }
        });

        requestFrame = (FrameLayout)findViewById(R.id.activity_detail_request_frame);
        requestFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeRequest = new Intent(getApplicationContext(), Request.class);
                startActivity(seeRequest);
            }
        });
        int ii = getIntent().getIntExtra("Activity Index", 0);
        UserActivity activity = (UserActivity) MapActivity.activitiesList.get(i);

        //GET The image file at the pictureURL
        AsyncHttpClient client = new AsyncHttpClient();
        String pictureURL = activity.getProfilePicURL();
        creatorPhoto = (ImageView) this.findViewById(R.id.creator_image);
        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
                //Use the downloaded image as the profile picture
                Uri uri = Uri.fromFile(response);
//                    profilePicture = (ImageView) markup_layout.findViewById(R.id.banner_profilepicture);
                creatorPhoto.setImageURI(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL","Could not retrieve image");
            }
        });

        //Get the XML instances for each of the headings
        activityName = (TextView) this.findViewById(R.id.activity_detail_name);
        activityName.setText(activity.getName());
        creatorName = (TextView) this.findViewById(R.id.activity_detail_creator_name);
        creatorName.setText(activity.getCreator());
        creatorAge = (TextView) this.findViewById(R.id.activity_detail_creator_age);
        creatorAge.setText(activity.getCreatorAge());

        creatorStatus = (TextView) this.findViewById(R.id.activity_detail_creator_status);
        creatorStatus.setText(activity.getCreatorStatus()+",");

        creatorHome = (TextView) this.findViewById(R.id.activity_detail_creator_home);
        Log.w("HOME CITY DEBUG", activity.getHomeCity());
        creatorHome.setText(" " + activity.getHomeCity());

        creatorFlag = (ImageView) this.findViewById(R.id.activity_detail_creator_flag);
        creatorFlag.setImageResource(R.drawable.canada);

        activityTime = (TextView) this.findViewById(R.id.activity_detail_date);
        activityTime.setText("7:30 PM - 10:30 PM");//TODO: Include a custom Time object

        activityDate = (TextView) this.findViewById(R.id.activity_detail_time);
        activityTime.setText("Feb 7th, 2017 ");

        activityDescription = (TextView) this.findViewById(R.id.activity_detail_description_text);
        activityDescription.setText(activity.getDescription());

        activityAddress = (TextView) this.findViewById(R.id.activity_type_address_text);
        activityAddress.setText(activity.getAddress());


        participantsRecycler = (RecyclerView) this.findViewById(R.id.participants_recycler_view);
        mHorizontallayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);

        //Set Inner Layout Adapters
        participantsRecycler.setLayoutManager(mHorizontallayoutManager);
        participantsRecycler.setAdapter(mHorizontalAdapter);

//        List<> =
//        int position = 0;
//
//        mHorizontalAdapter = new AttendingHorizontalRow()
//        participantsRecycler.setHasFixedSize(true);
        //TODO: Build The Layout Adapter

        //TODO: Figure out how to get the images



    }
}