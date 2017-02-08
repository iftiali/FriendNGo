package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;

import static com.friendngo.friendngo.friendngo.R.drawable.add_oval;

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

           // case R.id.item_info:

                /*Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
*/
             //   break;


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


        viewHolder.profilePicture.setImageResource(R.drawable.scott);
        viewHolder.notImageButton.setImageResource(R.drawable.not);
        viewHolder.yesImageButton.setImageResource(R.drawable.yes);
        Log.w("name",dataModel.getName());
        viewHolder.name.setText(dataModel.getName()+",");
        viewHolder.name.setTextColor(Color.GRAY);
        viewHolder.years.setText(dataModel.getYear()+" Years");
        viewHolder.years.setTextColor(Color.GRAY);
        viewHolder.homeCity.setText(dataModel.getHomeCity());
        viewHolder.homeCity.setTextColor(Color.GRAY);
        viewHolder.resident.setTextColor(Color.GRAY);
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

