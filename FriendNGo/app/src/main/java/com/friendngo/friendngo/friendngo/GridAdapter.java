package com.friendngo.friendngo.friendngo;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public GridAdapter(Context c){
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return Popular.categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return Popular.categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        convertView = mInflater.inflate(R.layout.grid_item,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
        TextView textView = (TextView) convertView.findViewById(R.id.grid_text);
        Category category = (Category) Popular.categoryList.get(i);
        if (Popular.categoryList.get(i) != null) {

            try {

                String activityTypeText = category.getActivityTypeList().get(0).toString();
                textView.setText(activityTypeText);
                textView.setTextColor(Color.GRAY);
            } catch (IndexOutOfBoundsException e){
                Log.w("GRID ADAPTER","Index Out of Bounds");
                textView.setText("Not Available");
                textView.setTextColor(Color.GRAY);
            }
        } else{
            Log.w("GRID ADAPTER","Category Empty");
            textView.setText("Not Available");
            textView.setTextColor(Color.GRAY);
        }

        switch (category.name) {
            case "Art & Culture":
                imageView.setImageResource(R.drawable.art_exposition);
                break;
            case "Nightlife":
                imageView.setImageResource(R.drawable.concert);
                break;
            case "Sports":
                imageView.setImageResource(R.drawable.running);
                break;
            case "Fun & Crazy":
                imageView.setImageResource(R.drawable.naked_run);
                break;
            case "Games":
                imageView.setImageResource(R.drawable.billard);
                break;
            case "Nature & Outdoors":
                imageView.setImageResource(R.drawable.backpack);
                break;
            case "Travel & Road-Trip":
                imageView.setImageResource(R.drawable.camping);
                break;
            case "Social Activities":
                imageView.setImageResource(R.drawable.grab_drink);
                break;
            case "Professional & Networking":
                imageView.setImageResource(R.drawable.coworking);
                break;
            case "Help & Association":
                imageView.setImageResource(R.drawable.coworking);
                break;
            default:
                imageView.setImageResource(R.drawable.naked_run);
        }
        return convertView;
    }
}
