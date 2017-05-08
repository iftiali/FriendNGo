package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignIn extends AppCompatActivity {


    private CheckBox checkBoxRememberMe;
    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;
    boolean flag = false;
    AlertDialog dialogOne;
    private String checkOnlineToast = null;
    AlertDialog dialogTwo;
    private String phoneNumberEmptyMessage = null;
    private String someEmptyMessage = null;
    private String codeErrorMessage = null;
    private String matchPassword = null;
    private String invalidPhoneNumber = null;
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

        //Toast message
        invalidPhoneNumber = getResources().getString(R.string.dialog_verification_four_phone_error_message);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        matchPassword = getResources().getString(R.string.dialog_verification_four_password_match_message);
        codeErrorMessage = getResources().getString(R.string.dialog_verification_four_code_error_message);
        phoneNumberEmptyMessage = getResources().getString(R.string.dialog_verification_two_phone_empty_message);
        someEmptyMessage = getResources().getString(R.string.dialog_verification_four_empty_message);

        forgotLinkTextView = (TextView)findViewById(R.id.forgot_link);
        forgotLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
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
                               String verificationPhoneNumber = countryCodeEditText.getText().toString() + veriflyPhoneNumber.getText().toString();
                                if(countryCodeEditText.getText().toString().equals("")||veriflyPhoneNumber.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(),phoneNumberEmptyMessage,Toast.LENGTH_LONG).show();
                                 }else {
                                    getPasswordResetCode(verificationPhoneNumber);
                                }
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

    private void getPasswordResetCode(final String verificationPhoneNumber){

        if(ValidationClass.checkOnline(getApplicationContext())) {
            AsyncHttpClient client = new AsyncHttpClient();
//            if (SignIn.static_token != null) {
//                client.addHeader("Authorization", "Token " + SignIn.static_token);
//            }

            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
           // params.put("phone",verificationPhoneNumber);
            params.put("phone",verificationPhoneNumber);
            client.post(MainActivity.base_host_url + "api/getPasswordResetCode/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.w("POST RESET CODE", statusCode + ": " + "Response = " + response.toString());

                     AlertDialog.Builder mBuilderPartFour = new AlertDialog.Builder(SignIn.this);
                     View mviewPartFour = getLayoutInflater().inflate(R.layout.dialog_verfication_four,null);
                     Button submit = (Button) mviewPartFour.findViewById(R.id.dialog_verification_four_submit);
                     final EditText codeEditTest = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_edit_code);
                     final EditText password = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_password);
                     final EditText confimPassword = (EditText)mviewPartFour.findViewById(R.id.dialog_verification_four_password_confirm);
                     submit.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                      if(codeEditTest.getText().toString().equals("")||password.getText().toString().equals("")||confimPassword.getText().toString().equals("")) {
                      Toast.makeText(getApplicationContext(), someEmptyMessage, Toast.LENGTH_SHORT).show();
                          }else
                              {
                                if (password.getText().toString().equals(confimPassword.getText().toString())) {
                                                twilioResetPassword(codeEditTest.getText().toString(),password.getText().toString());
                                  } else {
                                          Toast.makeText(getApplicationContext(), matchPassword, Toast.LENGTH_SHORT).show();
                                          }
                                        }
                                   }
                                });
                                mBuilderPartFour.setView(mviewPartFour);
                                dialogFour = mBuilderPartFour.create();
                                dialogFour.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    Log.w("POST RESET CODE2", statusCode + ": " + timeline.toString());
                    //  NewWhoAreYouActivity.this.finish();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("POST RESET CODE", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("POST RESET CODE", "Error Code: " + error_code + "," + text);
                    if (error_code == 401 ){
                        Toast.makeText(getApplicationContext(),invalidPhoneNumber,Toast.LENGTH_LONG).show();
                        }
                }

                @Override
                public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                    Log.w("POST RESET CODE", "Error Code: " + error_code + ",  " + json.toString());
                    if (error_code == 401){
                        Toast.makeText(getApplicationContext(),invalidPhoneNumber,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();

        }
    }
    private void twilioResetPassword(String code,String password){
        if(ValidationClass.checkOnline(getApplicationContext())) {
            AsyncHttpClient client = new AsyncHttpClient();
//            if (SignIn.static_token != null) {
//                client.addHeader("Authorization", "Token " + SignIn.static_token);
//            }

            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
            params.put("generated_code",code);
            params.put("new_password",password);
            client.post(MainActivity.base_host_url + "api/twilioResetPassword/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.w("POST RESET PASSWORD", statusCode + ": " + "Response = " + response.toString());

                    dialogTwo.dismiss();
                    dialogOne.dismiss();
                    dialogFour.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    Log.w("POST RESET PASSWORD", statusCode + ": " + timeline.toString());
                    //  NewWhoAreYouActivity.this.finish();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("POST RESET PASSWORD", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("POST RESET PASSWORD", "Error Code: " + error_code + "," + text);
                    if(error_code == 400){
                        Toast.makeText(getApplicationContext(),codeErrorMessage,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                    Log.w("POST RESET PASSWORD", "Error Code: " + error_code + ",  " + json.toString());
                    if(error_code == 400){
                        Toast.makeText(getApplicationContext(),codeErrorMessage,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();

        }
    }
}
