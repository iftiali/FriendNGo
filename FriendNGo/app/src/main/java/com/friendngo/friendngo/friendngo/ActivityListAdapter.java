package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mikhaellopez.circularimageview.CircularImageView;

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
    public ActivityListAdapter(Context context) {
        super(context, R.layout.activity_list_row_item, activitiesList);
        this.mContext = context;
    }

    //This is the data structure that will be recycled
    private static class ViewHolder {
        TextView paid_event_created_text,paid_event_activity_time,paid_event_distance;

        TextView name;
        TextView creator;
        TextView status;
        TextView homeCity;
        ImageView nationality;
        TextView points;
        ImageView category;
        ImageView clock,paid_event_clock,paid_event_pin_image,paid_event_activity_type;
        TextView dateTime;
        ImageView pin;
        TextView distance;
        RelativeLayout info;
        Button addActivityButton;
        Button paidAddActivityButton;
        RelativeLayout freeEvent,paidEventRelativeLayout;
    }

    //Process a click on a row item
    @Override
    public void onClick(View v) {
        Log.w("ADAPTER", "List Item Clicked... adapter onClick(View v) was called");
        int position = (Integer) v.getTag(R.string.POSITION_KEY);
        Object object = getItem(position);
        UserActivity userActivity = (UserActivity) object;
        Log.w("ADAPTER", "Item = " + userActivity.getName());
        MapActivity.centerOnActivity(userActivity.getName());
    }

    //Creates the View instance for the row from xml OR recycles it if already available
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserActivity userActivity = getItem(position);
        final ViewHolder viewHolder;
        final View result;
        if (convertView == null) {

            //View needs to be inflated from xml
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_list_row_item, null, true); //Seems equivalent to inflate(R... , parent, false)
            viewHolder.freeEvent = (RelativeLayout) convertView.findViewById(R.id.freeEventRelativeLayout);
//            viewHolder.creator = (TextView) convertView.findViewById(R.id.created_text);
            viewHolder.status = (TextView) convertView.findViewById(R.id.participants_status_text);
//            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.home_city_text);
//            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.country_flag);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.category = (ImageView) convertView.findViewById(R.id.activity_type);
            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
            viewHolder.clock = (ImageView) convertView.findViewById(R.id.messages_clock_image);
            viewHolder.dateTime = (TextView) convertView.findViewById(R.id.paid_event_activity_time);
            viewHolder.pin = (ImageView) convertView.findViewById(R.id.pin_image);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.info = (RelativeLayout) convertView.findViewById(R.id.row_item);
            viewHolder.addActivityButton = (Button) convertView.findViewById(R.id.add_activity_button);
            viewHolder.paidAddActivityButton = (Button) convertView.findViewById(R.id.add_activity_button_paid);

            //paid event
            viewHolder.paidEventRelativeLayout = (RelativeLayout)convertView.findViewById(R.id.paidEventRelativeLayout);
            viewHolder.paid_event_created_text = (TextView)convertView.findViewById(R.id.paid_event_created_text);
            viewHolder.paid_event_clock = (ImageView)convertView.findViewById(R.id.paid_event_clock);
            viewHolder.paid_event_pin_image = (ImageView)convertView.findViewById(R.id.paid_event_pin_image);
            viewHolder.paid_event_activity_time = (TextView)convertView.findViewById(R.id.paid_event_activity_time);
            viewHolder.paid_event_distance = (TextView)convertView.findViewById(R.id.paid_event_distance);
            viewHolder.paid_event_activity_type = (ImageView)convertView.findViewById(R.id.paid_event_activity_type);

            convertView.setTag(R.string.VIEW_HOLDER_KEY, viewHolder); //This associates the viewHolder to the convertView
        } else {
            //Recycle old view -> More Efficient
            viewHolder = (ViewHolder) convertView.getTag(R.string.VIEW_HOLDER_KEY);
        }
        if(userActivity.getisPaid()){
            Log.w("Hello","Event is Paid");
            viewHolder.freeEvent.setVisibility(View.GONE);
            viewHolder.paidEventRelativeLayout.setVisibility(View.VISIBLE);
            //GET The image file at the pictureURL
            AsyncHttpClient client = new AsyncHttpClient();

            String pictureURL = ((UserActivity) activitiesList.get(position)).getProfilePicURL();
            Log.w("Hello",pictureURL);
            final ImageView profilePic = (ImageView) convertView.findViewById(R.id.paid_event_profile_picture);

            client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(mContext) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                    //Use the downloaded image as the profile picture
                    Uri uri = Uri.fromFile(response);
                    profilePic.setImageURI(uri);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Log.w("GET IMAGE FAIL", "Could not retrieve image");
                }
            });
            viewHolder.paid_event_created_text.setText(userActivity.getName());
            viewHolder.paid_event_created_text.setTextColor(Color.GRAY);
            viewHolder.paid_event_clock.setImageResource(R.drawable.clock);
            viewHolder.paid_event_pin_image .setImageResource(R.drawable.icon_mapa);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, HH:mma");
            viewHolder.paid_event_activity_time.setText(dateFormat.format(userActivity.getActivityTime()));
            viewHolder.paid_event_activity_time.setTextColor(Color.GRAY);
            viewHolder.paid_event_distance.setText(userActivity.getAddress().toString());
            viewHolder.paid_event_distance.setTextColor(Color.GRAY);
            switch (userActivity.getCategory()) {
                case "Art & Culture":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.art_exposition);
                    break;
                case "Nightlife":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.music);
                    break;
                case "Sports":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.running);
                    break;
                case "Professional & Networking":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.coworking);
                    break;
                case "Fun & Crazy":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.naked_run);
                    break;
                case "Games":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.billard);
                    break;
                case "Travel & Road-Trip":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.backpack);
                    break;
                case "Nature & Outdoors":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.camping);
                    break;
                case "Social Activities":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.grab_drink);
                    break;
                case "Help & Association":
                    viewHolder.paid_event_activity_type.setImageResource(R.drawable.coworking);
                    break;
                default:
                    Log.w("CASE FAILURE", "Invalid Case for category");
                    break;
            }

            Log.w("OVAL DEBUG", userActivity.getRequest_state()+"");
            if(userActivity.getRequest_state()==0){
                viewHolder.paidAddActivityButton.setBackgroundResource(R.drawable.edit_oval);
            }
            if(userActivity.getRequest_state()==1){
                viewHolder.paidAddActivityButton.setBackgroundResource(R.drawable.success);
            }
            if(userActivity.getRequest_state()==2){
                viewHolder.paidAddActivityButton.setBackgroundResource(R.drawable.delete_red);
            }
        }else {
            viewHolder.freeEvent.setVisibility(View.VISIBLE);
            viewHolder.paidEventRelativeLayout.setVisibility(View.GONE);
            //Here is where we map our model data to our View instance
            viewHolder.name.setText(userActivity.getName());
            viewHolder.name.setTextColor(Color.GRAY);
            Log.w("CREATED BY",userActivity.getCreator());
           viewHolder.creator.setText("Created by " + userActivity.getCreator().toString());
            viewHolder.creator.setTextColor(Color.GRAY);
//        viewHolder.profilePicture.setImageResource(R.drawable.scott);
            viewHolder.status.setText("Resident" + ", ");
            viewHolder.status.setTextColor(Color.GRAY);
            viewHolder.homeCity.setText(userActivity.getHomeCity());
            viewHolder.homeCity.setTextColor(Color.GRAY);
            viewHolder.nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
            viewHolder.points.setText(userActivity.getPoints() + "pts");

            if(userActivity.getRequest_state()==0){
                viewHolder.addActivityButton.setBackgroundResource(R.drawable.edit_oval);
                viewHolder.addActivityButton.setText("");
            }
            if(userActivity.getRequest_state()==1){
                viewHolder.addActivityButton.setBackgroundResource(R.drawable.success);
                viewHolder.addActivityButton.setText("");
            }
            if(userActivity.getRequest_state()==2){
                viewHolder.addActivityButton.setBackgroundResource(R.drawable.delete_red);
                viewHolder.addActivityButton.setText("");
            }

        //GET The image file at the pictureURL
        AsyncHttpClient client = new AsyncHttpClient();

        String pictureURL = ((UserActivity) activitiesList.get(position)).getProfilePicURL();
        final ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilepicture);
        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(mContext) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                //Use the downloaded image as the profile picture
                Uri uri = Uri.fromFile(response);
                profilePic.setImageURI(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.w("GET IMAGE FAIL", "Could not retrieve image");
            }
        });

        switch (userActivity.getCategory()) {
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
        viewHolder.distance.setText(userActivity.getDistance() + " km away");
        viewHolder.distance.setTextColor(Color.GRAY);
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(R.string.POSITION_KEY, position);


        if (viewHolder.addActivityButton != null) {
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
        }
        return convertView;
    }



}


