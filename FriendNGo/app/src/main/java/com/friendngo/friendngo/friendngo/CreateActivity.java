package com.friendngo.friendngo.friendngo;


import android.app.TimePickerDialog;
import android.content.Context;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateActivity extends AppCompatActivity {
    boolean validationFlag = false;
    TextView plus_minus_textview;
    int plus = 0;
    Button plus_button,minus_button;
    int todayTomorrowFlag =0;
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
                if(todayTomorrowFlag == 0){
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
                    Toast.makeText(CreateActivity.this, "Selected time is not valid", Toast.LENGTH_LONG).show();
                    currentDateEndTime = sdf.format(new Date());
                }
                }else {
                    endEventTime.setText(hourOfDay + ":" + minute);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    TimePickerDialog.OnTimeSetListener startTimer = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            sdf = new SimpleDateFormat("HH:mm");
            currentDateStartTime = sdf.format(new Date());

            try {
                Date start = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(currentDateStartTime);
                Date end = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(hourOfDay + ":" + minute);
                if(todayTomorrowFlag ==0){
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
                }}else{
                    startEventTime.setText(hourOfDay + ":" + minute);
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



        plus_minus_textview = (TextView)findViewById(R.id.plus_minus_textview);
        plus_button = (Button)findViewById(R.id.create_plus_button);
        minus_button = (Button)findViewById(R.id.create_minus_button);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus = Integer.parseInt(plus_minus_textview.getText().toString());
                plus++;
                plus_minus_textview.setText(String.valueOf(plus));
            }
        });

        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus = Integer.parseInt(plus_minus_textview.getText().toString());
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

        final ArrayList<CategorySpinnerModel> list=new ArrayList<>();
        list.add(new CategorySpinnerModel("Art & Culture",R.drawable.art_exposition));
        list.add(new CategorySpinnerModel("Nightlife",R.drawable.concert));
        list.add(new CategorySpinnerModel("Sports",R.drawable.running));
        list.add(new CategorySpinnerModel("Fun & Crazy",R.drawable.naked_run));
        list.add(new CategorySpinnerModel("Games",R.drawable.billard));
        list.add(new CategorySpinnerModel("Travel & Road-Trip",R.drawable.backpack));
        list.add(new CategorySpinnerModel("Nature & Outdoors",R.drawable.camping));
        list.add(new CategorySpinnerModel("Social Activities",R.drawable.grab_drink));
        list.add(new CategorySpinnerModel("Professional & Networking",R.drawable.coworking));
        list.add(new CategorySpinnerModel("Help & Association", R.drawable.handshake));

        Spinner category_spinner = (Spinner)findViewById(R.id.category_picker);
        final CategorySpinnerActivity adapter=new CategorySpinnerActivity(CreateActivity.this, R.layout.category_picker,R.id.txt,list);
        category_spinner.setAdapter(adapter);

        Spinner activity_type_spinner = (Spinner)findViewById(R.id.activity_type_picker);
        final ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_type_spinner.setAdapter(spinnerAdapter2);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerAdapter2.clear();

                Log.w("hello","hello");
                String itemSelected =  list.get(position).getText();

                List categoryArrayList=new ArrayList<Category>();
                categoryArrayList = MapActivity.categoryList;
                Category c = new Category();

                for(int i =0; i<categoryArrayList.size(); i++)
                {
                    Log.w("list size ",categoryArrayList.size()+"");
                    c = (Category) categoryArrayList.get(i);
                    if(itemSelected.equals(c.getName())){
                    Log.w("category list", c.getName());

                    Log.w("Size",c.getActivityTypeList().size()+"");
                    for(int j=0;j<c.getActivityTypeList().size();j++){
                        String activityType = (String) c.getActivityTypeList().get(j);
                         Log.w("list list",activityType);
                        spinnerAdapter2.add(activityType);
                    }}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM");

        todayButton = (Button)findViewById(R.id.today_button);
        todayButton.setText(dateFormat.format(new Date()));
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayTomorrowFlag = 0;
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
                todayTomorrowFlag = 1;
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
                EditText activityEditText = (EditText) findViewById(R.id.editText);
                String activity_name = activityEditText.getText().toString();

                EditText addressText = (EditText) findViewById(R.id.address_edit_text);
                String address = addressText.getText().toString();

                EditText descriptionText = (EditText) findViewById(R.id.description_edit_text);
                String activityDescription = descriptionText.getText().toString();

                EditText additionalNotesText = (EditText) findViewById(R.id.additional_notes_edit_text);
                String additionalNotes = additionalNotesText.getText().toString();

                //Determine the latitude and longitude from the address provided
                Geocoder coder = new Geocoder(getApplicationContext());

                //POST new activity to the server
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }

                //Set the status type into the message for the server
                RequestParams params = new RequestParams();
                if(activity_name.equals("")){
                    validationFlag = false;
                }else {
                    validationFlag = true;
                    params.put("activity_name", activity_name);
                }params.put("activity_type", activityType);
                params.put("max_users",plus);

                if(address.equals("")){
                    validationFlag =false;
                }else {
                    validationFlag =true;
                    params.put("activity_lat", Double.toString(ValidationClass.get_Latitude(address,coder)));
                    params.put("activity_lon", Double.toString(ValidationClass.get_longitude(address,coder)));
                    params.put("address", address);
                }
                params.put("activity_time", startEventTime.getText());
                params.put("activity_end_time", endEventTime.getText());
                params.put("description", activityDescription);

                params.put("additional_notes",additionalNotes);
                if(validationFlag) {
                    client.post(MainActivity.base_host_url + "api/postActivity/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST ACT SUCCESS", statusCode + ": " + "Response = " + response.toString());
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
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST ACT FAIL", "Error Code: " + error_code + "Text: " + text);
                        }
                    });
                    CreateActivity.this.finish();
                }else {
                    Toast.makeText(CreateActivity.this,"Activity name or address field is empty",Toast.LENGTH_LONG).show();
                }

            }
        });
        }
}
