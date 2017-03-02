package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;

import static com.friendngo.friendngo.friendngo.MapActivity.activitiesList;

/**
 * Created by scott on 2017-01-17.
 */

public class ActivityListAdapter extends ArrayAdapter<UserActivity> implements View.OnClickListener {

    Context mContext;

    //Maps each list row item to one Activity
    public ActivityListAdapter(Context context){
        super(context, R.layout.activity_list_row_item, activitiesList);
        this.mContext = context;
    }

    //This is the data structure that will be recycled
    private static class ViewHolder {
        ImageView profilePicture;
        TextView name;
        TextView creator;
        TextView status;
        TextView homeCity;
        ImageView nationality;
        TextView points;
        ImageView category;
        ImageView clock;
        TextView dateTime;
        ImageView pin;
        TextView distance;
        RelativeLayout info;
        Button addActivityButton;
    }

    //Process a click on a row item
    @Override
    public void onClick(View v) {
        Log.w("ADAPTER","List Item Clicked... adapter onClick(View v) was called");
        int position = (Integer) v.getTag(R.string.POSITION_KEY);
        Object object = getItem(position);
        UserActivity userActivity = (UserActivity)object;
        Log.w("ADAPTER","Item = " + userActivity.getName());
        MapActivity.centerOnActivity(userActivity.getName());
    }

    //Creates the View instance for the row from xml OR recycles it if already available
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        UserActivity userActivity = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if(convertView == null) {

            //View needs to be inflated from xml
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_list_row_item, null, true); //Seems equivalent to inflate(R... , parent, false)
            viewHolder.profilePicture = (ImageView) convertView.findViewById(R.id.messages_profile_picture);
            viewHolder.creator = (TextView) convertView.findViewById(R.id.paid_event_created_text);
            viewHolder.status = (TextView) convertView.findViewById(R.id.participants_status_text);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.participants_city_text);
            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.participants_country_flag);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.category = (ImageView) convertView.findViewById(R.id.activity_type);
            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
            viewHolder.clock = (ImageView) convertView.findViewById(R.id.messages_clock_image);
            viewHolder.dateTime = (TextView) convertView.findViewById(R.id.paid_event_activity_time);
            viewHolder.pin = (ImageView) convertView.findViewById(R.id.pin_image);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.info = (RelativeLayout) convertView.findViewById(R.id.row_item);
            viewHolder.addActivityButton = (Button) convertView.findViewById(R.id.add_activity_button);

            convertView.setTag(R.string.VIEW_HOLDER_KEY,viewHolder); //This associates the viewHolder to the convertView
        } else {
            //Recycle old view -> More Efficient
            viewHolder = (ViewHolder) convertView.getTag(R.string.VIEW_HOLDER_KEY);
        }

        //Here is where we map our model data to our View instance
        viewHolder.name.setText(userActivity.getName());
        viewHolder.name.setTextColor(Color.GRAY);
        viewHolder.creator.setText("Created by "+userActivity.getCreator());
        viewHolder.creator.setTextColor(Color.GRAY);
//        viewHolder.profilePicture.setImageResource(R.drawable.scott);
        viewHolder.status.setText("Resident" + ", ");
        viewHolder.status.setTextColor(Color.GRAY);
        viewHolder.homeCity.setText(userActivity.getHomeCity());
        viewHolder.homeCity.setTextColor(Color.GRAY);
        viewHolder.nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
        viewHolder.points.setText(userActivity.getPoints()+"pts");

        //GET The image file at the pictureURL
        AsyncHttpClient client = new AsyncHttpClient();

        String pictureURL = ((UserActivity)activitiesList.get(position)).getProfilePicURL();
        final ImageView profilePic = (ImageView) convertView.findViewById(R.id.messages_profile_picture);

        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(mContext) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
                //Use the downloaded image as the profile picture
                Uri uri = Uri.fromFile(response);
                profilePic.setImageURI(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL","Could not retrieve image");
            }
        });

        switch(userActivity.getCategory()){
            case "Art & Culture":
                viewHolder.category.setImageResource(R.drawable.art_exposition);
                break;
            case "Nightlife":
                viewHolder.category.setImageResource(R.drawable.music);
                break;
            case "Sports":
                viewHolder.category.setImageResource(R.drawable.running);
                break;
            case "Professional & Networking":
                viewHolder.category.setImageResource(R.drawable.coworking);
                break;
            case "Fun & Crazy":
                viewHolder.category.setImageResource(R.drawable.naked_run);
                break;
            case "Games":
                viewHolder.category.setImageResource(R.drawable.billard);
                break;
            case "Travel & Road-Trip":
                viewHolder.category.setImageResource(R.drawable.backpack);
                break;
            case "Nature & Outdoors":
                viewHolder.category.setImageResource(R.drawable.camping);
                break;
            case "Social Activities":
                viewHolder.category.setImageResource(R.drawable.grab_drink);
                break;
            case "Help & Association":
                viewHolder.category.setImageResource(R.drawable.coworking);
        }

        viewHolder.clock.setImageResource(R.drawable.clock);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, HH:mma");
        viewHolder.dateTime.setText(dateFormat.format(userActivity.getActivityTime()));
        viewHolder.dateTime.setTextColor(Color.GRAY);
        viewHolder.pin.setImageResource(R.drawable.pin);
        viewHolder.distance.setText( userActivity.getDistance()+" km away");
        viewHolder.distance.setTextColor(Color.GRAY);
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(R.string.POSITION_KEY, position);

        if(viewHolder.addActivityButton != null) {
            viewHolder.addActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ActivityDetails.class);
                    intent.putExtra("Activity Index", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}


