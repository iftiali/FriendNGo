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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SpecialGroup extends AppCompatActivity {
    private ListView list;
    private TextView buttonCancel;
    private String checkOnlineToast = null;
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

    }
    private void getPromotionCodes() {
        //set Model
        dataModels = new ArrayList<SpecialGroupModel>();
        Log.d("Hello1","get promotion code");
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {

            Log.d("Hello1","get promotion code 2");
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
                         Log.d("Hello",specialGroupName);
                         spm.setGroupName(specialGroupName);
                        dataModels.add(spm);
                    } catch (Exception e) {

                    }
                }
                adapter=new SpecialGroupAdapter(SpecialGroup.this, dataModels);
                list.setAdapter( adapter );
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


    }
}
