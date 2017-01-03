package com.friendngo.scott.friendngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

public class SignIn extends AppCompatActivity {

    private TextView textView;
    private Button signinButton;

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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

                client.post("http://54.175.1.158:8000/api-token-auth/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                            try{
                                Log.w("HTTP SUCCESS: ", response.get("token").toString());
                            }catch (JSONException e){
                                Log.w("HTTP FAIL: ",e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("HTTP SUCCESS: ", statusCode + ": " + timeline.toString());
                            try {
                                JSONObject firstEvent = timeline.getJSONObject(0);
                                String token = firstEvent.getString("token");
                                Log.w("HTTP SUCCESS: ", token.toString());
                            } catch (JSONException e) {
                                Log.w("HTTP FAIL: ", e.getMessage().toString());
                            }

                        }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                    });
                }
            });
        }
    }
