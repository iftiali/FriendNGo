package com.friendngo.friendngo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ResetPasswordActivity extends AppCompatActivity {
    private String checkOnlineToast = null;
    private EditText  edit_text_reset_login_email;
    private String edit_Text = null;
    private String reset_login_email = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        edit_Text = getResources().getString(R.string.email_reset_field_Toast);
        initXmlView();
        getApiResetPassword();
    }
    public void initXmlView(){
    edit_text_reset_login_email = (EditText)findViewById(R.id.reset_login_email);
    }
    private void getApiResetPassword() {
        if(edit_text_reset_login_email.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), checkOnlineToast, Toast.LENGTH_LONG).show();
        }else {

            if (ValidationClass.checkOnline(getApplicationContext())) {
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }
                RequestParams params = new RequestParams();
                params.put("email", edit_text_reset_login_email.getText().toString());
                client.post(MainActivity.base_host_url + "djoser/password/reset/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.w("POST UPDATE REQUEST", statusCode + ": " + "Response = " + response.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST UPDATE REQUEST", statusCode + ": " + timeline.toString());
                        //
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        Log.w("POST PROFILE RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Log.w("POST PROFILE FAIL", "Error Code: " + error_code + "," + text);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                        Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), checkOnlineToast, Toast.LENGTH_LONG).show();
            }
        }
    }
}
