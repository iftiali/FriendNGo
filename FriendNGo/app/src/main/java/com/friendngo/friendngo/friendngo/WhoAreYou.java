package com.friendngo.friendngo.friendngo;

<<<<<<< HEAD
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
=======
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
>>>>>>> 0a071deecc80a2710a80edf67bbc527cf8048dcf
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.ActivityCompat;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Manifest;

import cz.msebera.android.httpclient.Header;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

<<<<<<< HEAD
    private static final int CAMERA_REQUEST = 1888;

=======
public class WhoAreYou extends AppCompatActivity {
    CircularImageView circularImageView;
>>>>>>> 0a071deecc80a2710a80edf67bbc527cf8048dcf
    Button continueButton;
    EditText nameInput;
    EditText nationalityInput;
    EditText languageInput;
    EditText ageInput;

    ImageView profilePicture;
    String pictureURL ="";
    File directory;
    File downloadedImage;
    File myFile;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Who Are you?");
        profilePicture = (ImageView) findViewById(R.id.profilepicture);
//        ageInput = (EditText) findViewById(R.id.age_editText);
//        languageInput = (EditText) findViewById(R.id.language_editText);
//        nationalityInput = (EditText) findViewById(R.id.language_editText);
//        nameInput = (EditText) findViewById(R.id.name_input_editView);


        //Set OnClick Listener for the profile picture pressed
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set OnClick Listener for the profile picture button
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        //SETUP GET user profile
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }
        //GET last known location
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                AsyncHttpClient client = new AsyncHttpClient();

                try{
                    pictureURL = response.getString("picture");
                }catch (JSONException e){
                    Log.w("GET PROFILE JSON FAIL",e.getMessage().toString());
                }

                client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Log.w("GET IMAGE FAIL","Could not retrieve image");
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File response) {
                        Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
                        //Use the downloaded image as the profile picture
                        Uri uri = Uri.fromFile(response);
                        profilePicture.setImageURI(uri);
                        downloadedImage = response;
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.w("PROFILE SUCCESS ARRAY", statusCode + ": " + timeline.toString());
//                try {
//                    JSONObject firstEvent = timeline.getJSONObject(0);
//                    String token = firstEvent.getString("token");
//                    Log.w("GET LASTLOC SUCCESS2", token.toString());
//
//
//                } catch (JSONException e) {
//                    Log.w("GET LASTLOC FAIL1: ", e.getMessage().toString());
//                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                Log.w("GET PROFILE FAIL", "Error Code: " + error_code + ",  " + text);
            }
        });

       // isStoragePermissionGranted();
        setContentView(R.layout.activity_who_are_you);

         get_UserInfo();
        //TODO: Make a GET request to get the stored values for the user's profile

        circularImageView = (CircularImageView)findViewById(R.id.profilepicture);
        continueButton = (Button)findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
<<<<<<< HEAD
              @Override
              public void onClick(View view) {

                  //SETUP POST to profile
                  AsyncHttpClient client = new AsyncHttpClient();
                  if(SignIn.static_token != null) {
                      client.addHeader("Authorization","Token "+SignIn.static_token);
                  }

                  ageInput = (EditText) findViewById(R.id.age_editText);
                  languageInput = (EditText) findViewById(R.id.language_editText);
                  nationalityInput = (EditText) findViewById(R.id.language_editText);
                  nameInput = (EditText) findViewById(R.id.name_input_editView);

                  RequestParams params = new RequestParams();
                  //Adding text params
                  if(MainActivity.cheat_mode==true){

                      params.put("first_name","Seahorse");
                      params.put("last_name","Tootles");
                      params.put("phone","444-444-4444");
                      params.put("age","99");
                      params.put("home_city","toronto");
                      params.put("home_nationality","Canadian");
                  }else{
//                      params.put("first_name",nameInput.getText());
//                      params.put("last_name",nameInput.getText());
//                      params.put("phone","444-444-4444");
//                      params.put("age",ageInput.getText());
//                      params.put("home_city","toronto");
//                      params.put("home_nationality",nationalityInput.getText());
                  }

                  //Adding image params
                 File myFile = new File(directory + "/picture.jpg");
                  try {
                      params.put("picture", myFile);
//                      params.put("picture", downloadedImage);
                  } catch(FileNotFoundException e) {}

                  //POST Update to profile
                  client.post(MainActivity.base_host_url + "api/postProfile/", params, new JsonHttpResponseHandler() {

                      @Override
                      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                          Log.w("POST PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());

//                          String pictureURL = "";
//                          try{
//                              pictureURL = MainActivity.base_host_url + response.getString("picture");
//
//                          }catch (JSONException e){
//                              Log.w("GET PROFILE JSON FAIL",e.getMessage().toString());
//                          }
                      }

                      @Override
                      public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                          Log.w("POST PROFILE ARRAY", statusCode + ": " + timeline.toString());
                      }

                      @Override
                      public void onRetry(int retryNo) {
                          // called when request is retried
                          Log.w("POST PROFILE RETRY", "" + retryNo);
                      }

                      @Override
                      public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                          Log.w("POST PROFILE FAIL", "Headers: " + headers + ", Error Code: " + error_code + ",  " + text);
                      }
                  });
                  //Close the Activity and Return to the map when finished
                  WhoAreYou.this.finish();
              }
          }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            profilePicture.setImageBitmap(photo);

            //Preprocess Image for Uploading
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            myFile=new File(directory,"picture.jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myFile);
                // Use the compress method on the BitMap object to write image to the OutputStream
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
=======
                                              @Override
                                              public void onClick(View view) {
                                                  //TODO: Make HTTP POST call to update the user's profile
                                                  WhoAreYou.this.finish();
                                              }
                                          }
        );
    }
    private void get_UserInfo() {
        //GET the user info
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }else{
            Log.w("TOKEN ERROR","What happened to the token :(");
        }
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("User info", statusCode + ": " + "Response = " + response.toString());
                try {
                    Log.w("User info", response.getString("lastCity"));
                } catch (JSONException e) {
                    Log.w("User info ", e.getMessage().toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                Log.w("User info", statusCode + "- JSON ARRAY: " + responseArray.toString());

//asdfas
                //Cycle through the list of activities
                /*for (int i=0; i<responseArray.length(); i++){
                    try {
                        JSONObject activity = responseArray.getJSONObject(i);
                        String name = activity.getString("activity_name");
                        String creator = activity.getString("creator");
                        int maxUsers = activity.getInt("max_users");
                        String activityTimeString = activity.getString("activity_time");
                        Log.w("parth",activityTimeString);
                        SimpleDateFormat activityTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        Date activityTime = new Date();
                        //TODO: Improve timzezones for multi-city support
                        try {

                            activityTime = activityTimeFormat.parse(activityTimeString);
                        }catch (ParseException p){
                            Log.w("PARSE EXCEPTION","Something went wrong with DATE parsing");
                        }
                        String activityType = activity.getString("activity_type");
                        double latitude = activity.getDouble("activity_lat");
                        double longitude = activity.getDouble("activity_lon");

                        UserActivity userActivity = new UserActivity(name,
                                creator,
                                maxUsers,
                                activityTime,
                                "Business",
                                activityType,
                                latitude,
                                longitude );

                        activitiesList.add(userActivity);

                        int height = 75;
                        int width = 75;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.canada_icon);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude,longitude))
                                .title(name)
                                .snippet(activityType)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        markerMap.put(name,i);
                        mMap.addMarker(marker);


                    } catch (JSONException e){
                        Log.w("JSON EXCEPTION:", "Error parsing the getActivities response");
                    }

                }

                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            */}

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("User info", "TRYING AGAIN");
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("User info", "Error Code: " + error_code);
                Log.w("User info",text);
            }
        });
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("tag", "Permission is granted");
                return true;
            } else {

                Log.v("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("tag", "Permission is granted");
            return true;
        }
    }

>>>>>>> 0a071deecc80a2710a80edf67bbc527cf8048dcf
}
