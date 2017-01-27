package com.friendngo.friendngo.friendngo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhoAreYou extends AppCompatActivity {

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);
        getSupportActionBar().setTitle("Who Are you?");

        continueButton = (Button)findViewById(R.id.profile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  //TODO: Make HTTP call to update the user's profile
                                                  WhoAreYou.this.finish();
                                              }
                                          }
        );
    }
}
