package com.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class FacebookLogin extends AppCompatActivity {
    public static double clat = 0, clon = 0;
    CallbackManager callbackManager;
    private Button useEmailButton;
    GPSTracker gps;
    String emailToken = "";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private String TOKEN_PREFERENCE = "token_preference";

    public static Uri facebook_profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creates fullscreen effect for older phones
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        getLocationPermission();
        setContentView(R.layout.activity_facebook_login);


        //Creates fullscreen effect for newer phones
        if (Build.VERSION.SDK_INT >= 16) {

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            getSupportActionBar().hide();
        }

        //TODO: Why does white bar appear at the top when we logout and come back to this screen??? Fix it!
        if (MainActivity.cheat_mode == true) {
            Intent mainIntent = new Intent(FacebookLogin.this, SignIn.class);
            FacebookLogin.this.startActivity(mainIntent);
        }
        //Email token check
        SharedPreferences pref = getSharedPreferences("EmailToken", MODE_PRIVATE); // 0 - for private mode
        emailToken= pref.getString("email_token", null);

        if(emailToken == null)
        {

            Log.d("First time","First time");
        }else {

            MainActivity.new_user = false;
            SignIn.static_token = emailToken;
            Intent mainIntent = new Intent(FacebookLogin.this, MapActivity.class);
            FacebookLogin.this.startActivity(mainIntent);
            FacebookLogin.this.finish();
        }

        //If the user is logged in then go straight to the New City Activity
        if (isLoggedIn()) {

            MainActivity.new_user = false;
            SharedPreferences prefs = getSharedPreferences(TOKEN_PREFERENCE, MODE_PRIVATE);
            String shared_preference_token = prefs.getString("token", null);
            if (shared_preference_token != null) {
                SignIn.static_token = shared_preference_token;
            }

                Intent mainIntent = new Intent(FacebookLogin.this, MapActivity.class);
                FacebookLogin.this.startActivity(mainIntent);
                FacebookLogin.this.finish(); //Scott: Was there a readon this was commented out???

        } else {
            //User needs to login

            //Handler for the button to go to e-mail login
            useEmailButton = (Button) findViewById(R.id.use_email_button);
            useEmailButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                        Intent mainIntent = new Intent(FacebookLogin.this, SignIn.class);
                        FacebookLogin.this.startActivity(mainIntent);

                }
            });

            //Sets up the callback to the Facebook API for the facebook button
            callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

            // Set the permissions that the user should have with the login
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    /* Create an Intent that will start the Menu-Activity. */
                    Log.w("FACEBOOK LOGIN", "Success! Token: " + loginResult.getAccessToken().getToken());
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    MainActivity.new_user = true;
                    params.put("access_token", loginResult.getAccessToken().getToken());
                    client.post(MainActivity.base_host_url + "api/exchange_token/facebook/", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("SOCIAL SIGN UP SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            try {

                                SignIn.static_token = response.get("token").toString();
                                Log.w("TOKEN SUCCESS2: ", SignIn.static_token);
                                //TODO: pasre is_created or is_new key
                                SharedPreferences.Editor editor = getSharedPreferences(TOKEN_PREFERENCE, MODE_PRIVATE).edit();
                                editor.putString("token", response.get("token").toString());
                                editor.commit();

                                Bundle params2 = new Bundle();
                                params2.putString("fields","id,email,picture.type(large),birthday,hometown");
                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(), "me",
                                        params2,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                            /* handle the result */
                                                if(response!=null){
                                                    try{
                                                        SignIn.static_profile_image_url = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");

                                                        AsyncHttpClient client = new AsyncHttpClient();
                                                        client.get(SignIn.static_profile_image_url, new FileAsyncHttpResponseHandler(getApplicationContext()) {

                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, File response) {
                                                                Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                                                                //Use the downloaded image as the profile picture
                                                                facebook_profile_pic = Uri.fromFile(response);

//                                                                try {
//                                                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(FacebookLogin.this.getContentResolver(), facebook_profile_pic);

                                                                    //POST the download picture to django!
                                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                                    if (SignIn.static_token != null) {
                                                                        client.addHeader("Authorization", "Token " + SignIn.static_token);
                                                                    }

                                                                    File file = new File(facebook_profile_pic.getPath());
                                                                    //Log.w("PICTURE PATH",myFile.toString());
                                                                    RequestParams paramsProfilePicture = new RequestParams();
                                                                    // paramsProfilePicture.(true);
                                                                    try {
                                                                        paramsProfilePicture.put("picture", file);

                                                                    } catch (FileNotFoundException e) {
                                                                    }
                                                                    client.post(MainActivity.base_host_url + "api/uploadProfilePicture/", paramsProfilePicture, new JsonHttpResponseHandler() {
                                                                        // client.post("http://requestb.in/zzmhq6zz", params, new JsonHttpResponseHandler() {

                                                                        @Override
                                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                            Log.w("POST PROFILE PICTURE", statusCode + ": " + "Response = " + response.toString());
                                                                        }

                                                                        @Override
                                                                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                                                                            Log.w("POST PROFILE PICTURE2", statusCode + ": " + timeline.toString());
                                                                            //  NewWhoAreYouActivity.this.finish();
                                                                        }

                                                                        @Override
                                                                        public void onRetry(int retryNo) {
                                                                            // called when request is retried
                                                                            Log.w("POST PROFILE PICTURE", "" + retryNo);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                                                            Log.w("POST PROFILE PICTURE", "Error Code: " + error_code + "," + text);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                                                                            Log.w("MY PROFILE PICTURE", "Error Code: " + error_code + ",  " + json.toString());
                                                                        }
                                                                    });
                                                            }
                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                                                Log.w("GET IMAGE FAIL", "Could not retrieve image");
                                                            }
                                                        });
                                                        //TODO: Post the url or the image to django somewhere...
                                                    } catch (JSONException e){
                                                        Log.w("JSON PARSE ERROR", e.toString());
                                                    }
                                                    Log.w("FACEBOOK GRAPH RESPONSE",response.toString());
                                                } else {
                                                    Log.w("FACEBOOK GRAPH ERROR", "TRY AGAIN!!!");
                                                }
                                            }
                                        }
                                ).executeAsync();

                                Intent mainIntent = new Intent(FacebookLogin.this, MapActivity.class);
                                FacebookLogin.this.startActivity(mainIntent);
                                FacebookLogin.this.finish(); //Scott: Was there a reason we skipped this???
                            } catch (JSONException e) {
                                Log.w("SOCIAL SIGN UP FAIL: ", e.getMessage().toString());
                            }
                        }

                        //Handler of alternative JSONArray form
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("SOCIAL SIGNUP SUCCESS3", statusCode + ": " + response.toString());
                            try {
                                JSONObject firstEvent = response.getJSONObject(0);
                            } catch (JSONException e) {
                                Log.w("SOCIAL SIGNUP FAIL2", e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("SOCIAL SIGNUP FAIL3", "Error Code: " + error_code + ", Text: " + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                            Log.w("SOCIAL SIGNUP FAIL3", "Error Code: " + error_code + ", JSON Object: " + jsonObject.toString());
                        }
                    });
                }

                @Override
                public void onCancel() {
                    Log.w("FACEBOOK LOGIN: ", "Cancelled");
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.w("FACEBOOK LOGIN: ", "Error");
                    // App code
                }
            });
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Helper method to check if the user is logged in or not
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //Parth's Get Last Known Location
    private void getLocationPermission() {



        if (ContextCompat.checkSelfPermission(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(FacebookLogin.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {

                ActivityCompat.requestPermissions(FacebookLogin.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {
            gps = new GPSTracker(FacebookLogin.this);

            // check if GPS enabled
            if(gps.canGetLocation()){

                clat = gps.getLatitude();
                clon = gps.getLongitude();

                // \n is for new line
               // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                 //       + clat + "\nLong: " + clon, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                Toast.makeText(FacebookLogin.this,"Please enable your GPS in settings",Toast.LENGTH_LONG).show();
            }
//            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//            Location location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            try {
//
//                if (location == null) {
//                    location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                }
//                clat = location.getLatitude();
//                clon = location.getLongitude();
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//                Toast.makeText(FacebookLogin.this, "turn on your gps please", Toast.LENGTH_LONG).show();
//                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//             //   gspEnableFlag = 1;
          //  }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {
                        {
                            gps = new GPSTracker(FacebookLogin.this);

                            // check if GPS enabled
                            if(gps.canGetLocation()){

                                clat = gps.getLatitude();
                                clon = gps.getLongitude();

                                // \n is for new line
                             //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                               //         + clat + "\nLong: " + clon, Toast.LENGTH_LONG).show();
                            }else{
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                Toast.makeText(FacebookLogin.this,"Please enable your GPS in settings",Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(FacebookLogin.this, "No permission granted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
