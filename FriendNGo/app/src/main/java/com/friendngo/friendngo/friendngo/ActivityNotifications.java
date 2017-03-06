package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityNotifications extends AppCompatActivity {
    ListView list;
    ArrayList<RequestModel> dataModels;
    BottomNavigationView bottomNavigationView;
    private static ActivityRequestListAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_request_activity);
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_icon:
                             //   Intent intent = new Intent(ActivityNotifications.this, MapActivity.class);
                              //  ActivityNotifications.this.startActivity(intent);
                                ActivityNotifications.this.finish();
                                break;
                            case R.id.calendar_icon:
                                Log.w("BOTTOM NAV","Calendar Icon Pressed");
                                break;
                            case R.id.notification_icon:
                                Log.w("BOTTOM NAV","Notifications Icon Pressed");
                                Intent intentRequest = new Intent(ActivityNotifications.this, ActivityNotifications.class);
                                ActivityNotifications.this.startActivity(intentRequest);
                                ActivityNotifications.this.finish();
                                break;
                            case R.id.message_icon:
                                Log.w("BOTTOM NAV","Message Icon Pressed");
                                break;
                            case R.id.settings_icon:
                                Log.w("NAV DEBUG", "Settings Icon Pressed");
                                break;
                            default:
                                Log.w("NAV DEBUG", "Default called on nav switch... what on earth are you doing???");
                                break;
                        }
                        return true;
                    }
                });

        //list of request
        list=(ListView)this.findViewById(R.id.request_list);
        dataModels= new ArrayList<>();
        adapter= new ActivityRequestListAdapter(dataModels,getApplicationContext());
        list.setAdapter(adapter);

        //GET ActivityRequests
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }

        //GET ActivityRequests
        client.get(MainActivity.base_host_url + "api/getActivityRequests/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET REQUEST SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray requests) {
                Log.w("GET AR JSON ARRAY", statusCode + ": " + requests.toString());
                for (int i = 0; i < requests.length(); i++) {
                    try {
                        JSONObject activity = requests.getJSONObject(i);
                        dataModels.add(new RequestModel(
                                activity.getLong("sender"),
                                activity.getString("sender_profile"),
                                activity.getString("sender_name"),
                                activity.getInt("request_state"),
                                activity.getInt("sender_age"),
                                activity.getString("sender_home_city"),
                                activity.getString("sender_home_nationality"),
                                activity.getString("sender_points")));
                    } catch (JSONException e) {
                        Log.w("GET REQUEST FAIL1: ", e.getMessage().toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                Log.w("GET REQUEST FAILURE2:", "Error Code: " + error_code + ",  " + text);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RequestModel dataModel= dataModels.get(position);

                /*Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
            }
        });
    }

}
