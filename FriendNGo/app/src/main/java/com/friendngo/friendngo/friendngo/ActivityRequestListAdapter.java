package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
/**
 * Created by krishna on 2017-02-07.
 */

public class ActivityRequestListAdapter extends ArrayAdapter<RequestModel> implements View.OnClickListener{

    private ArrayList<RequestModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        CircularImageView profilePicture;
        TextView name;
        TextView years;
        TextView homeCity;
        ImageView nationality;
        TextView points;
        TextView resident;
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
            viewHolder.profilePicture = (CircularImageView) convertView.findViewById(R.id.profilepicture);
            viewHolder.name = (TextView) convertView.findViewById(R.id.request_person_name);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.reqiest_city_name);
            viewHolder.years = (TextView)convertView.findViewById(R.id.request_years);
            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.request_flag);
            viewHolder.points = (TextView) convertView.findViewById(R.id.request_point);
            viewHolder.notImageButton =(CircularImageView)convertView.findViewById(R.id.request_delete_image);
            viewHolder.yesImageButton = (CircularImageView)convertView.findViewById(R.id.request_success_image);
            viewHolder.nationality.setImageResource(R.drawable.canada);
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
<<<<<<< HEAD

        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(mContext) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS2","Successfully Retrieved The Image");
                Picasso.with(mContext).load(response).into(profilePic);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL","Could not retrieve image");
            }
        });
=======
        Picasso.with(mContext).load(MainActivity.base_host_url + pictureURL).into(profilePic);
>>>>>>> dev6

        viewHolder.notImageButton.setImageResource(R.drawable.not);
        viewHolder.yesImageButton.setImageResource(R.drawable.yes);

        viewHolder.name.setText(dataModel.getName()+",");
        viewHolder.name.setTextColor(Color.GRAY);
        viewHolder.years.setText(dataModel.getYear()+" y-o");
        viewHolder.years.setTextColor(Color.GRAY);
        viewHolder.homeCity.setText(dataModel.getHomeCity());
        viewHolder.homeCity.setTextColor(Color.GRAY);
        viewHolder.resident.setTextColor(Color.GRAY);

        if(dataModel.request_state == 0){
            //Do nothing!
        } else if (dataModel.request_state == 1){
            viewHolder.yesImageButton.setImageResource(R.drawable.success);
        } else if(dataModel.request_state==2){
            viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
        } else {
            Log.w("DATABASE ERROR","Old records... like the beatles");
        }

       viewHolder.notImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
                viewHolder.yesImageButton.setImageResource(R.drawable.yes);

                //TODO: Call backend to post request state
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

                Log.w("REQUESTS DEBUG ID:", dataModel.getRequest_id() + "");
                RequestParams params = new RequestParams();
                params.put("request_state","2");
                params.put("id",dataModel.getRequest_id());
                params.setUseJsonStreamer(true);

                    client.post(MainActivity.base_host_url + "api/updateActivityRequest/", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                            }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST PROFILE SUCCESS2", statusCode + ": " + timeline.toString());
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

                RequestParams params = new RequestParams();
                params.put("request_state","1");
                params.put("id",dataModel.getSender_id());
                params.setUseJsonStreamer(true);

                client.post(MainActivity.base_host_url + "api/updateActivityRequest/", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.w("POST PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST PROFILE SUCCESS2", statusCode + ": " + timeline.toString());
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

