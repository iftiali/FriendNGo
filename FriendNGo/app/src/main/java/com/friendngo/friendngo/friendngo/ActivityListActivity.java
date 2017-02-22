package com.friendngo.friendngo.friendngo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityListActivity extends AppCompatActivity {

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

        //TODO: Move this to seperate ListView Activity
//      ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

}
