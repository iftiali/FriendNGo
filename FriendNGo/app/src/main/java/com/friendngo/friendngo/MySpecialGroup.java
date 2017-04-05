package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MySpecialGroup extends AppCompatActivity {

    private Button next_button;
    private TextView no_code_text;
    private EditText code_edit_text;
    public static String PROFILE_CREATED_PREFERENCE = "profile_created";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_special_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        next_button = (Button) findViewById(R.id.code_next_button);
        no_code_text = (TextView) findViewById(R.id.no_code_text);
        code_edit_text = (EditText) findViewById(R.id.code_edit_text);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                if(SignIn.static_token != null) {
                    client.addHeader("Authorization","Token "+SignIn.static_token);
                }

                RequestParams params = new RequestParams();

                String code = code_edit_text.getText().toString();
                params.put("code_received",code);

                client.post(MainActivity.base_host_url + "api/postPromotionCode/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(MySpecialGroup.this, "Code Accepted!", Toast.LENGTH_LONG).show();
                        Log.w("POST CODE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        finish();
                        //TODO: Create A Flag Here
//                        SharedPreferences.Editor editor = getSharedPreferences(PROFILE_CREATED_PREFERENCE, MODE_PRIVATE).edit();
//                        editor.putString("is_profile_created","true");
//                        editor.commit();
                        MainActivity.new_user = false;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.w("POST CODE SUCCESS3", statusCode + ": " + response.toString());
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        Log.w("POST CODE RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Toast.makeText(MySpecialGroup.this, "Code Is Not Valid", Toast.LENGTH_LONG).show();
                        Log.w("POST CODE FAILURE2", "Error Code: " + error_code + text);
                    }
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(MySpecialGroup.this, "Code Is Not Valid!", Toast.LENGTH_LONG).show();
                        Log.w("POST CODE FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                    }
                });
            }
        });

        no_code_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //check edit profile path
                SharedPreferences pref = getSharedPreferences("EditPath", MODE_PRIVATE); // 0 - for private mode
                String editprofilePath= pref.getString("edit_path", null);
                if(editprofilePath == null){

                        if( MainActivity.new_user = true){
                            Log.d("State", String.valueOf(MainActivity.new_user));
                            Intent intent = new Intent(getApplicationContext(),WhatDoYouWantToDoToday.class);
                            MySpecialGroup.this.startActivity(intent);
                            MySpecialGroup.this.finish();
                            SharedPreferences.Editor editor = getSharedPreferences(PROFILE_CREATED_PREFERENCE, MODE_PRIVATE).edit();
                            editor.putString("is_profile_created","true");

                            editor.commit();
                        }
                        else {
                            finish();
                            //TODO: Create A Flag Here
                            SharedPreferences.Editor editor = getSharedPreferences(PROFILE_CREATED_PREFERENCE, MODE_PRIVATE).edit();
                            editor.putString("is_profile_created", "true");
                            editor.commit();
                        }


                    //remove edit profile path

                }else {
                        SharedPreferences editPofileSharedPreferences = getApplicationContext().getSharedPreferences("EditPath", 0);
                        SharedPreferences.Editor editPofileEditor = editPofileSharedPreferences.edit();
                        editPofileEditor.clear();
                        editPofileEditor.commit();
                        Log.d("True",editprofilePath);
                        MySpecialGroup.this.finish();
                        SharedPreferences.Editor editor = getSharedPreferences(PROFILE_CREATED_PREFERENCE, MODE_PRIVATE).edit();
                        editor.putString("is_profile_created", "true");
                        editor.commit();
                    }

                return false;
            }
        });



    }



}
