package com.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ActivityRequestListAdapter extends ArrayAdapter<RequestModel> implements View.OnClickListener{
    private ArrayList<RequestModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        CircularImageView profilePicture;
        TextView name;
        TextView years;
        TextView homeCity;
        TextView request_point;
        TextView points;
        TextView activityName;
        TextView resident;
        ImageView imageFlagOne;
        ImageView imageFlagTwo;
        ImageView imageFlagThree;
        TextView dot_text;
        CircularImageView notImageButton,yesImageButton;
        long sender_id;
        long request_id;
    }

    public ActivityRequestListAdapter(ArrayList<RequestModel> data, Context context) {
        super(context, R.layout.activity_request_list_row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        RequestModel dataModel=(RequestModel) object;
        switch (v.getId())
        {
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final RequestModel dataModel = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_request_list_row_item, parent, false);
            viewHolder.profilePicture = (CircularImageView) convertView.findViewById(R.id.messages_profile_picture);
            viewHolder.name = (TextView) convertView.findViewById(R.id.request_person_name);
            viewHolder.request_point = (TextView)convertView.findViewById(R.id.request_point);
            viewHolder.activityName = (TextView)convertView.findViewById(R.id.request_activity_name);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.request_city_name);
            viewHolder.years = (TextView)convertView.findViewById(R.id.request_years);
            viewHolder.points = (TextView) convertView.findViewById(R.id.request_point);
            viewHolder.notImageButton =(CircularImageView)convertView.findViewById(R.id.request_delete_image);
            viewHolder.yesImageButton = (CircularImageView)convertView.findViewById(R.id.request_success_image);
            viewHolder.dot_text = (TextView)convertView.findViewById(R.id.request_home_city_dot);
            viewHolder.imageFlagOne = (ImageView)convertView.findViewById(R.id.request_country_flag_one);
            viewHolder.imageFlagTwo = (ImageView)convertView.findViewById(R.id.request_country_flag_two);
            viewHolder.imageFlagThree = (ImageView)convertView.findViewById(R.id.request_country_flag_three);

            //viewHolder.nationality.setImageResource(R.drawable.canada);
            viewHolder.resident = (TextView)convertView.findViewById(R.id.request_resident);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;

        //GET The image file at the pictureURL
        AsyncHttpClient client = new AsyncHttpClient();

        //TODO: Change this to get the picture of the SENDER of the request
        String pictureURL = dataModel.getProfileImage();
        final ImageView profilePic = (CircularImageView) convertView.findViewById(R.id.profilepicture);
        Picasso.with(mContext).load(MainActivity.base_host_url + pictureURL).into(profilePic);

        viewHolder.notImageButton.setImageResource(R.drawable.not);
        viewHolder.yesImageButton.setImageResource(R.drawable.yes);
        viewHolder.request_point.setText(dataModel.getPoints()+"pts");
        viewHolder.dot_text.setText("  ·  ");
        viewHolder.dot_text.setTextColor(Color.GRAY);
        viewHolder.name.setText(dataModel.getName()+",");
        viewHolder.name.setTextColor(Color.GRAY);
        viewHolder.years.setText(dataModel.getYear()+" y-o");
        viewHolder.years.setTextColor(Color.GRAY);
        viewHolder.homeCity.setText(dataModel.getHomeCity());
        viewHolder.homeCity.setTextColor(Color.GRAY);
        viewHolder.resident.setTextColor(Color.GRAY);
        viewHolder.activityName.setText(dataModel.getActiivityName().toString());
        viewHolder.activityName.setTextColor(Color.GRAY);
        viewHolder.imageFlagOne.setImageResource(R.drawable.canada);
        viewHolder.imageFlagTwo.setImageResource(R.drawable.canada);
        viewHolder.imageFlagThree.setImageResource(R.drawable.canada);
        if(dataModel.request_state == 0){
            //Do nothing!
        } else if (dataModel.request_state == 1){
            viewHolder.yesImageButton.setImageResource(R.drawable.success);
        } else if(dataModel.request_state==2){
            viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
        } else {
            Log.w("DATABASE ERROR","Old records... like the beatles");
        }

        final RequestModel buttonData = dataModel;

       viewHolder.notImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
                viewHolder.yesImageButton.setImageResource(R.drawable.yes);

                //TODO: Call backend to post request
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

               // Log.d("Parth Desai",buttonData.request_state+":"+buttonData.getid()+":"+buttonData.sender_id);
                RequestParams params = new RequestParams();
                params.put("sender",buttonData.getSender_id());
                params.put("request_state","2");
                //Log.w("WHY IS THIS HAPPENING", buttonData.getid()+"");
                params.put("request_id",buttonData.getid());
                params.setUseJsonStreamer(true);

                    client.post(MainActivity.base_host_url + "api/updateActivityRequest/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST UPDATE REQUEST", statusCode + ": " + "Response = " + response.toString());
                            }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST UPDATE REQUEST", statusCode + ": " + timeline.toString());
                            //
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Log.w("POST PROFILE RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST PROFILE FAIL", "Error Code: " + error_code + "," + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                            Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                        }
                    });
            }
        });

        viewHolder.yesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.yesImageButton.setImageResource(R.drawable.success);
                viewHolder.notImageButton.setImageResource(R.drawable.not);

                //TODO: Call backend to post request state
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

             //   Log.d("Parth Desai",buttonData.request_state+":"+buttonData.getid()+":"+buttonData.sender_id);
                RequestParams params = new RequestParams();
                params.put("request_state","1");
                params.put("sender",dataModel.getSender_id());
                params.put("request_id",buttonData.getid());
                params.setUseJsonStreamer(true);

                client.post(MainActivity.base_host_url + "api/updateActivityRequest/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.w("POST UPDATE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST UPDATE SUCCESS2", statusCode + ": " + timeline.toString());
                        //
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        Log.w("POST PROFILE RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Log.w("POST PROFILE FAIL", "Error Code: " + error_code + "," + text);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                        Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                    }
                });
            }
       });
        // Return the completed view to render on screen
        return convertView;
}
}

