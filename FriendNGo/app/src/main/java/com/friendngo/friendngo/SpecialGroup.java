package com.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SpecialGroup extends AppCompatActivity {
    private ListView list;
    private TextView buttonCancel;
    private String checkOnlineToast = null;
    private String empltyFiledToast = null;
    private String codeAacceptedToast = null;
    private String codeNotAacceptedToast = null;
    private Button save_button;
    private EditText code_edit_text;
    private ArrayList<SpecialGroupModel> dataModels;
    private SpecialGroupAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //check Online
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        empltyFiledToast = getResources().getString(R.string.Special_Group_Empty_Toast);
        codeAacceptedToast = getResources().getString(R.string.Special_Group_code_accepted_Toast);
        codeNotAacceptedToast = getResources().getString(R.string.Special_Group_code_accepted_invalid_Toast);
        code_edit_text = (EditText)findViewById(R.id.Special_Group_code_edit_text);
        save_button = (Button)findViewById(R.id.Special_Group_code_next_button);
        list = (ListView)findViewById(R.id.special_group_list);


        //call Api getActivityRequest
        getPromotionCodes();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SpecialGroupModel dataModel= dataModels.get(position);
            }
        });

        buttonCancel = (TextView)findViewById(R.id.special_cancel_text_view);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialGroup.this.finish();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

                RequestParams params = new RequestParams();

                String code = code_edit_text.getText().toString();
                params.put("code_received", code);
                if(code_edit_text.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), empltyFiledToast, Toast.LENGTH_LONG).show();
                 }else {
                    client.post(MainActivity.base_host_url + "api/postPromotionCode/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Code Accepted!", Toast.LENGTH_LONG).show();
                            Log.w("POST CODE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            getPromotionCodes();

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.w("POST CODE SUCCESS-2", statusCode + ": " + response.toString());
                            getPromotionCodes();
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Log.w("POST CODE RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Code Is Not Valid", Toast.LENGTH_LONG).show();
                            Log.w("POST CODE FAILURE2", "Error Code: " + error_code + text);
                        }

                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(getApplicationContext(), "Code Is Not Valid!", Toast.LENGTH_LONG).show();
                            Log.w("POST CODE FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                        }
                    });
                }
            }
        });
    }
    private void getPromotionCodes() {
        //set Model
        dataModels = new ArrayList<SpecialGroupModel>();
        Log.d("Hello1","get promotion code");
        if(ValidationClass.checkOnline(getApplicationContext())) {

            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {

                Log.d("Hello1", "get promotion code 2");
                client.addHeader("Authorization", "Token " + SignIn.static_token);
            } else {

                Log.w("TOKEN ERROR", "What happened to the token :(");
            }
            client.get(MainActivity.base_host_url + "api/getPromotionCodes/goes", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.w("GET PROMOTION SUCCESS", statusCode + ": " + "Response = " + response.toString());


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                    Log.w("GET PROMOTION SUCCESS 2", statusCode + ": " + "Response = " + responseArray.toString());
                    dataModels.clear();
                    for (int i = 0; i < responseArray.length(); i++) {
                        final SpecialGroupModel spm = new SpecialGroupModel();
                        try {

                            JSONObject chatJSONObject = responseArray.getJSONObject(i);
                            String specialGroupName = chatJSONObject.getString("code_received");
                            spm.setGroupName(specialGroupName);
                            dataModels.add(spm);
                        } catch (Exception e) {

                        }
                    }
                    adapter = new SpecialGroupAdapter(SpecialGroup.this, dataModels);
                    list.setAdapter(adapter);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("GET PROMOTION RETRY", "TRYING AGAIN");
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    //death crash
                    Log.w("GET PROMOTION FAIL2", "Error Code: " + error_code + ", Text: " + text);
                }

                //death crash
                @Override
                public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                    if (json != null) {
                        Log.w("MY PROMOTION FAIL", "Error Code: " + error_code + ",  " + json.toString());
                    }
                }
            });
        }else {
                Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }

    }
}
