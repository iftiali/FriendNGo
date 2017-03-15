package com.friendngo.friendngo;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class ActivityDetailPaidEvent extends AppCompatActivity {
    RelativeLayout activity_detail_free_event;
    TextView activity_detail_creator_name;
    TextView detail_paid_event_address;
    TextView detail_paid_date;
    TextView detail_event_endStartTime;
    Button sendRequestButton;
    FrameLayout requestFrame;
    TextView activity_detail_paid_description_text;
    ImageView detail_event_background_image;
    RecyclerView participantsRecycler;
    private RecyclerView.LayoutManager mHorizontallayoutManager;
    private RecyclerView.Adapter mHorizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paid_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity_detail_paid_description_text= (TextView) this.findViewById(R.id.activity_detail_paid_description_text);
        activity_detail_free_event = (RelativeLayout)findViewById(R.id.activity_detail_free_event);
        activity_detail_creator_name = (TextView)findViewById(R.id.activity_detail_creator_name);
        detail_paid_event_address = (TextView)findViewById(R.id.detail_paid_event_address);
        detail_paid_date = (TextView)findViewById(R.id.detail_paid_date);
        detail_event_endStartTime = (TextView)findViewById(R.id.detail_event_endStartTime);
        sendRequestButton = (Button)findViewById(R.id.send_request_button_paid_event);
        requestFrame = (FrameLayout)findViewById(R.id.activity_detail_paid_request_frame);
        detail_event_background_image = (ImageView) this.findViewById(R.id.detail_event_background_image);
        detail_event_background_image.setScaleType(ImageView.ScaleType.FIT_XY);
        final int activity_index = getIntent().getIntExtra("Activity Index", 0);
        final long activity_pk = ((UserActivity)MapActivity.activitiesList.get(activity_index)).getActivity_pk();

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
                        Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
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
                ActivityDetailPaidEvent.this.finish();
            }
        });
        requestFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeRequest = new Intent(getApplicationContext(), Request.class);
                startActivity(seeRequest);
            }
        });
        UserActivity activity = (UserActivity) MapActivity.activitiesList.get(activity_index);

        AsyncHttpClient client = new AsyncHttpClient();
        String pictureURL = activity.getProfilePicURL();

        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
                //Use the downloaded image as the profile picture
                Uri uri = Uri.fromFile(response);
                detail_event_background_image.setImageURI(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL","Could not retrieve image");
            }
        });

        activity_detail_creator_name.setText(activity.getName());
        detail_paid_event_address.setText(activity.getAddress());
        activity_detail_paid_description_text.setText(activity.getDescription());
        detail_paid_date.setText("Feb 7th, 2017 ");
        detail_event_endStartTime.setText("7:30 PM - 10:30 PM");//TODO: Include a custom Time object


        participantsRecycler = (RecyclerView) this.findViewById(R.id.participants_recycler_view_paid_event);
        mHorizontallayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        participantsRecycler.setLayoutManager(mHorizontallayoutManager);
        mHorizontalAdapter = new AttendingHorizontalRow((UserActivity)MapActivity.activitiesList.get(activity_index), getApplicationContext());
        participantsRecycler.setAdapter(mHorizontalAdapter);
        participantsRecycler.setHasFixedSize(true);

    }

}
