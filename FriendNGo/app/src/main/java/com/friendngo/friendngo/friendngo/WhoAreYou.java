package com.friendngo.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class WhoAreYou extends AppCompatActivity {

    Button continueButton;
    private static final int CAMERA_REQUEST = 1888;
    ImageView profilePicture;
    EditText first_name;
    EditText last_name;
    EditText citizenship;
    EditText mother_tongue;
    EditText second_language;
    String pictureURL ="";
    File directory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);
        getSupportActionBar().setTitle("Who Are you?");
        profilePicture = (ImageView) findViewById(R.id.profilepicture);
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
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File response) {
                        //Set the profile image as the received image file
                        Uri uri = Uri.fromFile(response);
                        profilePicture.setImageURI(uri);
                    }
                });

                if (Build.VERSION.SDK_INT >= 23) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
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


        continueButton = (Button)findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //After saving... Use Async Library's Post Images
                  File myFile = new File(directory + "/profile.jpg");
                  RequestParams params = new RequestParams();
//                  params.put("first_name","Testing");
//                  params.put("last_name","Lasting");
//                  params.put("phone","444-444-4444");
//                  params.put("age","27");
//                  params.put("home_city","toronto");
//                  params.put("home_nationality","canadian");

                  try {
                      params.put("picture", myFile);
                  } catch(FileNotFoundException e) {}


                  AsyncHttpClient client = new AsyncHttpClient();
                  if(SignIn.static_token != null) {
                      client.addHeader("Authorization","Token "+SignIn.static_token);
                  }
                  //GET last known location

                  client.post(MainActivity.base_host_url + "api/postProfile", params, new JsonHttpResponseHandler() {
//                  client.post( "http://requestb.in/y84bv2y8", params, new JsonHttpResponseHandler() {

                      @Override
                      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                          Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());

                          String pictureURL = "";
                          try{
                              pictureURL = MainActivity.base_host_url + response.getString("picture");

                          }catch (JSONException e){
                              Log.w("GET PROFILE JSON FAIL",e.getMessage().toString());
                          }
                      }

                      @Override
                      public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                          Log.w("PROFILE SUCCESS ARRAY", statusCode + ": " + timeline.toString());
                      }

                      @Override
                      public void onRetry(int retryNo) {
                          // called when request is retried
                      }

                      @Override
                      public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                          Log.w("GET PROFILE FAIL", "Headers: " + headers + ", Error Code: " + error_code + ",  " + text);
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
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,"profile.jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
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

}
