package com.friendngo.friendngo.friendngo;

import android.app.Activity;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public static String base_host_url = "http://54.175.1.158:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//      TODO: Just a Reminder you can log in app purchases via Facebook console using this line of code
//        logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,FacebookLogin.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        This is supposed to generate HASH keys that will work more than once without resetting the app settings... but it doesn't work as advertised

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.packagename",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.w("KeyHash:", " Package Manager FAIL");
//        } catch (NoSuchAlgorithmException e) {
//            Log.w("KeyHash:", "No algo FAIL");
//        }

    }
}
