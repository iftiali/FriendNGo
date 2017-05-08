package com.friendngo.friendngo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.PrivateKeyDetails;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Recommend extends AppCompatActivity {
    private ImageView recommend_dot_one_button;
    private ImageView recommend_dot_two_button;
    private ImageView recommend_dot_three_button;
    private ImageView recommend_dot_four_button;
    private ImageView recommend_dot_five_button;
    private ImageView recommend_dot_six_button;
    private ImageView recommend_dot_seven_button;
    private ImageView recommend_dot_eight_button;
    private ImageView recommend_dot_nine_button;
    private ImageView recommend_dot_ten_button;
    private Button submit_Button;
    private String checkOnlineToast = null;
    private int recommendNumber =0;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        initXmlView();

        recommend_dot_one_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber =1;
                recommend_dot_one_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);

            }
        });
        recommend_dot_two_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 2;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_three_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber=3;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_four_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber =4;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_five_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 5;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_six_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 6;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_seven_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 7;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_eight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 8;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_nine_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 9;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_selected);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_rating);
            }
        });
        recommend_dot_ten_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendNumber = 10;
                recommend_dot_one_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_two_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_three_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_four_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_five_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_six_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_seven_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_eight_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_nine_button.setImageResource(R.drawable.dot_rating);
                recommend_dot_ten_button.setImageResource(R.drawable.dot_selected);
            }
        });
        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recommend.this.finish();
                postApiRecommend();
            }
        });
    }

    private void postApiRecommend() {
        if(ValidationClass.checkOnline(getApplicationContext())){
            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {
                client.addHeader("Authorization", "Token " + SignIn.static_token);
            }

            RequestParams params = new RequestParams();
            params.put("score", recommendNumber);

            client.post(MainActivity.base_host_url + "api/postPromoterScore/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.w("POST report SUCCESS", statusCode + ": " + "Response = " + response.toString());

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.w("POST report SUCCESS3", statusCode + ": " + response.toString());
                }

                @Override
                public void onRetry(int retryNo) {
                    Log.w("POST report RETRY", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("POST report FAILURE2", "Error Code: " + error_code + text);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.w("POST report FAILURE", String.valueOf(statusCode) + errorResponse.toString());
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }

    }

    private void initXmlView() {
        recommend_dot_one_button = (ImageView)findViewById(R.id.recommend_dot_one);
        recommend_dot_two_button = (ImageView)findViewById(R.id.recommend_dot_two);
        recommend_dot_three_button = (ImageView)findViewById(R.id.recommend_dot_three);
        recommend_dot_four_button = (ImageView)findViewById(R.id.recommend_dot_four);
        recommend_dot_five_button = (ImageView)findViewById(R.id.recommend_dot_five);
        recommend_dot_six_button = (ImageView)findViewById(R.id.recommend_dot_six);
        recommend_dot_seven_button = (ImageView)findViewById(R.id.recommend_dot_seven);
        recommend_dot_eight_button = (ImageView)findViewById(R.id.recommend_dot_eight);
        recommend_dot_nine_button = (ImageView)findViewById(R.id.recommend_dot_nine);
        recommend_dot_ten_button = (ImageView)findViewById(R.id.recommend_dot_ten);
        submit_Button = (Button)findViewById(R.id.recommend_continue_button);

    }


}
