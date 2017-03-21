package com.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends Activity {

    private final int PRODUCTION_SETTING = 0;
    private final int STAGING_SETTING = 1;
    private final int TEST_SETTING = 2;

    private int server_mode = PRODUCTION_SETTING;
    private int SPLASH_DISPLAY_LENGTH = 2000;
    public static String base_host_url = "";
    public static boolean cheat_mode = false;
    public static boolean new_user = true;

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
            base_host_url = "https://api.friendngo.com/";
        } else if (server_mode == STAGING_SETTING) {
            base_host_url = "https://staging.friendngo.com/";
        } else {
            base_host_url = "https://test.friendngo.com/";
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,FacebookLogin.class);
               //Intent mainIntent = new Intent(MainActivity.this,ActivityListWithNAvigationDrawer.class);

                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
