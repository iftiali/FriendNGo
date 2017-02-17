package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

    CallbackManager callbackManager;
    public static boolean using_facebook = false;
    private Button useEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creates fullscreen effect for older phones
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_facebook_login);

        //Creates fullscreen effect for newer phones
        if (Build.VERSION.SDK_INT >= 16) {

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            getSupportActionBar().hide();
        }

        if(MainActivity.cheat_mode==true){
            Intent mainIntent = new Intent(FacebookLogin.this,SignIn.class);
            FacebookLogin.this.startActivity(mainIntent);
            FacebookLogin.this.finish();
        }

        //If the user is logged in then go straight to the New City Activity
        if(isLoggedIn()) {
            Intent mainIntent = new Intent(FacebookLogin.this,Popular.class);
            FacebookLogin.this.startActivity(mainIntent);
            FacebookLogin.this.finish();
        }else{
            //Handler for the button to go to e-mail login
            useEmailButton = (Button) findViewById(R.id.use_email_button);
            useEmailButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent mainIntent = new Intent(FacebookLogin.this,SignIn.class);
                    FacebookLogin.this.startActivity(mainIntent);
                    FacebookLogin.this.finish();
                }
            });

            //Sets up the callback to the Facebook API for the facebook button
            callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

            // Set the permissions that the user should have with the login
            // loginButton.setReadPermissions("email");
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//                "public_profile", "email", "user_birthday", "user_friends"));
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

                                Intent mainIntent = new Intent(FacebookLogin.this, MapActivity.class);
                                FacebookLogin.this.startActivity(mainIntent);
                                FacebookLogin.this.finish();

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
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                            Log.w("SOCIAL SIGNUP FAIL3", "Error Code: " + error_code + ", Text: " + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject jsonObject){
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


}
