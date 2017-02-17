package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.friendngo.friendngo.friendngo.MapActivity.activitiesList;
import static com.friendngo.friendngo.friendngo.R.drawable.add_oval;

/**
 * Created by krishna on 2017-02-07.
 */

public class ActivityRequestListAdapter extends ArrayAdapter<RequestModel> implements View.OnClickListener{
//
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
        // Get the data item for this position
        RequestModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
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
               // viewHolder.notImageButton.setBackground(R.drawable.add_oval);
                viewHolder.notImageButton.setImageResource(R.drawable.delete_red);
                viewHolder.yesImageButton.setImageResource(R.drawable.yes);

            }
        });
        viewHolder.yesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.yesImageButton.setImageResource(R.drawable.success);
                viewHolder.notImageButton.setImageResource(R.drawable.not);
            }
       });
        // Return the completed view to render on screen
        return convertView;
}
}

