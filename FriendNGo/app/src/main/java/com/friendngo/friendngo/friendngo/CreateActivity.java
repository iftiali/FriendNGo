package com.friendngo.friendngo.friendngo;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateActivity extends AppCompatActivity {

    TextView plus_minus_textview;
    Button plus_button,minus_button;
    TimePicker startTimePicker;
    TimePicker endTimePicker;
    public ArrayList<CategorySpinnerModel> cust_category = new ArrayList<CategorySpinnerModel>();
    TextView startEventTime,endEventTime;
    Button createActivityButton;
    Button todayButton;
    Button tomorrowButton;
    SimpleDateFormat sdf;
    String currentDateEndTime,currentDateStartTime;
    Calendar c = Calendar.getInstance();
    TimePickerDialog.OnTimeSetListener endTimer = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            sdf = new SimpleDateFormat("HH:mm");
            currentDateEndTime = sdf.format(new Date());

            try {
                Date start = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(currentDateEndTime);
                Date end = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(hourOfDay + ":" + minute);
                if (start.compareTo(end) > 0) {
                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateEndTime = sdf.format(new Date());
                    endEventTime.setText(currentDateEndTime);
                } else if (start.compareTo(end) < 0) {

                    //Log.w("Hello",currentDateEndTime+":" +hourOfDay + ":" + minute);
                    endEventTime.setText(hourOfDay + ":" + minute);
                } else if (start.compareTo(end) == 0) {
                    endEventTime.setText(hourOfDay + ":" + minute);

                } else {
                    endEventTime.setText(currentDateStartTime);
                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateEndTime = sdf.format(new Date());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
    TimePickerDialog.OnTimeSetListener startTimer = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            sdf = new SimpleDateFormat("HH:mm");
            currentDateStartTime = sdf.format(new Date());

            try {
                Date start = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(currentDateStartTime);
                Date end = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(hourOfDay + ":" + minute);
                if (start.compareTo(end) > 0) {

                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateStartTime = sdf.format(new Date());
                    //Log.w("Hello1",currentDateStartTime+":" +hourOfDay + ":" + minute);
                    startEventTime.setText(currentDateStartTime);
                } else if (start.compareTo(end) < 0) {
                    //Log.w("Hello2",currentDateStartTime+":" +hourOfDay + ":" + minute);
                    startEventTime.setText(hourOfDay + ":" + minute);
                } else if (start.compareTo(end) == 0) {
                    startEventTime.setText(hourOfDay + ":" + minute);
                } else {
                    startEventTime.setText(currentDateStartTime);
                    //Log.w("Hello4",currentDateStartTime+":" +hourOfDay + ":" + minute);
                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateStartTime = sdf.format(new Date());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Log.w("text",MainActivity.categoryList.toString());

        plus_minus_textview = (TextView)findViewById(R.id.plus_minus_textview);
        plus_button = (Button)findViewById(R.id.create_plus_button);
        minus_button = (Button)findViewById(R.id.create_minus_button);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(plus_minus_textview.getText().toString());
                plus++;
                plus_minus_textview.setText(String.valueOf(plus));
            }
        });

        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(plus_minus_textview.getText().toString());
                if(plus<=0){
                    plus =0;
                }else {
                plus--;}
                plus_minus_textview.setText(String.valueOf(plus));
            }
        });
        //Data Model
        startEventTime = (TextView)findViewById(R.id.start_time_text_view);
        sdf = new SimpleDateFormat("HH:mm");
        currentDateStartTime = sdf.format(new Date());
        startEventTime.setText(currentDateStartTime);
        startEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(CreateActivity.this, startTimer, c
                        .get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        true).show();
            }

        });
        endEventTime = (TextView)findViewById(R.id.end_time_text_view);
        sdf = new SimpleDateFormat("HH:mm");
        currentDateEndTime = sdf.format(new Date());
        endEventTime.setText(currentDateEndTime);
        endEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateActivity.this, endTimer, c
                        .get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        true).show();
            }
        });


        getSupportActionBar().setTitle("Create a new activity");
        final ArrayList<CategorySpinnerModel> list=new ArrayList<>();
        list.add(new CategorySpinnerModel("Arts & Culture",R.drawable.art_exposition));
        list.add(new CategorySpinnerModel("Nightlife",R.drawable.nightlife));
        list.add(new CategorySpinnerModel("Sports",R.drawable.running));
        list.add(new CategorySpinnerModel("Dating",R.drawable.naked_run));
        list.add(new CategorySpinnerModel("Activities",R.drawable.billard));
        list.add(new CategorySpinnerModel("Outdoors",R.drawable.backpack));
        list.add(new CategorySpinnerModel("Camping",R.drawable.camping));
        list.add(new CategorySpinnerModel("Food and Drink",R.drawable.grab_drink));
        list.add(new CategorySpinnerModel("Networking",R.drawable.coworking));


        Spinner category_spinner = (Spinner)findViewById(R.id.category_picker);
//        category_spinner.setBackgroundColor(Color.WHITE);
        CategorySpinnerActivity adapter=new CategorySpinnerActivity(CreateActivity.this, R.layout.category_picker,R.id.txt,list);
        category_spinner.setAdapter(adapter);

        Spinner activity_type_spinner = (Spinner)findViewById(R.id.activity_type_picker);
        final ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_type_spinner.setAdapter(spinnerAdapter2);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerAdapter2.clear();
                String itemSelected =  list.get(position).getText();
                List categoryArrayList=new ArrayList<Category>();
                categoryArrayList = MainActivity.categoryList;
                Category c = new Category();

                for(int i =0; i<categoryArrayList.size(); i++)
                {
                    c = (Category) categoryArrayList.get(i);
                    if(itemSelected.equals(c.getName())){
                    //Log.w("category list", c.getName());
                    //Log.w("Size",c.getActivityTypeList().size()+"");
                    for(int j=0;j<c.getActivityTypeList().size();j++){
                        String activityType = (String) c.getActivityTypeList().get(j);
                        // Log.w("list list",activityType);
                        spinnerAdapter2.add(activityType);
                    }}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        startTimePicker = (TimePicker)findViewById(R.id.start_time_text_view);
//        endTimePicker = (TimePicker)findViewById(R.id.end_time_text_view);

        //TODO: Dynamically create this lists based on which category was chosen
/*
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
*/

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
                //Get the items selected by the user and put them into an HTTP reques
                Spinner activityTypePicker = (Spinner) findViewById(R.id.activity_type_picker);
                String activityType = activityTypePicker.getSelectedItem().toString();

                // Spinner maxUsersPicker = (Spinner) findViewById(R.id.max_participants_spinner);
                //String maxUsers = maxUsersPicker.getSelectedItem().toString();

                EditText activityEditText = (EditText) findViewById(R.id.editText);
                String activity_name = activityEditText.getText().toString();

                EditText addressText = (EditText) findViewById(R.id.address_edit_text);
                String address = addressText.getText().toString();

                EditText descriptionText = (EditText) findViewById(R.id.description_edit_text);
                String activityDescription = descriptionText.getText().toString();

                EditText additionalNotesText = (EditText) findViewById(R.id.additional_notes_edit_text);
                String additionalNotes = additionalNotesText.getText().toString();

//                String startTime = startTimePicker.getCurrentHour() +":"+startTimePicker.getCurrentMinute();
//                String endTime =  endTimePicker.getCurrentHour() + ":"+endTimePicker.getCurrentMinute();


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
                params.put("activity_type", activityType);
              //  params.put("max_users", maxUsers);
                params.put("activity_lat", Double.toString(ValidationClass.get_Latitude(address,coder)));
                params.put("activity_lon", Double.toString(ValidationClass.get_longitude(address,coder)));
//                params.put("activity_lat", Double.toString(43));
//                params.put("activity_lon", Double.toString(-72));

                params.put("address",address);
                params.put("activity_time", startEventTime.getText());
                params.put("activity_end_time", endEventTime.getText());
                params.put("description", activityDescription);
                params.put("additional_notes",additionalNotes);

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

                CreateActivity.this.finish();
            }
        });
        }
}
