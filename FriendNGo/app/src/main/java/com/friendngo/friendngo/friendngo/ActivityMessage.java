package com.friendngo.friendngo.friendngo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ActivityMessage extends AppCompatActivity {
    public static String user_Id=null;
    public static String user_Name=null;
    private List<ChatListModel> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ChatListAdapter(chatList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);


        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }



        client.get(MainActivity.base_host_url + "api/getMessages/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET MESSAGES SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {

                Log.w("GET MESSAGES SUCCESS-2", statusCode + ": " + chatJsonArray.toString());
                for (int i =0; i < chatJsonArray.length(); i++){
                    try {
                        JSONObject chatJSONObject = chatJsonArray.getJSONObject(i);
                        ChatListModel chatModel = new ChatListModel(
                               chatJSONObject.getString("category_name"),
                                chatJSONObject.getString("activity_name"),
                                chatJSONObject.getString("sender_name"),
                                chatJSONObject.getString("message"),
                                chatJSONObject.getString("created_ago"),
                                chatJSONObject.getString("activity_id")

                        );
                     if(!chatList.isEmpty()){
                         for(int zz =0;zz<chatList.size();zz++){
                             if(chatList.get(zz).getactivityID().equals( chatJSONObject.getString("activity_id"))){
                                 Log.i("No need to add","No need to add");
                                 chatList.remove(zz);
                                 chatList.add(chatModel);
                             }else{
                                 chatList.add(chatModel);
                             }
                         }
                     }else {
                        chatList.add(chatModel);
                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET MESSAGES RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET MESSAGES FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("GET MESSAGES FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });




        client.get(MainActivity.base_host_url + "api/getSelfIdentity", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET identity SUCCESS", statusCode + ": " + "Response = " + response.toString());

                try {
                  user_Id = response.getString("id");
                  user_Name = response.getString("first_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {

                Log.w("GET identity SUCCESS-2", statusCode + ": " + chatJsonArray.toString());


            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET idetity  RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET identity FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("GET identity FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });

    }

}