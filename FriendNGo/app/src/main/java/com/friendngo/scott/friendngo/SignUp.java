package com.friendngo.scott.friendngo;

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
import org.w3c.dom.Text;
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

                AsyncHttpClient client = new AsyncHttpClient();
//                client.setBasicAuth(emailEditTextValue.getText().toString(),passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
                params.put("username", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());


//                client.post("http://requestb.in/s3zx6as4", params, new AsyncHttpResponseHandler() {

                client.post("http://54.175.1.158:8000/users/register/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        //TODO: Test and implement statusCode handler for developers and graceful degradation
                        Log.w("HTTP SUCCESS: ", statusCode + ": " + "Response = " + response.toString());
                        try{
                            Log.w("HTTP SUCCESS: ", response.get("token").toString());

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token",response.get("token").toString());
                            editor.commit();

                            Intent intent = new Intent(SignUp.this,NewCity.class);
                            SignUp.this.startActivity(intent);
                            SignUp.this.finish();
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

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token",token.toString());
                            editor.commit();

                            Intent intent = new Intent(SignUp.this,NewCity.class);
                            SignUp.this.startActivity(intent);
                            SignUp.this.finish();

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
