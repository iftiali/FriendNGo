package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    //TODO: What with favorite???
    TextView activityAddress;
    Button sendRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Set top bar and toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Activity Details");

        sendRequestButton = (Button)findViewById(R.id.send_request_button);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        int i = getIntent().getIntExtra("Activity Index", 0);
        UserActivity activity = (UserActivity) MapActivity.activitiesList.get(i);

        //Get the XML instances for each of the headings
        activityName = (TextView) this.findViewById(R.id.activity_detail_name);
        activityName.setText(activity.getName());

        creatorPhoto = (ImageView) this.findViewById(R.id.creator_image);
        creatorPhoto.setImageResource(R.drawable.scott);

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
    }
}
