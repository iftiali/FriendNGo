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

import org.w3c.dom.Text;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


public class SignUp extends AppCompatActivity {

    private TextView textView;
    private Button signupButton;

    private EditText emailEditTextValue;
    private EditText passwordEditTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Sets the top bar text
        getSupportActionBar().setTitle("Create an account");

        //Creates a code instance of the buttons and text inputs
        textView = (TextView) findViewById(R.id.account_link);
        emailEditTextValue = (EditText) findViewById(R.id.signup_email);
        passwordEditTextValue = (EditText) findViewById(R.id.signup_password);
        signupButton = (Button) findViewById(R.id.signup_button);

        //Sets the callback for when the user presses the I have an account link
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent mainIntent = new Intent(SignUp.this,SignIn.class);
                SignUp.this.startActivity(mainIntent);
                SignUp.this.finish();
                return false;
            }
        });

        //Sets the callback for when the user submits his username and password
        signupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                AsyncHttpClient client = new AsyncHttpClient();
//                client.setBasicAuth(emailEditTextValue.getText().toString(),passwordEditTextValue.getText().toString());

                RequestParams params = new RequestParams();
//                params.setUseJsonStreamer(true);
                params.put("username", emailEditTextValue.getText().toString());
                params.put("password", passwordEditTextValue.getText().toString());


//                client.post("http://requestb.in/s3zx6as4", params, new AsyncHttpResponseHandler() {

                client.post("http://54.175.1.158:8000/users/register/", params, new AsyncHttpResponseHandler() {

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
