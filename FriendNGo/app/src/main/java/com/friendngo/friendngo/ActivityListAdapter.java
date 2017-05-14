package com.friendngo.friendngo;

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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import cz.msebera.android.httpclient.Header;

import static com.friendngo.friendngo.MapActivity.activitiesList;

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
        TextView paid_event_created_text,paid_event_activity_time,paid_event_distance,paid_event_status_text_view;

        TextView name;
        TextView creator;
        TextView status;
        TextView homeCity;
//        ImageView nationality;
        TextView points;
        ImageView category;
        ImageView clock,paid_event_clock,paid_event_pin_image,paid_event_activity_type;
        TextView dateTime;
        ImageView pin,paid_event_certified;
        TextView distance;
        RelativeLayout info;
        Button addActivityButton;
        Button paidAddActivityButton;
        ImageView imageFlagOne;
        ImageView imageFlagTwo;
        ImageView imageFlagThree;
        RelativeLayout freeEvent,paidEventRelativeLayout;
        ;
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
            viewHolder.creator = (TextView) convertView.findViewById(R.id.created_text);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status_text);
            viewHolder.homeCity = (TextView) convertView.findViewById(R.id.list_city_text);
//            viewHolder.nationality = (ImageView) convertView.findViewById(R.id.country_flag);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.category = (ImageView) convertView.findViewById(R.id.activity_type);
            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
            viewHolder.clock = (ImageView) convertView.findViewById(R.id.clock_image);
            viewHolder.dateTime = (TextView) convertView.findViewById(R.id.activity_time);
            viewHolder.pin = (ImageView) convertView.findViewById(R.id.pin_image);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.info = (RelativeLayout) convertView.findViewById(R.id.row_item);
            viewHolder.addActivityButton = (Button) convertView.findViewById(R.id.add_activity_button);
            viewHolder.paidAddActivityButton = (Button) convertView.findViewById(R.id.add_activity_button_paid);
            viewHolder.imageFlagOne = (ImageView)convertView.findViewById(R.id.list_country_flag_one);
            viewHolder.imageFlagTwo = (ImageView)convertView.findViewById(R.id.list_country_flag_two);
            viewHolder.imageFlagThree = (ImageView)convertView.findViewById(R.id.list_country_flag_three);

            //paid event
            viewHolder.paidEventRelativeLayout = (RelativeLayout)convertView.findViewById(R.id.paidEventRelativeLayout);
            viewHolder.paid_event_created_text = (TextView)convertView.findViewById(R.id.paid_event_created_text);
            viewHolder.paid_event_clock = (ImageView)convertView.findViewById(R.id.paid_event_clock);
            viewHolder.paid_event_pin_image = (ImageView)convertView.findViewById(R.id.paid_event_pin_image);
            viewHolder.paid_event_activity_time = (TextView)convertView.findViewById(R.id.paid_event_activity_time);
            viewHolder.paid_event_distance = (TextView)convertView.findViewById(R.id.paid_event_distance);
            viewHolder.paid_event_activity_type = (ImageView)convertView.findViewById(R.id.paid_event_activity_type);
            viewHolder.paid_event_status_text_view = (TextView)convertView.findViewById(R.id.paid_event_status_text);
            viewHolder.paid_event_certified = (ImageView)convertView.findViewById(R.id.paid_event_certified);
            convertView.setTag(R.string.VIEW_HOLDER_KEY, viewHolder); //This associates the viewHolder to the convertView
        } else {
            //Recycle old view -> More Efficient
            viewHolder = (ViewHolder) convertView.getTag(R.string.VIEW_HOLDER_KEY);
        }
        if(userActivity.getisPaid()){

            viewHolder.freeEvent.setVisibility(View.GONE);
            viewHolder.paidEventRelativeLayout.setVisibility(View.VISIBLE);
            //GET The image file at the pictureURL
            AsyncHttpClient client = new AsyncHttpClient();

            String pictureURL = ((UserActivity) activitiesList.get(position)).getOrganization_logo();

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
            viewHolder.paid_event_status_text_view.setTextColor(Color.GRAY);
            viewHolder.paid_event_certified.setImageResource(R.drawable.checkmark);
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

            viewHolder.paidEventRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ActivityDetailPaidEvent.class);
                    intent.putExtra("Activity Index", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }else {

            viewHolder.freeEvent.setVisibility(View.VISIBLE);
            viewHolder.paidEventRelativeLayout.setVisibility(View.GONE);
            //Here is where we map our model data to our View instance
            viewHolder.imageFlagOne.setVisibility(View.GONE);
            viewHolder.imageFlagTwo.setVisibility(View.GONE);
            viewHolder.imageFlagThree.setVisibility(View.GONE);
            getLanguages(userActivity.getcreator_PK(),viewHolder);
            viewHolder.name.setText(userActivity.getName());
            viewHolder.name.setTextColor(Color.GRAY);
            Log.w("CREATED BY",userActivity.getCreator());
           viewHolder.creator.setText("Created by " + userActivity.getCreator().toString());
            viewHolder.creator.setTextColor(Color.GRAY);
//        viewHolder.profilePicture.setImageResource(R.drawable.scott);
            viewHolder.status.setText("Resident" + ", ");
            viewHolder.status.setTextColor(Color.GRAY);
            if(userActivity.getHomeCity() == null){
                Log.d("Hello",userActivity.getHomeCity().toString());
            }else {
                Log.d("Hello",userActivity.getHomeCity().toString());
                viewHolder.homeCity.setText(userActivity.getHomeCity());
            }
            viewHolder.homeCity.setTextColor(Color.GRAY);
//            viewHolder.nationality.setImageResource(R.drawable.canada); //TODO: Get flag from nationalities
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
            Picasso.with(mContext).load(MainActivity.base_host_url + pictureURL).into(profilePic);
           /* client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(mContext) {

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
            });*/

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
            viewHolder.pin.setImageResource(R.drawable.icon_mapa);
            viewHolder.distance.setText(userActivity.getDistance() + " km away");
            viewHolder.distance.setTextColor(Color.GRAY);
            viewHolder.info.setOnClickListener(this);
            viewHolder.info.setTag(R.string.POSITION_KEY, position);

            if (viewHolder.addActivityButton != null) {
                viewHolder.freeEvent.setOnClickListener(new View.OnClickListener() {
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
    private void getLanguages(long creator_pk, final ViewHolder viewHolderlanguage) {
        AsyncHttpClient client = new AsyncHttpClient();

        if(SignIn.static_token != null) {

            client.addHeader("Authorization","Token "+SignIn.static_token);
        }else{

        }
        client.get(MainActivity.base_host_url + "api/getProfilePK/"+creator_pk, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("GET Profile PK", statusCode + ": " + "Response = " + response.toString());

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray categoryJSONArray) {
                Log.w("JSON Profile PK", statusCode + ": " + categoryJSONArray.toString());

                try {
                    JSONObject languagesObject = categoryJSONArray.getJSONObject(0);
                    JSONArray languagesArray = languagesObject.getJSONArray("languages");
                    Log.d("Langiage-3",languagesArray.toString());
                    for(int x = 0;x<languagesArray.length();x++){
                        JSONObject languageNames = languagesArray.getJSONObject(x);
                        String languageString = languageNames.getString("name");
                        Log.d("language",languageString);
                        if(x==0){
                            Log.d("language-1",languageString);
                            viewHolderlanguage.imageFlagOne.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                viewHolderlanguage.imageFlagOne.setImageResource(R.drawable.it);
                            }else{
                                viewHolderlanguage.imageFlagOne.setVisibility(View.GONE);
                            }
                        }if(x==1){
                            viewHolderlanguage.imageFlagTwo.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                viewHolderlanguage.imageFlagTwo.setImageResource(R.drawable.it);
                            }else{
                                viewHolderlanguage.imageFlagTwo.setVisibility(View.GONE);
                            }
                        }if(x==2){
                            viewHolderlanguage.imageFlagThree.setVisibility(View.VISIBLE);
                            if(languageString.equals("French") || languageString.equals("français")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.fr);
                            }else if(languageString.equals("English") || languageString.equals("anglais")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.gb);
                            }else if(languageString.equals("Spanish") || languageString.equals("espagnol")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.spain);
                            }else if(languageString.equals("Chinese") || languageString.equals("chinois")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.cn);
                            }else if(languageString.equals("German") || languageString.equals("allemand")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.de);
                            }else if(languageString.equals("Russian") || languageString.equals("russe")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.ru);
                            }else if(languageString.equals("Portuguese") || languageString.equals("portugais")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.pr);
                            }else if(languageString.equals("Arabic") || languageString.equals("arabe")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.ae);
                            }else if(languageString.equals("Korean") || languageString.equals("coréen")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.kr);
                            }else if(languageString.equals("Vietnamese") || languageString.equals("vietnamien")){
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.vn);
                            }else if(languageString.equals("Italian") || languageString.equals("italien")) {
                                viewHolderlanguage.imageFlagThree.setImageResource(R.drawable.it);
                            }else{
                                viewHolderlanguage.imageFlagThree.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET Profile PK", "Error Code: " + error_code + "," + text);
            }

            @Override
            public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json){

                Log.w("GET Profile PK", "Error Code: " + error_code + ",  " + json.toString());
            }
        });

    }
}


