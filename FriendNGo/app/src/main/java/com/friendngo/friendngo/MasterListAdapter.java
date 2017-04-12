package com.friendngo.friendngo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        //Setup the variables
        mHorizontalDataset = new ArrayList<>();
        List categoryArrayList=new ArrayList<Category>();

        //Inflate the layout
        convertView = layoutInflater.inflate(R.layout.activity_what_do_you_want2, null);
        ImageView categoryImage = (ImageView)convertView.findViewById(R.id.category_image_button);
        final ImageView categoryCheckMark = (ImageView)convertView.findViewById(R.id.master_category_check_mark);
        TextView categorySubtext = (TextView)convertView.findViewById(R.id.category_list_subtext);
        mHorizontalRecycleView = (RecyclerView)convertView.findViewById(R.id.recycler_view);
        mHorizontallayoutManager = new LinearLayoutManager(convertView.getContext(),LinearLayoutManager.HORIZONTAL,false);

        int xx = 0;
        //Set the category image
        Category category = (Category) WhatDoYouWantToDoToday.categoryList.get(position);
        switch (category.name) {
            case "Art & Culture":
                xx =1;
                categoryImage.setImageResource(R.drawable.art_exposition);
                break;
            case "Nightlife":
                xx =2;
                categoryImage.setImageResource(R.drawable.concert);
                break;
            case "Sports":
                xx =3;
                categoryImage.setImageResource(R.drawable.running);
                break;
            case "Help & Association":
                xx =4;
                categoryImage.setImageResource(R.drawable.handshake);
                break;
            case "Fun & Crazy":
                xx =5;
                categoryImage.setImageResource(R.drawable.naked_run);
                break;
            case "Games":
                xx =6;
                categoryImage.setImageResource(R.drawable.billard);
                break;
            case "Travel & Road-Trip":
                xx =7;
                categoryImage.setImageResource(R.drawable.backpack);
                break;
            case "Nature & Outdoors":
                xx =8;
                categoryImage.setImageResource(R.drawable.camping);
                break;
            case "Social Activities":
                xx =9;
                categoryImage.setImageResource(R.drawable.grab_drink);
                break;
            case "Professional & Networking":
                xx =10;
                categoryImage.setImageResource(R.drawable.coworking);
                break;
            default:
                xx=0;
                categoryImage.setImageResource(R.drawable.naked_run);
        }

        //Set Inner Layout Adapters

        mHorizontalRecycleView.setLayoutManager(mHorizontallayoutManager);
        mHorizontalAdapter = new WhatDoyouWantToDoTodayHorizontalRow(mHorizontalDataset,xx);
        mHorizontalRecycleView.setAdapter(mHorizontalAdapter);
        mHorizontalRecycleView.setHasFixedSize(true);

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
        categoryArrayList = WhatDoYouWantToDoToday.categoryList;
        Category c = new Category();

        for(int i =0; i<categoryArrayList.size(); i++) {
            c = (Category) categoryArrayList.get(i);
            if (itemSelected.equals(c.getName())) {
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
