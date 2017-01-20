package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        Spinner category_spinner = (Spinner)findViewById(R.id.category_picker);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Sports");
        spinnerAdapter.add("Nightlife");
        spinnerAdapter.add("Arts & Culture");
        spinnerAdapter.notifyDataSetChanged();

        //TODO: Dynamically create lists
        Spinner activity_type_spinner = (Spinner)findViewById(R.id.activity_type_picker);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_type_spinner.setAdapter(spinnerAdapter2);
        spinnerAdapter2.add("Jogging");
        spinnerAdapter2.add("Tennis");
        spinnerAdapter2.add("Hockey");
        spinnerAdapter2.add("Dancing");
        spinnerAdapter2.add("Jazz");
        spinnerAdapter2.add("Pubs");
        spinnerAdapter2.add("Museum");
        spinnerAdapter2.add("Gallery");
        spinnerAdapter2.add("City Tour");
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

        todayButton = (Button)findViewById(R.id.today_button);
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

                double latitude=0;
                double longitude=0;

                //Determine the latitude and longitude from the address provided
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addressList;
                LatLng p1 = null;
                try {
                    addressList = coder.getFromLocationName(address, 5);

                    if (addressList == null) {
                        Log.w("FORM VALIDATION","ADDRESS IS NULL");
                    }else {
                        Address location = addressList.get(0);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                //POST new activity to the server
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }

                //Set the status type into the message for the server
                RequestParams params = new RequestParams();
                params.put("activity_name",activity_name );
//                params.put("category", category);
                params.put("activity_type", activityType);
                params.put("max_users", maxUsers);
                params.put("activity_lat", Double.toString(latitude) );
                params.put("activity_lon", Double.toString(longitude));

                client.post(MainActivity.base_host_url + "api/postActivity/",params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //TODO: Test and implement statusCode handler for developers and graceful degradation
                        Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
//                        try{
//                            Log.w("STATUS SUCCESS: ", response.getString("status"));
//                        }catch (JSONException e){
//                            Log.w("HTTP JSONException",e.getMessage().toString());
//                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("JSON ARRAY???: ", statusCode + ": " + timeline.toString());
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                        Log.w("HTTP FAILURE", "Error Code: " + error_code);
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
