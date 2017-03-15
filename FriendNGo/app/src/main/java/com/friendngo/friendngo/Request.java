package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Request extends AppCompatActivity {

    TextView cancelBtn;
    ListView list;
    ArrayList<RequestModel> dataModels;
    ArrayList<RequestModel> URLdataModels;
    private static ActivityRequestListAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cancelBtn = (TextView)findViewById(R.id.request_cancel_text_view);
        list=(ListView)this.findViewById(R.id.request_list);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Request.this,ActivityDetails.class);
                Request.this.startActivity(intent);
                Request.this.finish();
            }
        });
        dataModels= new ArrayList<>();
        adapter= new ActivityRequestListAdapter(dataModels,getApplicationContext());
        list.setAdapter(adapter);

        //GET ActivityRequests
        AsyncHttpClient client = new AsyncHttpClient();
        if(SignIn.static_token != null) {
            client.addHeader("Authorization","Token "+SignIn.static_token);
        }

        //GET ActivityRequests
        client.get(MainActivity.base_host_url + "api/getActivityRequests/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET REQUEST SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray requests) {
                Log.w("GET AR JSON ARRAY", statusCode + ": " + requests.toString());
                    for (int i = 0; i < requests.length(); i++) {
                        try {
                            JSONObject activity = requests.getJSONObject(i);
                            dataModels.add(new RequestModel(
                                    activity.getLong("sender"),
                                    activity.getString("sender_profile"),
                                    activity.getString("sender_name"),
                                    activity.getInt("request_state"),
                                    activity.getInt("sender_age"),
                                    activity.getString("sender_home_city"),
                                    activity.getString("sender_home_nationality"),
                                    activity.getString("sender_points"),
                                    activity.getInt("id")));
                        } catch (JSONException e) {
                            Log.w("GET REQUEST FAIL1: ", e.getMessage().toString());
                        }
                    }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable){
                Log.w("GET REQUEST FAILURE2:", "Error Code: " + error_code + ",  " + text);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RequestModel dataModel= dataModels.get(position);

                /*Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
            }
        });


        //TODO: Click should not disable the banner when you click on the banner.


    }

}
