package com.friendngo.scott.friendngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignIn extends AppCompatActivity {

    private TextView textView;
    private Button signinButton;

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Sets the top heading value
        getSupportActionBar().setTitle("Sign In");

        //Creates a code instance of the buttons and text input fields
        textView = (TextView) findViewById(R.id.create_account_link);
        emailEditTextValue = (EditText) findViewById(R.id.login_email);
        passwordEditTextValue = (EditText) findViewById(R.id.login_password);
        signinButton = (Button) findViewById(R.id.signin_button);

        //Sets the callback for when a user presseses the create account button
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent mainIntent = new Intent(SignIn.this,SignUp.class);
                SignIn.this.startActivity(mainIntent);
                SignIn.this.finish();
                return false;
            }
        });

        //Sets the callback for when a user submits the username and password form
        signinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                AsyncHttpClient client = new AsyncHttpClient();
                client.setBasicAuth(emailEditTextValue.getText().toString(),passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
                params.setUseJsonStreamer(true);
                params.put("email", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());

                client.post("http://requestb.in/s3zx6as4", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Log.w("HTTP SUCCESS: ", response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.w("HTTP FAIL: ", ""+statusCode);
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });
    }
}
