package com.friendngo.friendngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ActivityMessage extends AppCompatActivity {
    private List<ChatListModel> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatListAdapter mAdapter;
    private TextView emptyView;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mAdapter = new ChatListAdapter(chatList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
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
                        recyclerView.setVisibility(View.VISIBLE);
                         emptyView.setVisibility(View.GONE);
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

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_request_activity);
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_icon:
                                //   Intent intent = new Intent(ActivityNotifications.this, MapActivity.class);
                                //  ActivityNotifications.this.startActivity(intent);
                                ActivityMessage.this.finish();
                                break;
                            case R.id.calendar_icon:
                                Toast.makeText(getApplicationContext(), "Calarndar Not Available in Beta", Toast.LENGTH_LONG).show();
                                Log.w("BOTTOM NAV","Calendar Icon Pressed");
                                break;
                            case R.id.notification_icon:
                                Log.w("BOTTOM NAV","Notifications Icon Pressed");
                                Intent intentRequest = new Intent(ActivityMessage.this, ActivityNotifications.class);
                                ActivityMessage.this.startActivity(intentRequest);
                                ActivityMessage.this.finish();
                                break;
                            case R.id.message_icon:
//                                Intent intent = new Intent(ActivityMessage.this, ActivityMessage.class);
//                                ActivityMessage.this.startActivity(intent);
//                                ActivityMessage.this.finish();
                                break;
                            case R.id.settings_icon:
//                                Toast.makeText(getApplicationContext(), "Settings Not Available in Beta", Toast.LENGTH_LONG).show();
//                                Log.w("NAV DEBUG", "Settings Icon Pressed");
                                Intent intentSetting = new Intent(ActivityMessage.this, ReportIssue.class);
                                ActivityMessage.this.startActivity(intentSetting);
                                ActivityMessage.this.finish();
                                break;
                            default:
                                Log.w("NAV DEBUG", "Default called on nav switch... what on earth are you doing???");
                                break;
                        }
                        return true;
                    }
                });
    }

}
