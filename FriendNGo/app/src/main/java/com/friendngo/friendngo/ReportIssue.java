package com.friendngo.friendngo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ReportIssue extends AppCompatActivity {
    private Button submit_button;
    private EditText issue_edit_text;
    boolean validateReport=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit_button= (Button) findViewById(R.id.submit_next_button);
        issue_edit_text = (EditText) findViewById(R.id.issue_edit_text);
        issue_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            String countText=issue_edit_text.getText().toString();
                if(countText.length()>10){
                    Toast.makeText(getApplicationContext(),"message",Toast.LENGTH_LONG).show();
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
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateReport){
                    Toast.makeText(getApplicationContext(),"Message",Toast.LENGTH_LONG).show();
                }else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    if(SignIn.static_token != null) {
                        client.addHeader("Authorization","Token "+SignIn.static_token);
                    }

                    RequestParams params = new RequestParams();

                    String code = issue_edit_text.getText().toString();
                    params.put("report",code);

                    client.post(MainActivity.base_host_url + "api/postBugReport/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST CODE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("POST CODE SUCCESS3", statusCode + ": " + response.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Log.w("POST CODE RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST CODE FAILURE2", "Error Code: " + error_code + text);
                        }
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.w("POST CODE FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                        }
                    });
                }
            }
        });

    }

}
