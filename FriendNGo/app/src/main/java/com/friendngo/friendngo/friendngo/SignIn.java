package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import cz.msebera.android.httpclient.Header;

public class SignIn extends AppCompatActivity {

    private TextView textView;
    private Button signinButton;

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;

    //TODO: Migrate these simple staticpage variables to shared Prefs
//    private SharedPreferences sharedPref;
    public static String static_token;
    public static String static_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        static_token = "";
        static_username = "";
        //Sets the top heading value
        getSupportActionBar().setTitle("Sign In");

        //Creates a code instance of the buttons and text input fields
        textView = (TextView) findViewById(R.id.create_account_link);
        emailEditTextValue = (EditText) findViewById(R.id.login_email);
        passwordEditTextValue = (EditText) findViewById(R.id.login_password);
        signinButton = (Button) findViewById(R.id.signin_button);

        //Sets the callback for when a user presseses the create account button
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent mainIntent = new Intent(SignIn.this, SignUp.class);
                SignIn.this.startActivity(mainIntent);
                SignIn.this.finish();
                return false;
            }
        });

        //Sets the callback for when a user submits the username and password form
        signinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AsyncHttpClient client = new AsyncHttpClient();
                client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
                params.put("username", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());

                client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            //TODO: Test and implement statusCode handlers for developers and graceful degradation
                            Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                            try{
                                static_username = emailEditTextValue.getText().toString();
                                static_token = response.get("token").toString();
                                Log.w("HTTP SUCCESS: ", static_token);

                                Intent intent = new Intent(SignIn.this,Popular.class);
                                SignIn.this.startActivity(intent);
                                SignIn.this.finish();


                            }catch (JSONException e){
                                Log.w("HTTP FAIL: ",e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("HTTP SUCCESS: ", statusCode + ": " + response.toString());
                            try {
                                JSONObject firstEvent = response.getJSONObject(0);

                                static_username = emailEditTextValue.getText().toString();
                                static_token = firstEvent.getString("token");
                                Log.w("HTTP SUCCESS: ", static_token.toString());

                                Intent intent = new Intent(SignIn.this,Popular.class);
                                SignIn.this.startActivity(intent);
                                SignIn.this.finish();

                            } catch (JSONException e) {
                                Log.w("HTTP FAIL: ", e.getMessage().toString());
                            }
                        }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                        Log.w("HTTP FAILURE:", "Error Code: " + error_code);
                    }
                    });
                }
        });
        }
    }