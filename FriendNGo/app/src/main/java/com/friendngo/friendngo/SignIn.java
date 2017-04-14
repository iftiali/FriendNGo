package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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

import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignIn extends AppCompatActivity {


    private CheckBox checkBoxRememberMe;
    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;
    boolean flag = false;
    AlertDialog dialogOne;
    AlertDialog dialogTwo;
    AlertDialog dialogFour;
    String signInSubTitle= null;
    //private SharedPreferences sharedPref;
    public static String static_token;
    public static String static_username;
    public static String static_profile_image_url;
    private TextView forgotLinkTextView;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        forgotLinkTextView = (TextView)findViewById(R.id.forgot_link);
        forgotLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mBuilderPartOne = new AlertDialog.Builder(SignIn.this);
                View mviewPartOne = getLayoutInflater().inflate(R.layout.dialog_verification_part_one,null);

                TextView subViewTextView = (TextView) mviewPartOne.findViewById(R.id.dialog_sub_view);
                subViewTextView.setText(signInSubTitle);

                Button dialog_next_button = (Button)mviewPartOne.findViewById(R.id.dialog_next_button);

                dialog_next_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder mBuilderPartTwo = new AlertDialog.Builder(SignIn.this);
                        View mviewPartTwo = getLayoutInflater().inflate(R.layout.dialog_verification_part_two,null);
                        Button textMe = (Button) mviewPartTwo.findViewById(R.id.dialog_verification_two_text_me);
                        final EditText countryCodeEditText = (EditText)mviewPartTwo.findViewById(R.id.dialog_verification_two_country_code_edit_text);
                        final EditText veriflyPhoneNumber = (EditText)mviewPartTwo.findViewById(R.id.dialog_verification_two_text_me_edit_text);
                        textMe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogTwo.dismiss();
                                dialogOne.dismiss();
                                AlertDialog.Builder mBuilderPartFour = new AlertDialog.Builder(SignIn.this);
                                View mviewPartFour = getLayoutInflater().inflate(R.layout.dialog_verfication_four,null);
                                Button submit = (Button) mviewPartFour.findViewById(R.id.dialog_verification_four_submit);
                                EditText codeEditTest = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_edit_code);
                                EditText password = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_password);
                                EditText confimPassword = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_password_confirm);
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogFour.dismiss();
                                    }
                                });
                                mBuilderPartFour.setView(mviewPartFour);
                                dialogFour = mBuilderPartFour.create();
                                dialogFour.show();
                            }
                        });
                        mBuilderPartTwo.setView(mviewPartTwo);
                        dialogTwo = mBuilderPartTwo.create();
                        dialogTwo.show();

                    }
                });
                mBuilderPartOne.setView(mviewPartOne);
                dialogOne = mBuilderPartOne.create();
                dialogOne.show();
            }
        });


        signInSubTitle = getResources().getString(R.string.login_in_dialog_one);

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

        RequestParams params = new RequestParams();
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
                                    editor.putString("email_token", static_token);
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
                           //8 Log.w("POST AUTH FAILURE", String.valueOf(statusCode) + errorResponse.toString());
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

    private void getForgotPassword(){



    }
}
