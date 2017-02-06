package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by scott on 2017-01-17.
 */

public class ActivityListAdapter extends ArrayAdapter<UserActivity> implements View.OnClickListener {

    Context mContext;

    //Maps each list row item to one Activity
    public ActivityListAdapter(Context context){
        super(context, R.layout.activity_list_row_item, MapActivity.activitiesList);
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        UserActivity userActivity = getItem(position);

        ViewHolder viewHolder;
        final View result;


        if(convertView == null) {

            //View needs to be inflated from xml
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_list_row_item, null, true); //Seems equivalent to inflate(R... , parent, false)
            viewHolder.profilePicture = (ImageView) convertView.findViewById(R.id.profilepicture);
            viewHolder.creator = (TextView) convertView.findViewById(R.id.created_text);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status_text);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.home_city_text);
            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.country_flag);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.category = (ImageView) convertView.findViewById(R.id.activity_type);
            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
            viewHolder.clock = (ImageView) convertView.findViewById(R.id.clock_image);
            viewHolder.dateTime = (TextView) convertView.findViewById(R.id.activity_time);
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
        viewHolder.profilePicture.setImageResource(R.drawable.scott);
        viewHolder.status.setText("Resident" + ", ");
        viewHolder.status.setTextColor(Color.GRAY);
        viewHolder.homeCity.setText(userActivity.getHomeCity());
        viewHolder.homeCity.setTextColor(Color.GRAY);
        viewHolder.nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
        viewHolder.points.setText(userActivity.getPoints()+"pts");

        switch(userActivity.getCategory()){
            case "Arts & Culture":
                viewHolder.category.setImageResource(R.drawable.arts_and_culture);
                break;
            case "Nightlife":
                viewHolder.category.setImageResource(R.drawable.nightlife);
                break;
            case "Sports":
                viewHolder.category.setImageResource(R.drawable.sports);
                break;
            case "Networking":
                viewHolder.category.setImageResource(R.drawable.handshake);
                break;
            case "Dating":
                viewHolder.category.setImageResource(R.drawable.wink);
                break;
            case "Activities":
                viewHolder.category.setImageResource(R.drawable.pool);
                break;
            case "Outdoors":
                viewHolder.category.setImageResource(R.drawable.backpack);
                break;
            case "Camping":
                viewHolder.category.setImageResource(R.drawable.camping);
                break;
            case "Drinks":
                viewHolder.category.setImageResource(R.drawable.cup);
                break;
            case "Meetup":
                viewHolder.category.setImageResource(R.drawable.three);
        }

        viewHolder.clock.setImageResource(R.drawable.clock);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, HH:mma");
        viewHolder.dateTime.setText(dateFormat.format(userActivity.getActivityTime()));
        viewHolder.dateTime.setTextColor(Color.GRAY);
        viewHolder.pin.setImageResource(R.drawable.pin);
        viewHolder.distance.setText( userActivity.getDistance()+" away");
        viewHolder.distance.setTextColor(Color.GRAY);

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(R.string.POSITION_KEY, position);

        //TODO: Fix this: addActivityButton always returns null??
        //Needs a check for null for some reason
        if(viewHolder.addActivityButton != null) {
            viewHolder.addActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ActivityDetails.class);
                    //TODO: Put extra -> The name of the activity
                    mContext.startActivity(intent);
                }
            });
        }


        return convertView;
//        Animation animation = AnimationUtils.loadAnimation(mContext,(position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
    }
}


