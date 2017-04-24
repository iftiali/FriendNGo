package com.friendngo.friendngo;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private ListView list;
    private String checkOnlineToast = null;
    private ArrayList<RequestModel> dataModels;
    private BottomNavigationView bottomNavigationView;
    private static ActivityRequestListAdapter adapter;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        //set views
        initXmlView(view);
        //set Toast message
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        //set Model
        dataModels= new ArrayList<>();
        //set adapter
        adapter= new ActivityRequestListAdapter(dataModels,getApplicationContext());
        list.setAdapter(adapter);
        //call Api getActivityRequest
        getApiActivityRequests();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RequestModel dataModel= dataModels.get(position);
            }
        });
        return view;
    }
    public void initXmlView(View view){
        list=(ListView)view.findViewById(R.id.request_list);
        //Set text message whenever it gets empty list
        list.setEmptyView(view.findViewById(R.id.emptyElement));
    }

    private void getApiActivityRequests(){
        if(ValidationClass.checkOnline(getApplicationContext())) {
            //GET ActivityRequests
            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {
                client.addHeader("Authorization", "Token " + SignIn.static_token);
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
                                    activity.getString("activity_name"),
                                    activity.getInt("pk")));
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
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("GET REQUEST FAILURE2:", "Error Code: " + error_code + ",  " + text);
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }

}
