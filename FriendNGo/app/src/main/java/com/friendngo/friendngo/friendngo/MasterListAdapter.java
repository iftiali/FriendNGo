package com.friendngo.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017-02-03.
 */

public class MasterListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater layoutInflater;
    private RecyclerView mHorizontalRecycleView;
    private RecyclerView.LayoutManager mHorizontallayoutManager;
    private RecyclerView.Adapter mHorizontalAdapter;
    private ArrayList<String> mHorizontalDataset;

    public MasterListAdapter(Context context){
        super();
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return WhatDoYouWantToDoToday.categoryList.size();
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

        mHorizontalDataset = new ArrayList<>();
        mHorizontalRecycleView = (RecyclerView)convertView.findViewById(R.id.recycler_view);
        mHorizontalRecycleView.setHasFixedSize(true);
        mHorizontallayoutManager = new LinearLayoutManager(convertView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        mHorizontalRecycleView.setLayoutManager(mHorizontallayoutManager);
        mHorizontalAdapter = new WhatDoyouWantToDoTodayHorizontalRow(mHorizontalDataset);
        mHorizontalRecycleView.setAdapter(mHorizontalAdapter);

        //Set the category image
        Category category = (Category) WhatDoYouWantToDoToday.categoryList.get(position);
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

        mHorizontalDataset.clear();
        String itemSelected =  category.name;
        List categoryArrayList=new ArrayList<Category>();
        categoryArrayList = WhatDoYouWantToDoToday.categoryList;
        Category c = new Category();

        for(int i =0; i<categoryArrayList.size(); i++) {
            c = (Category) categoryArrayList.get(i);
            if (itemSelected.equals(c.getName())) {
                //Log.w("category list", c.getName());
                //Log.w("Size",c.getActivityTypeList().size()+"");
                for (int j = 0; j < c.getActivityTypeList().size(); j++) {
                    String activityType = (String) c.getActivityTypeList().get(j);
                    // Log.w("list list",activityType);
                    mHorizontalDataset.add(activityType);
                }
            }
        }
        return convertView;
    }
}
