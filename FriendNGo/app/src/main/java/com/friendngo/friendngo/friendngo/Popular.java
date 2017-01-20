package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class Popular extends AppCompatActivity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creates fullscreen effect for older phones -> before setContentView()
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_popular);

        //Creates fullscreen effect for newer phones
        if (Build.VERSION.SDK_INT >= 16) {

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
//            getSupportActionBar().hide();
        }

        imageButton = (ImageButton) findViewById(R.id.staticImage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Popular.this,MapActivity.class);
                Popular.this.startActivity(intent);
                Popular.this.finish();
            }
            });

    }
}
