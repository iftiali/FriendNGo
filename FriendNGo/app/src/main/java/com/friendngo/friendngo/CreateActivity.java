package com.friendngo.friendngo;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    boolean validationFlag = false;
    String autocomplateAddress = null;
    private static final String TAG ="Auther:Parth";
    TextView plus_minus_textview,cancelTextView;
    int plus = 0;
    String itemSelectedInCategory = null;
    Calendar tomorrowDate;
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
    protected GoogleApiClient mGoogleApiClient;
    Date start;
    Date end;
    Calendar starttimeCalender,endtimecalendar;
    String startparseDateForDatabase = null;
    String endparseDateForDatabase = null;
    double placeLong = 0;
    double placelant = 0;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    Geocoder coder =null;
    TimePickerDialog.OnTimeSetListener endTimer = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            sdf = new SimpleDateFormat("HH:mm");
            currentDateEndTime = sdf.format(new Date());

            try {
                 start = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(currentDateEndTime);
                 end = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(hourOfDay + ":" + minute);
                //validation for end time
               /* if(todayTomorrowFlag == 0){
                if (start.compareTo(end) > 0) {
                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateEndTime = sdf.format(new Date());
                    endEventTime.setText(currentDateEndTime);
                } else if (start.compareTo(end) < 0) {


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
                }*/
                endEventTime.setText(hourOfDay + ":" + minute);
                endtimecalendar = Calendar.getInstance();
                endtimecalendar.add(Calendar.HOUR_OF_DAY, hourOfDay);
                endtimecalendar.add(Calendar.MINUTE,minute);

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
                start = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(currentDateStartTime);
                end = new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        .parse(hourOfDay + ":" + minute);
                if(todayTomorrowFlag ==0){
                if (start.compareTo(end) > 0) {

                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateStartTime = sdf.format(new Date());

                    startEventTime.setText(currentDateStartTime);
                } else if (start.compareTo(end) < 0) {

                    startEventTime.setText(hourOfDay + ":" + minute);
                } else if (start.compareTo(end) == 0) {
                    startEventTime.setText(hourOfDay + ":" + minute);
                } else {
                    startEventTime.setText(currentDateStartTime);

                    Toast.makeText(CreateActivity.this,"Selected time is not valid",Toast.LENGTH_LONG).show();
                    currentDateStartTime = sdf.format(new Date());
                }}else{

                    startEventTime.setText(hourOfDay + ":" + minute);

                }
                starttimeCalender = Calendar.getInstance();
                starttimeCalender.add(Calendar.HOUR_OF_DAY, hourOfDay);
                starttimeCalender.add(Calendar.MINUTE,minute);


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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 ,CreateActivity.this)
                .addApi(Places.GEO_DATA_API)
                .build();
        setContentView(R.layout.activity_create);

        cancelTextView = (TextView)findViewById(R.id.cancel__text_view);
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.address_edit_text);
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
        cancelTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(CreateActivity.this, MapActivity.class);
                CreateActivity.this.startActivity(intent);
                CreateActivity.this.finish();
                return false;
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

        final Spinner category_spinner = (Spinner)findViewById(R.id.category_picker);
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


                itemSelectedInCategory =  list.get(position).getText();

                List categoryArrayList=new ArrayList<Category>();
                categoryArrayList = MapActivity.categoryList;
                Category c = new Category();

                for(int i =0; i<categoryArrayList.size(); i++)
                {

                    c = (Category) categoryArrayList.get(i);
                    if(itemSelectedInCategory.equals(c.getName())){



                    for(int j=0;j<c.getActivityTypeList().size();j++){
                        String activityType = (String) c.getActivityTypeList().get(j);

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
            tomorrowDate = Calendar.getInstance();
            tomorrowDate.setTime(new Date()); // Now use today date.
            tomorrowDate.add(Calendar.DATE, 1);
            String output = dateFormat.format(tomorrowDate.getTime());

            Log.w("Date",output);
            tomorrowButton.setText(output);


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


                //EditText addressText = (EditText) findViewById(R.id.address_edit_text);
                autocomplateAddress = mAutocompleteView.getText().toString();

                EditText descriptionText = (EditText) findViewById(R.id.description_edit_text);
                String activityDescription = descriptionText.getText().toString();

                EditText additionalNotesText = (EditText) findViewById(R.id.additional_notes_edit_text);
                String additionalNotes = additionalNotesText.getText().toString();

                //Determine the latitude and longitude from the address provided


                //POST new activity to the server
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }

                //Set the status type into the message for the server
                RequestParams params = new RequestParams();
                if(activity_name.equals("") || autocomplateAddress.equals("") || placeLong == 0 || placelant == 0) {
                    Toast.makeText(CreateActivity.this,"Activity name or address field is invalid/empty ",Toast.LENGTH_LONG).show();
                }else{

                    params.put("category",itemSelectedInCategory);
                    params.put("activity_name", activity_name);

                    params.put("activity_type", activityType);
                    params.put("max_users",plus);


                        params.put("activity_lat", placelant);
                        params.put("activity_lon", placeLong);
                        params.put("address", autocomplateAddress);

                    // SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");2017-03-14'T'11:24:00'Z'
                    String[] monthNames = new String[12];
                    monthNames[0] ="01";
                    monthNames[1] ="02";
                    monthNames[2] ="03";
                    monthNames[3] ="04";
                    monthNames[4] ="05";
                    monthNames[5] ="06";
                    monthNames[6] ="07";
                    monthNames[7] ="08";
                    monthNames[8] ="09";
                    monthNames[9] ="10";
                    monthNames[10] ="11";
                    monthNames[11] ="12";


                    if(todayTomorrowFlag == 1) {

                        tomorrowDate.add(Calendar.DATE, 1);
                        //  Log.w("Start Event", tomorrowDate.get(Calendar.YEAR)+"-"+monthNames[tomorrowDate.get(Calendar.MONTH)]+"-"+tomorrowDate.get(Calendar.DATE)+"'T'"+endEventTime.getText().toString()+":00'Z'");
                        endparseDateForDatabase = tomorrowDate.get(Calendar.YEAR) + "-" + monthNames[tomorrowDate.get(Calendar.MONTH)] + "-" + tomorrowDate.get(Calendar.DATE) + "T" + endEventTime.getText().toString() + ValidationClass.getCurrentTimezoneOffset();;
                    }else{
                        endparseDateForDatabase = tomorrowDate.get(Calendar.YEAR) + "-" + monthNames[tomorrowDate.get(Calendar.MONTH)] + "-" + tomorrowDate.get(Calendar.DATE) + "T" + endEventTime.getText().toString() + ValidationClass.getCurrentTimezoneOffset();;
                    }

                    if(todayTomorrowFlag == 0) {
                        tomorrowDate.add(Calendar.DATE, -1);
                        //Log.w("Start Event", tomorrowDate.get(Calendar.YEAR)+"-"+monthNames[tomorrowDate.get(Calendar.MONTH)]+"-"+tomorrowDate.get(Calendar.DATE)+"'T'"+startEventTime.getText().toString()+":00'Z'");
                        startparseDateForDatabase = tomorrowDate.get(Calendar.YEAR) + "-" + monthNames[tomorrowDate.get(Calendar.MONTH)] + "-" + tomorrowDate.get(Calendar.DATE) + "T" + startEventTime.getText().toString() + ValidationClass.getCurrentTimezoneOffset();;
                    }else{

                        startparseDateForDatabase = tomorrowDate.get(Calendar.YEAR) + "-" + monthNames[tomorrowDate.get(Calendar.MONTH)] + "-" + tomorrowDate.get(Calendar.DATE) + "T" + startEventTime.getText().toString() + ValidationClass.getCurrentTimezoneOffset();;
                    }


                    params.put("activity_time",startparseDateForDatabase);
                    params.put("activity_end_time",endparseDateForDatabase);
                    params.put("description", activityDescription);
                    params.put("additional_notes",additionalNotes);


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

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                            Log.w("POST ACT FAIL", "Error Code: " + error_code + "JSON: " + json.toString());
                        }
                    });
                    CreateActivity.this.finish();
                }

            }
        });
        //Address Autocomplete
        coder = new Geocoder(getApplicationContext());
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,null,
                null);
        mAutocompleteView.setAdapter(mAdapter);
//        mAutocompleteView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!mAutocompleteView.getText().toString().equals("")){
//                    try{
//                        validationFlag = false;
//                    ValidationClass.get_Latitude(autocomplateAddress, coder);
//                    ValidationClass.get_longitude(autocomplateAddress, coder);
//                   // Toast.makeText(getApplicationContext(),"Focus off",Toast.LENGTH_SHORT).show();
//                    }catch (Exception e){
//                        validationFlag = true;
//
//                        Toast.makeText(getApplicationContext(),"Invalid address ",Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//            }
//        });
        }
    /**
            * Listener that handles selections from suggestions from the AutoCompleteTextView that
    * displays Place suggestions.
    * Gets the place id of the selected item and issues a request to the Places Geo Data API
    * to retrieve more details about the place.
    *
            * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
                                                                                  * String...)
    */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            // Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final com.google.android.gms.location.places.Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
             autocomplateAddress = (String) place.getAddress();

            // Display the third party attributions if set.
           /* final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }
            */
            placelant = place.getLatLng().latitude;
            placeLong = place.getLatLng().longitude;
            //Log.i(TAG, "Place details received: " + place.getName());
            //Log.i(TAG,"Place details lan and log"+place.getLatLng().latitude+"log"+place.getLatLng().longitude);
            places.release();
        }
    };
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


}
