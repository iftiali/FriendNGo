package com.friendngo.friendngo.friendngo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class ChatRoomActivity extends AppCompatActivity {
    TextView chat_room_Canecl,chat_room_Activity_name;
    ImageView chat_room_three_dots;
    String receiverName = null;
    private static final int POLLING_PERIOD = 5;
    private Button chatSendButton;
    private EditText chatEditMsg;
    private ChatRoomAdapter adapter;
    private ArrayList<ChatRoomModel> chatHistory;
    private ListView messagesContainer;
    String activityID = null;
    String activityName = null;
    String receiverId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        chatSendButton = (Button) findViewById(R.id.chatSendButton);
        chatEditMsg =(EditText) findViewById(R.id.messageEdit);
        chat_room_Activity_name = (TextView)findViewById(R.id.my_activity_text_view_name);
        chat_room_three_dots = (ImageView)findViewById(R.id.my_activity_dots);
        chat_room_Canecl = (TextView)findViewById(R.id.my_activity_text_view_cancel);
         activityID = getIntent().getExtras().getString("activityID");
         activityName= getIntent().getExtras().getString("activityName");
         receiverId = MapActivity.selfIdentify;
         receiverName = MapActivity.selfName;
        Log.w("activityID",activityID +":"+activityName+":"+receiverId+":"+receiverName);
        chat_room_Activity_name.setText(activityName);
        chat_room_Canecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatRoomActivity.this.finish();
            }
        });
        //Here is where we schedule the polling of our chats
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

            //This happens in a seperate thread
            public void run() {
                //Now hop back onto main thread to do the actual work
                runOnUiThread(new Runnable() {
                    //death crash
                    @Override
                    public void run() {
                        //death crash
                        append_chat_conversation();
                    }
                });
            }
        }, 0, POLLING_PERIOD, TimeUnit.SECONDS);
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = chatEditMsg.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }else {

                ChatRoomModel chatMessage = new ChatRoomModel();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                //chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                chatEditMsg.setText("");

                displayMessage(chatMessage);
                //POST Location
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }
                RequestParams params = new RequestParams();
                params.put("sender", receiverId);
                params.put("activity_id", activityID);
                params.put("message",messageText );
                    client.post(MainActivity.base_host_url + "api/postActivityMessage/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            Log.w("POST MESSAGE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST MESSAGE SUCCESS2", statusCode + ": " + timeline.toString());
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Log.w("POST MESSAGE RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST MESSAGE FAIL", "Error Code: " + error_code + "," + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                            Log.w("MY MESSAGE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                        }
                    });
                }
            }
        });
    }
    private void append_chat_conversation() {
        chatHistory = new ArrayList<ChatRoomModel>();

        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }

        client.get(MainActivity.base_host_url + "api/getActivityMessages/"+activityID, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.w("GET CHAT ROOM SUCCESS", statusCode + ": " + "Response = " + response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray chatJsonArray) {
                chatHistory.clear();
                Log.w("GET CHAT ROOM SUCCESS-2", statusCode + ": " + chatJsonArray.toString());

                for (int i =0; i < chatJsonArray.length(); i++){
                    ChatRoomModel msg = new ChatRoomModel();

                    try {

                        JSONObject chatJSONObject = chatJsonArray.getJSONObject(i);
                      String senderID =  chatJSONObject.getString("sender");
                        //Log.w("Hello",senderID+":"+activityID);

                            if(senderID.equals(receiverId)){
                                msg.setId(1);
                                msg.setMe(true);
                                //msg.setMessage(chatJSONObject.getString("sender_name")+":"+chatJSONObject.getString("message"));
                                msg.setMessage(chatJSONObject.getString("message"));

                            }else{
                                msg.setId(1);
                                msg.setMe(false);
                                msg.setMessage(chatJSONObject.getString("sender_name")+":\n\n"+chatJSONObject.getString("message"));
                            }
                        chatHistory.add(msg);
                    }catch (Exception e){

                    }

                }
                adapter = new ChatRoomAdapter(ChatRoomActivity.this, new ArrayList<ChatRoomModel>());
                messagesContainer.setAdapter(adapter);
                for(int i=0; i<chatHistory.size(); i++) {
                    ChatRoomModel message = chatHistory.get(i);
                    displayMessage(message);
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.w("GET CHAT ROOM RETRY", "" + retryNo);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET CHAT ROOM FAIL", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){
                Log.w("GET CHAT ROOM FAIL", "Error Code: " + error_code + ",  " + json.toString());
            }
        });

    }
    public void displayMessage(ChatRoomModel message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }
    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
}
