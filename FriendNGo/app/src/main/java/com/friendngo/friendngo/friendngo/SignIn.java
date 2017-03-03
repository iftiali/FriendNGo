package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignIn extends AppCompatActivity {

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;
    boolean flag = false;
    //private SharedPreferences sharedPref;
    public static String static_token;
    public static String static_username;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        static_token = "";
        static_username = "";


        //check internet connection
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Sets the top heading value

        //Cheat Mode To Go Straight To Map Activity
        if (MainActivity.cheat_mode == true) {
            //Go straight to signing in with default user
            AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth("t2@t2.com", "fun");
            RequestParams params = new RequestParams();
//          params.setUseJsonStreamer(true);
            params.put("username", "t2@t2.com");
            params.put("password", "fun");
            client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.w("POST AUTH SUCCESS", statusCode + ": " + "Response = " + response.toString());
                    try {
                        static_username = "t2@t2.com";
                        static_token = response.get("token").toString();
                        Log.w("POST AUTH SUCCESS2", static_token);
                        Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                       // Intent intent = new Intent(SignIn.this, MapActivity.class);

                        SignIn.this.startActivity(intent);
                        SignIn.this.finish();

                    } catch (JSONException e) {
                        Log.w("POST AUTH FAIL", e.getMessage().toString());
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.w("POST AUTH SUCCESS3", statusCode + ": " + response.toString());
                    try {
                        JSONObject firstEvent = response.getJSONObject(0);
                        static_username = "t2@t2.com";
                        static_token = firstEvent.getString("token");
                        Log.w("POST AUTH SUCCESS4", static_token.toString());
                        Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                       // Intent intent = new Intent(SignIn.this, MapActivity.class);

                        SignIn.this.startActivity(intent);
                        SignIn.this.finish();

                    } catch (JSONException e) {
                        Log.w("POST AUTH JSON ERROR", e.getMessage().toString());
                    }
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("POST AUTH RETRY", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("POST AUTH FAILURE :(", "Error Code: " + error_code);
                }
            });
        } else {//Normal User Mode

            //Creates a code instance of the buttons and text input fields
            TextView textView = (TextView) findViewById(R.id.create_account_link);
            emailEditTextValue = (EditText) findViewById(R.id.login_email);
            passwordEditTextValue = (EditText) findViewById(R.id.login_password);
            Button signinButton = (Button) findViewById(R.id.signin_button);

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
                    //By Parth 30-Jan-2017
                    //client.setBasicAuth(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());

                    RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
                    params.put("username", emailEditTextValue.getText().toString());
                    params.put("password", passwordEditTextValue.getText().toString());
                    boolean isNullCheck = ValidationClass.isNullCheck(emailEditTextValue.getText().toString(), passwordEditTextValue.getText().toString());
                    boolean isEmailValid = ValidationClass.isValidEmail(emailEditTextValue.getText().toString());
                    //Log.w("error", String.valueOf(isEmailValid));
                    if (isNullCheck) {
                        if (isEmailValid) {
                            flag = isInternetOn();
                            if (flag) {

                               // Toast.makeText(SignIn.this, " Network connection is ok ", Toast.LENGTH_LONG).show();
                                client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                        Log.w("POST AUTH SUCCESS", statusCode + ": " + "Response = " + response.toString());
                                        try {
                                            static_username = emailEditTextValue.getText().toString();
                                            static_token = response.get("token").toString();
                                            Log.w("POST AUTH SUCCESS2", static_token);

                                           Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                                          //  Intent intent = new Intent(SignIn.this, MapActivity.class);
                                            SignIn.this.startActivity(intent);
                                            SignIn.this.finish();

                                        } catch (JSONException e) {
                                            Log.w("POST AUTH FAIL", e.getMessage().toString());
                                        }
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                        Log.w("POST AUTH SUCCESS3", statusCode + ": " + response.toString());
                                        try {
                                            JSONObject firstEvent = response.getJSONObject(0);

                                            static_username = emailEditTextValue.getText().toString();
                                            static_token = firstEvent.getString("token");
                                            Log.w("POST AUTH SUCCESS4", static_token.toString());

                                            Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                                           // Intent intent = new Intent(SignIn.this, MapActivity.class);
                                            SignIn.this.startActivity(intent);
                                            SignIn.this.finish();

                                        } catch (JSONException e) {
                                            Log.w("POST AUTH JSON ERROR", e.getMessage().toString());
                                        }
                                    }

                                    @Override
                                    public void onRetry(int retryNo) {
                                        // called when request is retried
                                        Log.w("POST AUTH RETRY", "" + retryNo);
                                    }

                                    @Override
                                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                                        Log.w("POST AUTH FAILURE2", "Error Code: " + error_code + text);
                                        Toast.makeText(SignIn.this, "Invalid user", Toast.LENGTH_LONG).show();

                                    }

                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        AsyncHttpClient.log.w("POST AUTH FAILURE", String.valueOf(statusCode));
                                        Toast.makeText(SignIn.this, "Invalid user", Toast.LENGTH_LONG).show();
                                    }

                                });
                            }else{
                                Toast.makeText(SignIn.this, "Sorry your net connection is fail", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(SignIn.this, "Invalid Email ", Toast.LENGTH_LONG).show();

                        }
                    }else {
                        //message
                        Toast.makeText(SignIn.this, "Email or password is empty ", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

}
