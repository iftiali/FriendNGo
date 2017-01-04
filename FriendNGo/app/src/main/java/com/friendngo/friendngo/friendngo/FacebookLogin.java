package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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

import cz.msebera.android.httpclient.Header;

public class FacebookLogin extends AppCompatActivity {

    CallbackManager callbackManager;
    public static boolean using_facebook = false;

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
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            getSupportActionBar().hide();
        }

        //Sets up the callback to the Facebook API for the facebook button
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
//        loginButton.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                /* Create an Intent that will start the Menu-Activity. */
                Log.w("FACEBOOK LOGIN: ", "Success! Token: " + loginResult.getAccessToken().getToken());

                // Now Let's Call the Facebook API to get the user's e-mail, birthday, picture, etc.
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.w("FACEBOOK RESPONSE: ", response.toString());

                                try {
                                    // Application code
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                } catch (JSONException e){}

                                    //Now Finally Call the Django Backend to get convert the Facebook token to a Django Token
                                    //HTTP ASYNC CODE to authenticate with the backend server
                                    AsyncHttpClient client = new AsyncHttpClient();
//                                  client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                                    RequestParams params = new RequestParams();
//                                    params.setUseJsonStreamer(true);
                                    params.put("grant_type", "convert_token");
                                    params.put("client_id", getString(R.string.django_client_id));
                                    params.put("client_secret", getString(R.string.django_client_secret));
                                    params.put("backend", "facebook");
                                    params.put("token", loginResult.getAccessToken().getToken());

                                    client.post(MainActivity.base_host_url + "api/auth/convert-token", params, new JsonHttpResponseHandler() {
//                                client.post("http://requestb.in/1ni37eo1", params, new JsonHttpResponseHandler() {



                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

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

//                                              static_username = emailEditTextValue.getText().toString();
//                                              static_token = firstEvent.getString("token");
//                                              Log.w("HTTP SUCCESS: ", static_token.toString());

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
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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
