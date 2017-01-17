package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by scott on 2017-01-17.
 */

public class ActivityListAdapter extends ArrayAdapter<UserActivity> implements View.OnClickListener {

//    private ArrayList<UserActivity> userActivityArrayList; //Use static activities list for now
    Context mContext;

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        UserActivity userActivity = (UserActivity)object;

        double clickedLatitude = userActivity.getLatitude();
        double clickedLongitude = userActivity.getLongitude();

        //TODO: Center the map at the clicked latitude and longitude and select that list item
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        UserActivity userActivity = getItem(position);
//        ViewHolder viewHolder;
//
//        final View result;
//
//        if(convertView == null) {
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.activity_list_row_item, parent, false);
//            viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name);
//            viewHolder.creator = (TextView) convertView.findViewById(R.id.created_text);
//
//            result=convertView;
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
//        }
//
//        viewHolder.name.setText(userActivity.getName());
//        viewHolder.creator.setText(userActivity.getCreator());
//
//        return convertView;
////        Animation animation = AnimationUtils.loadAnimation(mContext,(position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//    }

    private static class ViewHolder {
        TextView name;
        TextView creator;
    }

    public ActivityListAdapter(Context context){
        super(context, R.layout.activity_list_row_item, MapActivity.activitiesList);
        this.mContext = context;
    }
}


