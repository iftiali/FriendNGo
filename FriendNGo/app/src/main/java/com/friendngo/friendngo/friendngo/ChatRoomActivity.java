package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChatRoomActivity extends AppCompatActivity {
TextView chat_room_Canecl,chat_room_Activity_name;
    ImageView chat_room_three_dots;
    String receiverName = null;
    String activityID = null;
    String activityName = null;
    String receiverId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        chat_room_Activity_name = (TextView)findViewById(R.id.my_activity_text_view_name);
        chat_room_three_dots = (ImageView)findViewById(R.id.my_activity_dots);
        chat_room_Canecl = (TextView)findViewById(R.id.my_activity_text_view_cancel);
         activityID = getIntent().getExtras().getString("activityID");
         activityName= getIntent().getExtras().getString("activityName");
         receiverId = ActivityMessage.user_Id;
         receiverName = ActivityMessage.user_Name;
        Log.w("activityID",activityID +":"+activityName+":"+receiverId+":"+receiverName);
        chat_room_Activity_name.setText(activityName);
        chat_room_Canecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatRoomActivity.this.finish();
            }
        });
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }



        client.get(MainActivity.base_host_url + "api/getActivityMessages/"+activityID, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET CHAT ROOM SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {

                Log.w("GET CHAT ROOM SUCCESS-2", statusCode + ": " + chatJsonArray.toString());


            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET CHAT ROOM RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET CHAT ROOM FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("GET CHAT ROOM FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });
    }

}
