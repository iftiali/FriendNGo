package com.friendngo.friendngo.friendngo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

import com.mikhaellopez.circularimageview.CircularImageView;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class WhoAreYou extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    CircularImageView circularImageView;
    Button continueButton;
    EditText nameInput;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    EditText ageInput;
    String pictureURL = "";
    File directory;
    File downloadedImage;
    private String userChoosenTask;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.cheat_mode == true) {
            WhoAreYou.this.finish();
        }

     // nameInput = (EditText) findViewById(R.id.name_input_editView);
       // ageInput = (EditText) findViewById(R.id.age_editText);
        circularImageView = (CircularImageView) findViewById(R.id.profilepicture);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> languageList = new ArrayList<String>();
        for (Locale locale : locales) {
            String lang = locale.getDisplayLanguage();

            if (lang.trim().length() > 0 && !languageList.contains(lang)) {
                if (lang.equals("English") || lang.equals("French") || lang.equals("Spanish")) {

                } else {
                    languageList.add(lang);
                }
            }
        }
        Collections.sort(languageList);
        languageList.add(0, "English");
        languageList.add(1, "French");
        languageList.add(2, "Spanish");
        MultiSelectSpinner multiSelectSpinnerLanguage = (MultiSelectSpinner) findViewById(R.id.language_spinner);
        multiSelectSpinnerLanguage.setItems(languageList)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setSpinnerItemLayout(R.layout.custom_spinner_item)
                .setAllCheckedText("All types")
                .setAllUncheckedText("Spoken languages")
                .setSelectAll(false)
        ;
        multiSelectSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                // Log.w("TRYTRY",item);
                if (item.equals("Spoken languages"))
                    ((TextView) view).setTextColor(Color.GRAY);

                else
                    ((TextView) view).setTextColor(Color.BLACK);
                //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> countriesList = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countriesList.contains(country)) {
                countriesList.add(country);
            }
        }

        Collections.sort(countriesList);

        Spinner nationalityInputSpinner = (Spinner) findViewById(R.id.citizen_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, countriesList);

        nationalityInputSpinner.setAdapter(spinnerArrayAdapter);
        nationalityInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    ((TextView) view).setText("Citizen");
                    ((TextView) view).setTextColor(Color.GRAY);

                } else
                    ((TextView) view).setTextColor(Color.BLACK);
                //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //Set OnClick Listener for the profile picture pressed
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set OnClick Listener for the profile picture button
                selectImage();
            }
        });

        //SETUP GET user profile
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }

        //GET user profile
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());

                try {
                    nameInput.setText(response.getString("first_name"));
                    ageInput.setText(response.getString("age"));
                } catch (JSONException e) {
                    Log.w("JSON EXCEPTION", e.getMessage());
                }

                //GET The image file at the pictureURL
                AsyncHttpClient client = new AsyncHttpClient();
                try {
                    pictureURL = response.getString("picture");
                } catch (JSONException e) {
                    Log.w("GET PROFILE JSON FAIL", e.getMessage().toString());
                }
                client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File response) {
                        Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                        //Use the downloaded image as the profile picture
                        Uri uri = Uri.fromFile(response);
                        downloadedImage = response;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Log.w("GET IMAGE FAIL", "Could not retrieve image");
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
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET PROFILE FAIL", "Error Code: " + error_code + ",  " + text);
            }
        });

        get_UserInfo();

        continueButton = (Button) findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View view) {

                                                  //SETUP POST to profile
                                                  AsyncHttpClient client = new AsyncHttpClient();
                                                  if (SignIn.static_token != null) {
                                                      client.addHeader("Authorization", "Token " + SignIn.static_token);
                                                  }

//                                                  ageInput = (EditText) findViewById(R.id.age_editText);

                                                  RequestParams params = new RequestParams();
                                                  //Adding text params
                                                  if (MainActivity.cheat_mode == true) {
                                                      params.put("first_name", "Mr. Delicious");
                                                      params.put("last_name", "Tootles");
                                                      params.put("phone", "444-444-4444");
                                                      params.put("age", "99");
                                                      params.put("home_city", "toronto");
                                                      params.put("home_nationality", "Canadian");
                                                  } else {

                                                      String first_name = nameInput.getText().toString();
                                                      if (first_name != null) {
                                                          params.put("first_name", first_name);
                                                      }

                                                      String last_name = nameInput.getText().toString();
                                                      if (last_name != null) {
                                                          params.put("last_name", last_name);
                                                      }

                                                      params.put("phone", "444-444-4444");
                                                      String age = ageInput.getText().toString();
                                                      if (age != null || age == "") {
                                                          params.put("age", age);
                                                      }

                                                      //TODO: This is temporary... remove this eventually
                                                      params.put("home_city", "toronto");
                                                      params.put("home_nationality", "Canadian");

                                                      params.put("languages", "{\"0\":\"English\" , \"1\":\"French\"}");

                                                  }

                                                  //Adding image params
                                                  File myFile = new File(directory + "/picture.jpg");


                                                  try {
                                                      params.put("picture", myFile);

                                                  } catch (FileNotFoundException e) {
                                                  }

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
                                                      public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                                          Log.w("POST PROFILE FAIL", "Headers: " + headers + ", Error Code: " + error_code + ",  " + text);
                                                      }
                                                  });
                                                  Intent intent = new Intent(WhoAreYou.this, MyCity.class);
                                                  WhoAreYou.this.startActivity(intent);
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
        } else {
            Log.w("TOKEN ERROR", "What happened to the token :(");
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
                Log.w("User info", text);
            }
        });
    }

    /*public  boolean isStoragePermissionGranted() {
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
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImagePhotoPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(WhoAreYou.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ImagePhotoPermission.checkPermission(WhoAreYou.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //thumbnail.compress(Bitmap.CompressFormat.JPEG, 180, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");


        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap rotated = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(),
                matrix, true);
        circularImageView.setImageBitmap(rotated);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap rotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                matrix, true);
        circularImageView.setImageBitmap(rotated);
    }




}
