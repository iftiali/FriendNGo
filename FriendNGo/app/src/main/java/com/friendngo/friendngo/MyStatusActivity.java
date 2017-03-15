package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyStatusActivity extends AppCompatActivity {
    private Button residentButton;
    private Button migrantButton;
    private int status = 0;
    private final int RESIDENT = 1;
    private final int MIGRANT = 2;
    private final int TOURIST = 3;
    private final int STUDENT = 4;
    private Button touristButton;
    private Button studentButton;
    private Button nextButton;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        residentButton = (Button) findViewById(R.id.resident_button_my_status);
        migrantButton = (Button) findViewById(R.id.migrant_button_my_status);
        touristButton = (Button) findViewById(R.id.tourist_button_my_status);
        studentButton = (Button) findViewById(R.id.student_button_my_status);
        nextButton = (Button) findViewById(R.id.next_button_my_status);
        residentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = RESIDENT;
                disableOtherButtons();
                residentButton.setActivated(true);
                residentButton.setTextColor(Color.WHITE);
                residentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        migrantButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = MIGRANT;
                disableOtherButtons();
                migrantButton.setActivated(true);
                migrantButton.setTextColor(Color.WHITE);
                migrantButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        touristButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = TOURIST;
                disableOtherButtons();
                touristButton.setActivated(true);
                touristButton.setTextColor(Color.WHITE);
                touristButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        studentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = STUDENT;
                disableOtherButtons();
                studentButton.setActivated(true);
                studentButton.setTextColor(Color.WHITE);
                studentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyStatusActivity.this, MySpecialGroup.class);
                MyStatusActivity.this.startActivity(intent);
                MyStatusActivity.this.finish();
            }
        });
    }
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

}
