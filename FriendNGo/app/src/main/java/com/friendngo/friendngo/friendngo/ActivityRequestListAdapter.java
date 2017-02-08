package com.friendngo.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.data;

/**
 * Created by krishna on 2017-02-07.
 */

public class ActivityRequestListAdapter extends ArrayAdapter<RequestModel> {

    private ArrayList<RequestModel> dataSet;
    Context mContext;

    //Maps each list row item to one Activity
    public ActivityRequestListAdapter(Context context){
        super(context, R.layout.activity_request_list_row_item, Request.requestList);
        this.mContext = context;
    }

    //This is the data structure that will be recycled
    private static class ViewHolder {
        CircularImageView profilePicture;
        TextView name;
        TextView years;
        TextView homeCity;
//        ImageView nationality;
//        TextView points;
//
//        ImageButton notImageButton,yesImageButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       // UserActivity userActivity = getItem(position);

        final ViewHolder viewHolder;
        final View result;


        if(convertView == null) {

            //View needs to be inflated from xml
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_request_list_row_item, null, true); //Seems equivalent to inflate(R... , parent, false)
            viewHolder.profilePicture = (CircularImageView) convertView.findViewById(R.id.profilepicture);
            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.home_city_text);
//            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.country_flag);
//            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
//            viewHolder.notImageButton =(ImageButton)convertView.findViewById(R.id.request_delete_image);
//            viewHolder.yesImageButton = (ImageButton)convertView.findViewById(R.id.request_success_image);
//            viewHolder.nationality.setImageResource(R.drawable.canada);

            convertView.setTag(R.string.VIEW_HOLDER_KEY,viewHolder); //This associates the viewHolder to the convertView
        } else {
            //Recycle old view -> More Efficient
            viewHolder = (ViewHolder) convertView.getTag(R.string.VIEW_HOLDER_KEY);
        }
        viewHolder.profilePicture.setImageResource(R.drawable.scott);
        viewHolder.name.setText("Parth Desai");
        viewHolder.years.setText("26 years");
        viewHolder.homeCity.setText("Montreal");
        /*viewHolder.notImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
            }
        });*/
//        viewHolder.yesImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.yesImageButton.setImageResource(R.drawable.delete_red);
//            }
//        });
        return convertView;
}
}

