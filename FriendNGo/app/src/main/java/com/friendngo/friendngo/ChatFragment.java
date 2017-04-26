package com.friendngo.friendngo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private List<ChatListModel> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatListAdapter mAdapter;
    private TextView emptyView;
    private String checkOnlineToast = null;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Init xml
        initXmlView(view);
        //Set Recycle view with adapter
        setRecyclerView();
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        getApiGetMessage();
        return view;

    }

    private void getApiGetMessage() {
        if(ValidationClass.checkOnline(getApplicationContext())){
            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {
                client.addHeader("Authorization", "Token " + SignIn.static_token);
            }
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            client.get(MainActivity.base_host_url + "api/getMessages/", new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.w("GET MESSAGES SUCCESS", statusCode + ": " + "Response = " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {

                    Log.w("GET MESSAGES SUCCESS-2", statusCode + ": " + chatJsonArray.toString());
                    for (int i =0; i < chatJsonArray.length(); i++){
                        try {
                            JSONObject chatJSONObject = chatJsonArray.getJSONObject(i);
                            ChatListModel chatModel = new ChatListModel(
                                    chatJSONObject.getString("category_name"),
                                    chatJSONObject.getString("activity_name"),
                                    chatJSONObject.getString("sender_name"),
                                    chatJSONObject.getString("message"),
                                    chatJSONObject.getString("created_ago"),
                                    chatJSONObject.getString("activity_id")

                            );
                            if(!chatList.isEmpty()){

                                for(int zz =0;zz<chatList.size();zz++){
                                    if(chatList.get(zz).getactivityID().equals( chatJSONObject.getString("activity_id"))){
                                        Log.i("No need to add","No need to add");
                                        chatList.remove(zz);
                                        chatList.add(chatModel);
                                    }else{
                                        chatList.add(chatModel);
                                    }
                                }

                                //chatList.add(chatModel);
                            }else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                                chatList.add(chatModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.w("GET MESSAGES RETRY", "" + retryNo);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                    Log.w("GET MESSAGES FAIL", "Error Code: " + error_code + "," + text);
                }

                @Override
                public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                    Log.w("GET MESSAGES FAIL", "Error Code: " + error_code + ",  " + json.toString());
                }
            });

        }else{
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }


    public void initXmlView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        mAdapter = new ChatListAdapter(chatList,getApplicationContext());
    }
    public void setRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
