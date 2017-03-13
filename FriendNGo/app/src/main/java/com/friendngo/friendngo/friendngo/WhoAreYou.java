package com.friendngo.friendngo.friendngo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.Manifest;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.ActivityCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import cz.msebera.android.httpclient.Header;
import com.mikhaellopez.circularimageview.CircularImageView;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class WhoAreYou extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    CircularImageView circularImageView;
    Button continueButton;
    EditText nameInput;
    Spinner nationalityInputSpinner;

    EditText ageInput;
    ImageView profilePicture;
    String pictureURL ="";
    File directory;
    File downloadedImage;
    File myFile;

    Bitmap photo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);

        profilePicture = (ImageView) findViewById(R.id.messages_profile_picture);

        if(MainActivity.cheat_mode==true){
            WhoAreYou.this.finish();
        }
        nationalityInputSpinner = (Spinner) findViewById(R.id.citizen_spinner);
        nameInput = (EditText) findViewById(R.id.name_input_editView);
        ageInput = (EditText) findViewById(R.id.age_editText);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> languageList = new ArrayList<String>();

        for (Locale locale : locales) {
            String country = locale.getDisplayLanguage();
            if (country.trim().length()>0 && !languageList.contains(country)) {
                languageList.add(country);
            }
        }
        Collections.sort(languageList);
        MultiSelectSpinner multiSelectSpinner1 = (MultiSelectSpinner) findViewById(R.id.language_spninner);
        multiSelectSpinner1.setItems(languageList)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                    }
                })
                .setAllCheckedText("All types")
                .setAllUncheckedText("Spoken laguages")
                .setSelectAll(false)

        ;


        ArrayList<String> countriesList = new ArrayList<String>();

        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countriesList.contains(country)) {
                countriesList.add(country);
            }
        }

        Collections.sort(countriesList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WhoAreYou.this,android.R.layout.simple_spinner_item, countriesList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalityInputSpinner.setAdapter(adapter);
        nationalityInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.w("name",selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

                try{
                    nameInput.setHint(response.getString("first_name"));
                    ageInput.setHint(response.getString("age"));
                    MapActivity.myProfileNameEdit.setHint(response.getString("first_name"));
                    MapActivity.myProfileAgeEdit.setHint(response.getString("age"));
                }catch (JSONException e){
                    Log.w("JSON EXCEPTION", e.getMessage());
                }

                //GET The image file at the pictureURL
                AsyncHttpClient client = new AsyncHttpClient();
                try{
                    pictureURL = response.getString("picture");
                }catch (JSONException e){
                    Log.w("GET PROFILE JSON FAIL",e.getMessage().toString());
                }
                client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File response) {
                        Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
                        //Use the downloaded image as the profile picture
                        Uri uri = Uri.fromFile(response);
                        profilePicture.setImageURI(uri);
                        MapActivity.myProfilePicture.setImageURI(uri);
                        downloadedImage = response;


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Log.w("GET IMAGE FAIL","Could not retrieve image");
                    }
                });
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
                Log.w("GET PROFILE FAIL", "Error Code: " + error_code + ",  " + text);
            }
        });

        get_UserInfo();
        circularImageView = (CircularImageView)findViewById(R.id.messages_profile_picture);
        continueButton = (Button)findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view) {

                  //SETUP POST to profile
                  AsyncHttpClient client = new AsyncHttpClient();
                  if(SignIn.static_token != null) {
                      client.addHeader("Authorization","Token "+SignIn.static_token);
                  }

                  ageInput = (EditText) findViewById(R.id.age_editText);

                  RequestParams params = new RequestParams();
                  //Adding text params
                  if(MainActivity.cheat_mode==true){
                      params.put("first_name","Mr. Delicious");
                      params.put("last_name","Tootles");
                      params.put("phone","444-444-4444");
                      params.put("age","99");
                      params.put("home_city","toronto");
                      params.put("home_nationality","Canadian");
                  }else{

                      String first_name = nameInput.getText().toString();
                      if(first_name!=null) {
                          params.put("first_name", first_name);
                      }

                      String last_name = nameInput.getText().toString();
                      if(last_name!=null) {
                          params.put("last_name",last_name );
                      }

                      params.put("phone","444-444-4444");
                      String age = ageInput.getText().toString();
                      if(age!=null || age==""){
                          params.put("age",age);
                      }

                      //TODO: This is temporary... remove this eventually
                      params.put("home_city","toronto");
                      params.put("home_nationality","Canadian");
                     // params.put("home_nationality",nationalityInput.getText());
                  }

                  //Adding image params
                 File myFile = new File(directory + "/picture.jpg");

//                  //Use to test if file is being saved / loaded properly
//                  if(myFile.exists()){
//                      Bitmap myBitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());
//                      profilePicture.setImageBitmap(myBitmap);
//                  }else {
//                      Log.w("LOAD IMAGE FILE ERROR","Cannot load image :(");
//                  }

                  try {
                      params.put("picture", myFile);
//                      params.put("picture", downloadedImage);
                  } catch(FileNotFoundException e) {}

                  //TODO: This form needs proper validation
                  //POST Update to profile
                  client.post(MainActivity.base_host_url + "api/postProfile/", params, new JsonHttpResponseHandler() {

                      @Override
                      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                          Log.w("POST PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
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
                  WhoAreYou.this.finish();
              }
          }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            profilePicture.setImageBitmap(photo);
            MapActivity.myProfilePicture.setImageBitmap(photo);

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
            }

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
}
