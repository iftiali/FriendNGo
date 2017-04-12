package com.friendngo.friendngo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by krishna on 2017-02-15.
 */
public class WhatDoyouWantToDoTodayHorizontalRow extends RecyclerView.Adapter<WhatDoyouWantToDoTodayHorizontalRow.ViewHolder>{

    private ArrayList<String> mDataset;
    private int xx =0;
    public WhatDoyouWantToDoTodayHorizontalRow(ArrayList<String> mDataset,int xx) {
        this.mDataset = mDataset;
        this.xx = xx;
    }

    @Override
    public WhatDoyouWantToDoTodayHorizontalRow.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_what_do_you_want_horizontal_row,parent,false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final WhatDoyouWantToDoTodayHorizontalRow.ViewHolder holder, int position) {
    holder.museum_button.setText(mDataset.get(position));
    holder.mImageView.setImageResource(R.drawable.check);
    holder.mImageView.setVisibility(View.INVISIBLE);

        if(xx ==1) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(202, 144, 111));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 2) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(163, 60, 241));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 3) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(52, 170, 252));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 4) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(0, 174, 141));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 5) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(239, 171, 62));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 6) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(47, 43, 207));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 7) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(3, Color.rgb(99, 220, 226));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 8) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(3, Color.rgb(29, 226, 141));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 9) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(3,Color.rgb(183, 63, 23));
            holder.museum_button.setBackground(gd);
        }
        else if(xx == 10) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2,Color.rgb(225, 25, 185));
            holder.museum_button.setBackground(gd);
        }
        else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.GRAY);
            holder.museum_button.setBackground(gd);
        }
    holder.museum_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //ToDo:HTTP POST the categories and Activity
            if( holder.mImageView.getVisibility()==View.INVISIBLE){
                holder.mImageView.setVisibility(View.VISIBLE);
            }else {
                holder.mImageView.setVisibility(View.INVISIBLE);
            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button museum_button;
        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.museum_button_check);
            museum_button = (Button) itemView.findViewById(R.id.museum_button);
        }
    }
}
