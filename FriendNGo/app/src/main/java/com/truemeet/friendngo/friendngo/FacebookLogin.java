package com.truemeet.friendngo.friendngo;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FacebookLogin extends AppCompatActivity {

    CallbackManager callbackManager;
    public static boolean using_facebook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_facebook_login);

        if (Build.VERSION.SDK_INT >= 16) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            getSupportActionBar().hide();
        }

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /* Create an Intent that will start the Menu-Activity. */
                Log.w("FACEBOOK LOGIN: ", "Success! Starting New City Page");

                //TODO: Send the access token to the FriendNGo backend
//              loginResult.getAccessToken();

                //HTTP ASYNC CODE
                AsyncHttpClient client = new AsyncHttpClient();
//                client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
//                params.put("username", emailEditTextValue.getText().toString());
//                params.put("password", passwordEditTextValue.getText().toString());

                client.post(MainActivity.base_host_url + "api/fbsignup", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        //TODO: Test and implement statusCode handlers for developers and graceful degradation
                        Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                        try{
                            String fb_token = response.get("token").toString();
                            Log.w("HTTP SUCCESS: ", fb_token);

                            Intent mainIntent = new Intent(FacebookLogin.this,NewCity.class);
                            FacebookLogin.this.startActivity(mainIntent);
                            FacebookLogin.this.finish();

                        }catch (JSONException e){
                            Log.w("HTTP FAIL: ",e.getMessage().toString());
                        }
                    }

                    //Handler of alternative JSONArray form
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.w("HTTP SUCCESS: ", statusCode + ": " + response.toString());
                        try {
                            JSONObject firstEvent = response.getJSONObject(0);

//                            static_username = emailEditTextValue.getText().toString();
//                            static_token = firstEvent.getString("token");
//                            Log.w("HTTP SUCCESS: ", static_token.toString());

                        } catch (JSONException e) {
                            Log.w("HTTP FAIL: ", e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });

                using_facebook = true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
