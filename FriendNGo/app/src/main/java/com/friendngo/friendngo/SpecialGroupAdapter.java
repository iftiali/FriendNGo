package com.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krishna on 2017-04-26.
 */

public class SpecialGroupAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    SpecialGroupModel sgm = null;

    public SpecialGroupAdapter(Activity a, ArrayList d) {
         activity = a;
         data=d;
         inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public TextView special_group_name_text_view;

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.special_group_list, null);
            holder = new ViewHolder();
            holder.special_group_name_text_view = (TextView) vi.findViewById(R.id.special_group_name_text_view);
            vi.setTag(holder);
        } else
            holder=(ViewHolder)vi.getTag();
        if(data.size()<=0)
        {
          //  holder.special_group_name_text_view.setText("No Data");

        }else {
            sgm=null;
            sgm = ( SpecialGroupModel ) data.get( position );
            holder.special_group_name_text_view.setText( sgm.getGroupName());

        }
        return vi;
    }
}
