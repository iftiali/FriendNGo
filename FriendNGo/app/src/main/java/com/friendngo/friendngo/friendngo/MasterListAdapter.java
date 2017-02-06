package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by scott on 2017-02-03.
 */

public class MasterListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater layoutInflater;

    //TODO: Make this more efficient by using ViewHolder pattern
    //This is the data structure that will be recycled
//    private static class ViewHolder {
//        ImageView categoryImage;
//        ImageView categoryCheckMark;
//        TextView categorySubtext;
//        HorizontalScrollView horizontalScrollView;
//    }


    public MasterListAdapter(Context context){
        super();
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return Popular.categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Inflate the layout
        convertView = layoutInflater.inflate(R.layout.activity_what_do_you_want2, null);
        ImageView categoryImage = (ImageView)convertView.findViewById(R.id.category_image_button);
        final ImageView categoryCheckMark = (ImageView)convertView.findViewById(R.id.master_category_check_mark);
        TextView categorySubtext = (TextView)convertView.findViewById(R.id.category_list_subtext);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView)convertView.findViewById(R.id.horizontal_scroll_view_template);

        //Set the category image
        Category category = (Category) Popular.categoryList.get(position);
        switch (category.name) {
            case "Arts & Culture":
                categoryImage.setImageResource(R.drawable.art_exposition);
                break;
            case "Nightlife":
                categoryImage.setImageResource(R.drawable.music);
                break;
            case "Sports":
                categoryImage.setImageResource(R.drawable.running);
                break;
            case "Business":
                categoryImage.setImageResource(R.drawable.handshake);
                break;
            case "Date":
                categoryImage.setImageResource(R.drawable.naked_run);
                break;
            case "Activities":
                categoryImage.setImageResource(R.drawable.billard);
                break;
            case "Outdoors":
                categoryImage.setImageResource(R.drawable.backpack);
                break;
            case "Camping":
                categoryImage.setImageResource(R.drawable.camping);
                break;
            case "Food and Drink":
                categoryImage.setImageResource(R.drawable.grab_drink);
                break;
            case "Networking":
                categoryImage.setImageResource(R.drawable.coworking);
                break;
            default:
                categoryImage.setImageResource(R.drawable.wink);
        }

        categoryCheckMark.setVisibility(View.INVISIBLE);
        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryCheckMark.getVisibility()==View.INVISIBLE){
                    categoryCheckMark.setVisibility(View.VISIBLE);
                }else {
                    categoryCheckMark.setVisibility(View.INVISIBLE);
                }

            }
        });


        categorySubtext.setText(category.name);
        categorySubtext.setTextColor(Color.GRAY);

        //TODO: Add horizontal scroll bars for activity types
//        //Put in top 3 views for now
//        View horizontalView1 = layoutInflater.inflate(R.layout.horizontal_layout_item, null);
//        String activityType1A = category
//
//        View horizontalView2 = layoutInflater.inflate(R.layout.horizontal_layout_item, null);
//        View horizontalView3 = layoutInflater.inflate(R.layout.horizontal_layout_item, null);
//
//        horizontalScrollView.addView();

        return convertView;
    }
}
