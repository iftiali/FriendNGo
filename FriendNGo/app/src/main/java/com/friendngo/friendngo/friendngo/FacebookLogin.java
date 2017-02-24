package com.friendngo.friendngo.friendngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import cz.msebera.android.httpclient.Header;

public class FacebookLogin extends AppCompatActivity {
    public static double clat = 0, clon = 0;
    CallbackManager callbackManager;
    private Button useEmailButton;
    LocationManager locationmanager = null;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private String TOKEN_PREFERENCE = "token_preference";
    int gspEnableFlag = 0;

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

        if (MainActivity.cheat_mode == true) {
            Intent mainIntent = new Intent(FacebookLogin.this, SignIn.class);
            FacebookLogin.this.startActivity(mainIntent);
        }

        //If the user is logged in then go straight to the New City Activity
        if (isLoggedIn()) {
            SharedPreferences prefs = getSharedPreferences(TOKEN_PREFERENCE, MODE_PRIVATE);
            String shared_preference_token = prefs.getString("token", null);
            if (shared_preference_token != null) {
                SignIn.static_token = shared_preference_token;
            }
            if(gspEnableFlag ==1){
                getLocationPermission();
            }else {
                Intent mainIntent = new Intent(FacebookLogin.this, Popular.class);
                FacebookLogin.this.startActivity(mainIntent);
            }
        } else {
            //Handler for the button to go to e-mail login
            useEmailButton = (Button) findViewById(R.id.use_email_button);
            useEmailButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if(gspEnableFlag ==1){
                        getLocationPermission();
                    }else {
                        Intent mainIntent = new Intent(FacebookLogin.this, SignIn.class);
                        FacebookLogin.this.startActivity(mainIntent);
                    }
                    // FacebookLogin.this.finish();
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
                    params.put("access_token", loginResult.getAccessToken().getToken());
                    client.post(MainActivity.base_host_url + "api/exchange_token/facebook/", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("SOCIAL SIGN UP SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            try {
                                SignIn.static_token = response.get("token").toString();
                                Log.w("TOKEN SUCCESS2: ", SignIn.static_token);
                                SharedPreferences.Editor editor = getSharedPreferences(TOKEN_PREFERENCE, MODE_PRIVATE).edit();
                                editor.putString("token", response.get("token").toString());
                                editor.commit();
                                Intent mainIntent = new Intent(FacebookLogin.this, MapActivity.class);
                                FacebookLogin.this.startActivity(mainIntent);
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

        gspEnableFlag = 0;

        if (ContextCompat.checkSelfPermission(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(FacebookLogin.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {

                ActivityCompat.requestPermissions(FacebookLogin.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

        } else {

            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            try {

                if (location == null) {
                    Log.w("Hello1a","Hello1a");
                    location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }
                Log.w("Hello1b","Hello1b");
                clat = location.getLatitude();
                clon = location.getLongitude();

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(FacebookLogin.this, "turn on your gps please", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                gspEnableFlag = 1;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(FacebookLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {
                        {
                            LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Location location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            try {

                                if (location == null) {
                                    Log.w("Hello2a","Hello2a");
                                    location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }
                                Log.w("Hello2b","Hello2b");
                                clat = location.getLatitude();
                                clon = location.getLongitude();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(FacebookLogin.this, "Network not found", Toast.LENGTH_LONG).show();
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
