package com.friendngo.friendngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PointsHistoryActivity extends AppCompatActivity {
    private Button pointHistorySubmit;
    private List<pointHistoryModel> pointHistorylist = new ArrayList<>();
    private String checkOnlineToast = null;
    private PointHistoryAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set internet message.
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        //Init xml
        initXmlView();
        //submit button activity finish.
        pointHistorySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PointsSystemActivity.class);
                startActivity(intent);

            }
        });
        //Set Recycle view with adapter
        setRecyclerView();
        //call api
        getApiGetPointsHistory();
    }
    public void initXmlView(){
        recyclerView = (RecyclerView) findViewById(R.id.points_history_recycler_view);
        pointHistorySubmit = (Button) findViewById(R.id.points_history_save_button);
        mAdapter = new PointHistoryAdapter(pointHistorylist,getApplicationContext());
    }
    private void getApiGetPointsHistory() {
        if(ValidationClass.checkOnline(getApplicationContext())){
            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {
                client.addHeader("Authorization", "Token " + SignIn.static_token);
            }
            Log.d("UserID",Integer.parseInt(MapActivity.selfIdentify)+"");
            client.get(MainActivity.base_host_url + "api/getPoints", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.w("GET POINTS SUCCESS", statusCode + ": " + "Response = " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray pointJsonArray) {

                    Log.w("GET POINTS SUCCESS-2", statusCode + ": " + pointJsonArray.toString());
                    for (int i =0; i < pointJsonArray.length(); i++) {
                        pointHistoryModel pointHistoryModel = null;
                        try {
                            JSONObject pointJSONObject = pointJsonArray.getJSONObject(i);
                            pointHistoryModel = new pointHistoryModel(
                                    pointJSONObject.getString("points"),
                                    pointJSONObject.getString("awarded_text"),
                                    pointJSONObject.getString("created_ago")
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pointHistorylist.add(pointHistoryModel);
                    }

                     mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("GET POINTS RETRY", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("GET POINTS FAIL", "Error Code: " + error_code + "," + text);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                    Log.w("GET POINTS FAIL", "Error Code: " + error_code + ",  " + json.toString());
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }
    public void setRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
