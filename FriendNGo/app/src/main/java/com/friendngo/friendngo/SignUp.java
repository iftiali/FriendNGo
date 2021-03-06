package com.friendngo.friendngo;

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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SignUp extends AppCompatActivity {

    private TextView textView;
    private Button signupButton;
    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;
    private EditText passwordEditTextValueConfirm;
    private SharedPreferences sharedPref;
    private boolean try_once = true;
    private String matchPasswordMessage = null;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        matchPasswordMessage =  getResources().getString(R.string.sign_up_password_match_message);
        //Creates a code instance of the buttons and text inputs
        textView = (TextView) findViewById(R.id.account_link);
        emailEditTextValue = (EditText) findViewById(R.id.signup_email);
        passwordEditTextValue = (EditText) findViewById(R.id.signup_password);
        signupButton = (Button) findViewById(R.id.signup_button);
        passwordEditTextValueConfirm = (EditText)findViewById(R.id.signup_password_conform);
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

                //POST to /users/register to register the user
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("username", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());
                boolean isNullCheck = ValidationClass.isNullCheck(emailEditTextValue.getText().toString(),passwordEditTextValue.getText().toString());
                boolean isEmailValid = ValidationClass.isValidEmail(emailEditTextValue.getText().toString());
                //Log.w("error", String.valueOf(isEmailValid));
                if(passwordEditTextValueConfirm.getText().equals("")){
                    isNullCheck = false;
                }else {
                    if(passwordEditTextValueConfirm.getText().toString().equals(passwordEditTextValue.getText().toString())){

                        isNullCheck = true;
                    }else {
                        Toast.makeText(getApplicationContext(),matchPasswordMessage,Toast.LENGTH_LONG).show();
                        isNullCheck = false;
                    }

                }
                if(isNullCheck){
                    if(isEmailValid) {
                        MainActivity.new_user = true;
                        client.post(MainActivity.base_host_url + "users/register/", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.w("POST REGISTER SUCCESS: ", statusCode + ": " + "Response = " + response.toString());

                                //Now that you are registered, call the authenticate class
                                AsyncHttpClient client = new AsyncHttpClient();
                                client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                                RequestParams params = new RequestParams();
                                params.put("username", emailEditTextValue.getText().toString());
                                params.put("password", passwordEditTextValue.getText().toString());

                                client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                        Log.w("AUTH POST SUCCESS", statusCode + ": " + "Response = " + response.toString());
                                        try {
                                            MainActivity.new_user = true;
                                            SignIn.static_username = emailEditTextValue.getText().toString();
                                            SignIn.static_token = response.get("token").toString();
                                            Log.w("AUTH POST SUCCESS2", SignIn.static_token.toString());

                                            Intent intent = new Intent(SignUp.this, MapActivity.class);

                                            SignUp.this.startActivity(intent);
                                            SignUp.this.finish();

                                        } catch (JSONException e) {
                                            Log.w("AUTH POST FAIL", e.getMessage().toString());
                                        }
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                        Log.w("AUTH POST SUCCESS?", statusCode + ": " + response.toString());
                                        try {
                                            JSONObject firstEvent = response.getJSONObject(0);
                                            MainActivity.new_user = true;

                                            SignIn.static_username = emailEditTextValue.getText().toString();
                                            SignIn.static_token = firstEvent.getString("token");
                                            Log.w("AUTH POST SUCCESS", SignIn.static_token.toString());

                                            Intent intent = new Intent(SignUp.this, MapActivity.class);

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

                                    @Override
                                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                        Log.w("HTTP FAILURE1", "Error Code: " + error_code + ", Text: " + text);
                                        Toast.makeText(SignUp.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                                        Log.w("HTTP FAILURE1", "Error Code: " + error_code + ", HEADER: " + headers.toString() + ", JSON: " + jsonObject);
                                        Toast.makeText(SignUp.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                Log.w("HTTP SUCCESS: ", statusCode + ": " + response.toString());
                                try {
                                    MainActivity.new_user = true;

                                    JSONObject firstEvent = response.getJSONObject(0);
                                    Intent intent = new Intent(SignUp.this, MapActivity.class);
                                    SignUp.this.startActivity(intent);
                                    SignUp.this.finish();

                                } catch (JSONException e) {
                                    Log.w("HTTP JSON ERROR: ", e.getMessage().toString());
                                }
                            }

                            //update dev6-12 added


                            @Override
                            public void onRetry(int retryNo) {
                                Log.w("RETRY", retryNo + "");

                                if (try_once==true){
                                    Toast.makeText(SignUp.this, "Check Your Network Connection", Toast.LENGTH_LONG).show();
                                    try_once=false;
                                }
                            }

                            @Override
                            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                Log.w("HTTP FAILURE2", "Error Code: " + error_code + ", Text: " + text);
                                if(error_code==409){
                                    Toast.makeText(SignUp.this, "Username Already Exists", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignUp.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                                Log.w("HTTP JSON FAILURE2", "Error Code: " + error_code + ", JSON: " + jsonObject);
                                Toast.makeText(SignUp.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Toast.makeText(SignUp.this, "Invalid Email format ", Toast.LENGTH_LONG).show();
                    }
                }   else {
                    //message
                    Toast.makeText(SignUp.this, "Email or password is empty ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
