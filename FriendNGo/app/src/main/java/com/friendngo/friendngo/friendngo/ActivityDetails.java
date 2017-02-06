package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityDetails extends AppCompatActivity {

    Button sendRequestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sendRequestButton = (Button)findViewById(R.id.send_request_button);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDetails.this.finish();
            }
        });

    }
}
