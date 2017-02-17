package com.friendngo.friendngo.friendngo;

import android.support.v7.widget.RecyclerView;
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

    public WhatDoyouWantToDoTodayHorizontalRow(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
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
