package com.friendngo.scott.friendngo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NewCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

        //Sets the top bar text
        getSupportActionBar().setTitle("New City");

        String token = SignIn.token;
        Log.w("TOKEN TEST", token);
    }
}
