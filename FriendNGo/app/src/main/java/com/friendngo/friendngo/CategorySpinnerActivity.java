package com.friendngo.friendngo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by scott on 2017-01-17.
 */

public class CategorySpinnerActivity extends ArrayAdapter<CategorySpinnerModel>{
        int groupid;
        Activity context;
        ArrayList<CategorySpinnerModel> list;
        LayoutInflater inflater;
public CategorySpinnerActivity(Activity context, int groupid, int id, ArrayList<CategorySpinnerModel>
list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
        }

public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        itemView.setBackgroundColor(Color.WHITE);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.txt);

        textView.setText(list.get(position).getText());
        textView.setBackgroundColor(Color.WHITE);
        return itemView;
        }

public View getDropDownView(int position, View convertView, ViewGroup
        parent){
        return getView(position,convertView,parent);

        }
        }