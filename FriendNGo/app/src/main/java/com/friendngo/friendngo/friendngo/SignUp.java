package com.friendngo.friendngo.friendngo;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


public class SignUp extends AppCompatActivity {

    private TextView textView;
    private Button signupButton;

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //Sets the top bar text
        getSupportActionBar().setTitle("Create an account");

        //Creates a code instance of the buttons and text inputs
        textView = (TextView) findViewById(R.id.account_link);
        emailEditTextValue = (EditText) findViewById(R.id.signup_email);
        passwordEditTextValue = (EditText) findViewById(R.id.signup_password);
        signupButton = (Button) findViewById(R.id.signup_button);

        //Sets the callback for when the user presses the I have an account link
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent mainIntent = new Intent(SignUp.this,SignIn.class);
                SignUp.this.startActivity(mainIntent);
                SignUp.this.finish();
                return false;
            }
        });

        //Sets the callback for when the user submits his username and password
        signupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Make a call to register the user
                AsyncHttpClient client = new AsyncHttpClient();
//                client.setBasicAuth(emailEditTextValue.getText().toString(),passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
                params.put("username", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());

                client.post(MainActivity.base_host_url + "users/register/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.w("POST REGISTER SUCCESS: ", statusCode + ": " + "Response = " + response.toString());

                            //Now that you are registered, call the authenticate class
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                            RequestParams params = new RequestParams();
//                          params.setUseJsonStreamer(true);
                            params.put("username", emailEditTextValue.getText().toString());
                            params.put("password", passwordEditTextValue.getText().toString());

                            client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    Log.w("AUTH POST SUCCESS", statusCode + ": " + "Response = " + response.toString());
                                    try{
                                        SignIn.static_username = emailEditTextValue.getText().toString();
                                        SignIn.static_token = response.get("token").toString();
                                        Log.w("AUTH POST SUCCESS2", SignIn.static_token.toString());

                                        Intent intent = new Intent(SignUp.this,Popular.class);
                                        SignUp.this.startActivity(intent);
                                        SignUp.this.finish();

                                    }catch (JSONException e){
                                        Log.w("AUTH POST FAIL",e.getMessage().toString());
                                    }
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    Log.w("AUTH POST SUCCESS?", statusCode + ": " + response.toString());
                                    try {
                                        JSONObject firstEvent = response.getJSONObject(0);

                                        SignIn.static_username = emailEditTextValue.getText().toString();
                                        SignIn.static_token = firstEvent.getString("token");
                                        Log.w("AUTH POST SUCCESS", SignIn.static_token.toString());

                                        Intent intent = new Intent(SignUp.this,Popular.class);
                                        SignUp.this.startActivity(intent);
                                        SignUp.this.finish();

                                    } catch (JSONException e) {
                                        Log.w("AUTH POST FAIL", e.getMessage().toString());
                                    }
                                }

                                @Override
                                public void onRetry(int retryNo) {
                                    // called when request is retried
                                }

                                //TODO: Give Users Helpful Error messages when there is a problem
                                @Override
                                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                                    Log.w("HTTP FAILURE", "Error Code: " + error_code);
                                }
                            });
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.w("HTTP SUCCESS: ", statusCode + ": " + response.toString());
                        try {
                            JSONObject firstEvent = response.getJSONObject(0);

                            Intent intent = new Intent(SignUp.this,Popular.class);
                            SignUp.this.startActivity(intent);
                            SignUp.this.finish();

                        } catch (JSONException e) {
                            Log.w("HTTP JSON ERROR: ", e.getMessage().toString());
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
