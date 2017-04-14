package com.friendngo.friendngo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private Button submitIssueButton;
    private String reportIssueToast = null;
    private String checkOnlineToast = null;
    private String reportIssueSuccessToast = null;
    private EditText issue_edit_text;
    private boolean validateReport=false;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        //to navigate it to Point history.
        Intent intent= new Intent(getApplicationContext(), PointsHistoryActivity.class);
        startActivity(intent);
        //Init xml
        initXmlView(view);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        reportIssueToast = getResources().getString(R.string.reportIssueToast);
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
        return view;
    }
    public void initXmlView(View view){
        submitIssueButton = (Button)view.findViewById(R.id.submit_issue_button);
        issue_edit_text = (EditText) view.findViewById(R.id.issue_edit_text);
    }

    private void postApiIssueReport(){

        if(ValidationClass.checkOnline(getApplicationContext())){
            if(validateReport){
                Toast.makeText(getApplicationContext(),reportIssueToast,Toast.LENGTH_LONG).show();
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
                        Log.w("POST report SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        issue_edit_text.setText("");
                        Toast.makeText(getApplicationContext(),reportIssueSuccessToast,Toast.LENGTH_LONG).show();
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

        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }
}
