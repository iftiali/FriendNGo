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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityNotifications extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
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
                                Intent intent = new Intent(ActivityNotifications.this, MapActivity.class);
                                ActivityNotifications.this.startActivity(intent);
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
    }

}
