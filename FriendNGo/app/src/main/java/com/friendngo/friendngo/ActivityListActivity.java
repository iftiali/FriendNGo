package com.friendngo.friendngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityListActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public ListView listView;
    private static ActivityListAdapter adapter;
    private static final int POLLING_PERIOD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new ActivityListAdapter(getApplicationContext());

        //TODO: Move ListView Code to it's own activity
        listView = (ListView) findViewById(R.id.activity_list);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_request_activity);

        if (listView == null) {
            Log.w("LIST VIEW ERROR", "List view is null!");
        } else {
            listView.setAdapter(adapter);

            //Here is where we schedule the polling of our activities
            ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                //This happens in a seperate thread
                public void run() {
                    //Now hop back onto main thread to do the actual work
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            update_activities();
                        }
                    });
                }
            }, 0, POLLING_PERIOD, TimeUnit.SECONDS);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_icon:
                                //Intent intent = new Intent(ActivityListActivity.this, MapActivity.class);
                                //ActivityListActivity.this.startActivity(intent);
                                ActivityListActivity.this.finish();
                                break;
                            case R.id.calendar_icon:
                                Log.w("BOTTOM NAV","Calendar Icon Pressed");
                                break;
                            case R.id.notification_icon:
                                Log.w("BOTTOM NAV","Notifications Icon Pressed");
                                Intent intentRequest = new Intent(ActivityListActivity.this, ActivityNotifications.class);
                                ActivityListActivity.this.startActivity(intentRequest);
                                ActivityListActivity.this.finish();
                                break;
                            case R.id.message_icon:
                                Intent intent = new Intent(ActivityListActivity.this, ActivityMessage.class);
                                ActivityListActivity.this.startActivity(intent);
                                ActivityListActivity.this.finish();
                                break;
                            case R.id.settings_icon:
                                Intent intentSetting = new Intent(ActivityListActivity.this, ReportIssue.class);
                                ActivityListActivity.this.startActivity(intentSetting);
                                ActivityListActivity.this.finish();
                                break;
                            default:
                                Log.w("NAV DEBUG", "Default called on nav switch... what on earth are you doing???");
                                break;
                        }
                        return true;
                    }
                });
        //TODO: Move this to seperate ListView Activity
//      ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

}
