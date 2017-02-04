package com.friendngo.friendngo.friendngo;

import android.graphics.Color;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class CreateActivity extends AppCompatActivity {

    Button createActivityButton;
    Button todayButton;
    Button tomorrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        getSupportActionBar().setTitle("Create a new activity");
        ArrayList<CategorySpinnerModel> list=new ArrayList<>();
        list.add(new CategorySpinnerModel("Arts And Culture",R.drawable.art_exposition));
        list.add(new CategorySpinnerModel("Nightlife",R.drawable.nightlife));
        list.add(new CategorySpinnerModel("Sports",R.drawable.running));
//        list.add(new CategorySpinnerModel("Business",R.drawable.handshake));
        list.add(new CategorySpinnerModel("Dating",R.drawable.naked_run));
        list.add(new CategorySpinnerModel("Activities",R.drawable.billard));
        list.add(new CategorySpinnerModel("Outdoors",R.drawable.backpack));
        list.add(new CategorySpinnerModel("Camping",R.drawable.camping));
        list.add(new CategorySpinnerModel("Drinks",R.drawable.grab_drink));
        list.add(new CategorySpinnerModel("Networking",R.drawable.coworking));

        Spinner category_spinner = (Spinner)findViewById(R.id.category_picker);
        category_spinner.setBackgroundColor(Color.WHITE);
        CategorySpinnerActivity adapter=new CategorySpinnerActivity(CreateActivity.this, R.layout.category_picker,R.id.txt,list);
        category_spinner.setAdapter(adapter);

        //TODO: Dynamically create lists
        Spinner activity_type_spinner = (Spinner)findViewById(R.id.activity_type_picker);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_type_spinner.setAdapter(spinnerAdapter2);
        spinnerAdapter2.add("5 @ 7");
        spinnerAdapter2.add("Startup Weekend");
        spinnerAdapter2.add("Conference");
        spinnerAdapter2.add("See a Movie");
        spinnerAdapter2.add("Wine Tasting");
        spinnerAdapter2.add("Fishing Trip");
        spinnerAdapter2.add("Hiking");
        spinnerAdapter2.add("Clubbing");
        spinnerAdapter2.add("Pool");
        spinnerAdapter2.add("Running");
        spinnerAdapter2.add("Swimming");
        spinnerAdapter2.add("Soccer");
        spinnerAdapter2.add("Hockey");
        spinnerAdapter2.add("Badminton");
        spinnerAdapter2.add("Tennis");
        spinnerAdapter2.add("Dodgeball");
        spinnerAdapter2.add("None Of The Above");
        spinnerAdapter2.notifyDataSetChanged();

        Spinner max_user_spinner = (Spinner)findViewById(R.id.max_participants_spinner);
        ArrayAdapter<String> spinnerAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        max_user_spinner.setAdapter(spinnerAdapter3);
        spinnerAdapter3.add("1");
        spinnerAdapter3.add("2");
        spinnerAdapter3.add("3");
        spinnerAdapter3.add("4");
        spinnerAdapter3.add("5");
        spinnerAdapter3.add("6");
        spinnerAdapter3.add("7");
        spinnerAdapter3.add("8");
        spinnerAdapter3.notifyDataSetChanged();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM");

        todayButton = (Button)findViewById(R.id.today_button);
        todayButton.setText(dateFormat.format(new Date()));
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayButton.setActivated(true);
                todayButton.setTextColor(Color.WHITE);
                todayButton.setBackgroundResource(R.drawable.white_button_activated);
                tomorrowButton.setActivated(false);
                tomorrowButton.setTextColor(Color.BLACK);
                tomorrowButton.setBackgroundResource(R.drawable.white_button);

            }
        });

        tomorrowButton = (Button)findViewById(R.id.tomorrow_button);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime( dateFormat.parse(dateFormat.format(new Date())) );
            cal.add( Calendar.DATE, 1 );
            tomorrowButton.setText((String)(dateFormat.format(cal.getTime())));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomorrowButton.setActivated(true);
                tomorrowButton.setTextColor(Color.WHITE);
                tomorrowButton.setBackgroundResource(R.drawable.white_button_activated);
                todayButton.setActivated(false);
                todayButton.setTextColor(Color.BLACK);
                todayButton.setBackgroundResource(R.drawable.white_button);
            }
        });

        createActivityButton = (Button)findViewById(R.id.create_activity_button);
        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the items selected by the user and put them into an HTTP request
                Spinner category_picker = (Spinner) findViewById(R.id.category_picker);
                String category = category_picker.getSelectedItem().toString();

                Spinner activityTypePicker = (Spinner) findViewById(R.id.activity_type_picker);
                String activityType = activityTypePicker.getSelectedItem().toString();

                Spinner maxUsersPicker = (Spinner) findViewById(R.id.max_participants_spinner);
                String maxUsers = maxUsersPicker.getSelectedItem().toString();

                EditText activityEditText = (EditText) findViewById(R.id.editText);
                String activity_name = activityEditText.getText().toString();

                EditText addressText = (EditText) findViewById(R.id.address_edit_text);
                String address = addressText.getText().toString();

                //Determine the latitude and longitude from the address provided
                Geocoder coder = new Geocoder(getApplicationContext());

                //POST new activity to the server
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }

                //Set the status type into the message for the server
                RequestParams params = new RequestParams();
                params.put("activity_name",activity_name );
//                params.put("category", category);
//                params.put("address",address);
//                params.put("activity_type", activityType);
                params.put("max_users", maxUsers);
//                params.put("activity_lat", Double.toString(ValidationClass.get_Latitude(address,coder)));
//                params.put("activity_lon", Double.toString(ValidationClass.get_longitude(address,coder)));
                params.put("activity_lat", Double.toString(43));
                params.put("activity_lon", Double.toString(-72));
                client.post(MainActivity.base_host_url + "api/postActivity/",params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //TODO: Test and implement statusCode handler for developers and graceful degradation
                        Log.w("POST ACT SUCCESS", statusCode + ": " + "Response = " + response.toString());
//                        try{
//                            Log.w("STATUS SUCCESS: ", response.getString("status"));
//                        }catch (JSONException e){
//                            Log.w("HTTP JSONException",e.getMessage().toString());
//                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST ACT SUCCESS2", statusCode + ": " + timeline.toString());
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        Log.w("POST ACT RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                        Log.w("POST ACT FAIL", "Error Code: " + error_code + "Text: " + text);
                    }
                });
                //TODO: Go somewhere else next sprint? Maybe invite other people?
//                Intent intent = new Intent(CreateActivity.this,XXX.class);
//                CreateActivity.this.startActivity(intent);
                CreateActivity.this.finish();
            }
        });
        }
}
