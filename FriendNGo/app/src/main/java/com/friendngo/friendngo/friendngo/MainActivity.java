package com.friendngo.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {

    private final int PRODUCTION_SETTING = 0;
    private final int STAGING_SETTING = 1;
    private final int TEST_SETTING = 2;

    private int server_mode = STAGING_SETTING;
    private int SPLASH_DISPLAY_LENGTH = 2000;
    public static String base_host_url = "";
    public static boolean cheat_mode = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(cheat_mode==false){
            SPLASH_DISPLAY_LENGTH=2000;
        } else {
            SPLASH_DISPLAY_LENGTH=5;
        }

        if(server_mode == PRODUCTION_SETTING){
            base_host_url = "http://api.friendngo.com/";
        } else if (server_mode == STAGING_SETTING) {
            base_host_url = "http://staging.friendngo.com/";
        } else {
            base_host_url = "http://test.friendngo.com/";
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,FacebookLogin.class);
               //Intent mainIntent = new Intent(MainActivity.this,CreateActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
