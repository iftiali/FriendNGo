package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


    private CheckBox checkBoxRememberMe;
    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;
    boolean flag = false;
    //private SharedPreferences sharedPref;
    public static String static_token;
    public static String static_username;
    public static String static_profile_image_url;

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
                        MainActivity.new_user = false;
                        Log.w("POST AUTH SUCCESS2", static_token);
                        //Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                       Intent intent = new Intent(SignIn.this, MapActivity.class);

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
                        MainActivity.new_user = false;
                        JSONObject firstEvent = response.getJSONObject(0);
                        static_username = "t2@t2.com";
                        static_token = firstEvent.getString("token");
                        Log.w("POST AUTH SUCCESS4", static_token.toString());
                        //Intent intent = new Intent(SignIn.this, NewWhoAreYouActivity.class);
                        Intent intent = new Intent(SignIn.this, MapActivity.class);

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
            checkBoxRememberMe = (CheckBox)findViewById(R.id.checkBoxRememberME);
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
                    authCheck();

                }
            });
        }
    }
    private void authCheck(){
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
                flag = isOnline();

                if (flag) {

                    // Toast.makeText(SignIn.this, " Network connection is ok ", Toast.LENGTH_LONG).show();
                    client.post(MainActivity.base_host_url + "api-token-auth/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            Log.w("POST AUTH SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            try {
                                MainActivity.new_user = false;
                                static_username = emailEditTextValue.getText().toString();
                                static_token = response.get("token").toString();
                                Log.w("POST AUTH SUCCESS2", static_token);
                                if(checkBoxRememberMe.isEnabled()){

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("EmailToken", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("token", static_token);
                                    editor.commit();
                                }
                                Intent intent = new Intent(SignIn.this, MapActivity.class);
                              //  Intent intent = new Intent(SignIn.this, ReportIssue.class);
                                SignIn.this.startActivity(intent);
                                SignIn.this.finish();

                            } catch (JSONException e) {
                                Log.w("POST AUTH FAIL", e.getMessage().toString());
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("POST AUTH SUCCESS3", statusCode + ": " + response.toString());
//
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
                            Log.w("POST AUTH FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                            Toast.makeText(SignIn.this, "Invalid user", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(SignIn.this, "Sorry! Not connected to internet", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SignIn.this, "Invalid Email ", Toast.LENGTH_LONG).show();
            }
        }else {
            //message
            Toast.makeText(SignIn.this, "Email or password is empty ", Toast.LENGTH_LONG).show();
        }
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isOnline();
//       // loadPreferences();
//        Log.i("on resume","onResume invoked map");
//    }
//    private void removePreferences() {
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//
//        // Edit and commit
//        UnameValue = emailEditTextValue.getText().toString();
//        PasswordValue = passwordEditTextValue.getText().toString();
//        System.out.println("onPause save name: " + UnameValue);
//        System.out.println("onPause save password: " + PasswordValue);
//        editor.putString(PREF_UNAME, UnameValue);
//        editor.putString(PREF_PASSWORD, PasswordValue);
//        editor.commit();
//    }
//    private void savePreferences() {
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//
//        // Edit and commit
//        UnameValue = emailEditTextValue.getText().toString();
//        PasswordValue = passwordEditTextValue.getText().toString();
//        System.out.println("onPause save name: " + UnameValue);
//        System.out.println("onPause save password: " + PasswordValue);
//        editor.putString(PREF_UNAME, UnameValue);
//        editor.putString(PREF_PASSWORD, PasswordValue);
//        editor.commit();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//       // savePreferences();
//
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.i(My_TAG,"onStop invoked map");
//    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i(My_TAG,"onRestart invoked map ");
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i(My_TAG,"onDestroy invoked map");
//    }
//    private void loadPreferences() {
//
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//
//        // Get value
//        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
//        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
//        if(UnameValue.equals("") ||UnameValue.equals(null) ){
//            emailEditTextValue.setText(UnameValue);
//            passwordEditTextValue.setText(PasswordValue);
//            authCheck();
//        }else{
//            Log.d("Auth Pass","Failed");
//        }
//
//        //System.out.println("onResume load name: " + UnameValue);
//        //System.out.println("onResume load password: " + PasswordValue);
//    }
}
