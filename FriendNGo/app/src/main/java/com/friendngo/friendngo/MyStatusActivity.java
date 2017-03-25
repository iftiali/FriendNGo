package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyStatusActivity extends AppCompatActivity {
    private Button residentButton;
    private Button migrantButton;
    private int status = 0;
    private String statusName;
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
        final String currentCity = getIntent().getExtras().getString("cityName");

        residentButton = (Button) findViewById(R.id.resident_button_my_status);
        migrantButton = (Button) findViewById(R.id.migrant_button_my_status);
        touristButton = (Button) findViewById(R.id.tourist_button_my_status);
        studentButton = (Button) findViewById(R.id.student_button_my_status);
        nextButton = (Button) findViewById(R.id.next_button_my_status);
        residentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = RESIDENT;
                statusName = "Resident";
                MapActivity.other_user_location.setText(statusName+", "+currentCity);
                disableOtherButtons();
                residentButton.setActivated(true);
                residentButton.setTextColor(Color.WHITE);
                residentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        migrantButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = MIGRANT;
                statusName = "Migrant";
                MapActivity.other_user_location.setText(statusName+", "+currentCity);
                disableOtherButtons();
                migrantButton.setActivated(true);
                migrantButton.setTextColor(Color.WHITE);
                migrantButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        touristButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = TOURIST;
                statusName = "Tourist";
                MapActivity.other_user_location.setText(statusName+", "+currentCity);
                disableOtherButtons();
                touristButton.setActivated(true);
                touristButton.setTextColor(Color.WHITE);
                touristButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });

        studentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                status = STUDENT;
                statusName = "Student";
                MapActivity.other_user_location.setText(statusName+", "+currentCity);
                disableOtherButtons();
                studentButton.setActivated(true);
                studentButton.setTextColor(Color.WHITE);
                studentButton.setBackgroundResource(R.drawable.white_button_activated);
            }

        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

                RequestParams params = new RequestParams();
                params.setUseJsonStreamer(true);

                params.put("status",statusName);
                // Log.w("Home city",currentCityArray[0]);


                    client.post(MainActivity.base_host_url + "api/postStatus/", params, new JsonHttpResponseHandler() {
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
                MapActivity.other_user_location.setText(statusName+", "+ currentCity);
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
