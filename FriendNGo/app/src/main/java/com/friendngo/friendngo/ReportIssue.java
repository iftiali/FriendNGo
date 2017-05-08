package com.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ReportIssue extends AppCompatActivity {
    private Button submitIssueButton;
    private String reportIssueToast = null;
    private String checkOnlineToast = null;
    private String reportIssueSuccessToast = null;
    private String reportIssueEmptyToast = null;
    private EditText issue_edit_text;
    private boolean validateReport=false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Init xml
        initXmlView();
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        reportIssueToast = getResources().getString(R.string.reportIssueToast);
        reportIssueEmptyToast = getResources().getString(R.string.reportIssueEditTextEmptyToast);
        reportIssueSuccessToast = getResources().getString(R.string.reportIssueSuccessToast);
        //Submit report button
        submitIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postApiIssueReport();
            }
        });
        //count and validate character is not more than 4000.
        issue_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String countText=issue_edit_text.getText().toString();
                if(countText.length()>4000){
                    Toast.makeText(getApplicationContext(),"MORE THAN 4000 CHARACTERS? WHAT ARE YOU WRITING AN ESSAY?",Toast.LENGTH_LONG).show();
                    validateReport  =true;
                }else
                {
                    validateReport = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void initXmlView(){
        submitIssueButton = (Button)findViewById(R.id.submit_issue_button);
        issue_edit_text = (EditText) findViewById(R.id.issue_edit_text);
    }

    private void postApiIssueReport(){

        if(ValidationClass.checkOnline(getApplicationContext())){
            if(validateReport){
                Toast.makeText(getApplicationContext(),reportIssueToast,Toast.LENGTH_LONG).show();
            }else {
                if (issue_edit_text.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),reportIssueEmptyToast,Toast.LENGTH_LONG).show();

                }else{

                    AsyncHttpClient client = new AsyncHttpClient();
                    if (SignIn.static_token != null) {
                        client.addHeader("Authorization", "Token " + SignIn.static_token);
                    }

                    RequestParams params = new RequestParams();

                    String code = issue_edit_text.getText().toString();
                    params.put("report", code);

                    client.post(MainActivity.base_host_url + "api/postBugReport/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST report SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            issue_edit_text.setText("");
                            Toast.makeText(getApplicationContext(), reportIssueSuccessToast, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("POST report SUCCESS3", statusCode + ": " + response.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Log.w("POST report RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST report FAILURE2", "Error Code: " + error_code + text);
                        }

                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.w("POST report FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                        }
                    });
                }
            }


        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }

    }
}
