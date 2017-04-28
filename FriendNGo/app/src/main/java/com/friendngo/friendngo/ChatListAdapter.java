package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by krishna on 2017-03-09.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>  {
    private List<ChatListModel> ChatList;
    Context mContext;
    public ChatListAdapter(List<ChatListModel> ChatList,Context mContext) {
        this.ChatList= ChatList;
        this.mContext=mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ChatListModel chatListView = ChatList.get(position);
        holder.paid_event_created_text.setText(chatListView.getActivityName());

        holder.messages_created_text_person_name.setText(chatListView.getPersonName()+":");
        String messageSize=chatListView.getPersonLastMessage();
        if(messageSize.length() <20){
        holder.messages_created_text_message.setText(chatListView.getPersonLastMessage());
        }else {
            holder.messages_created_text_message.setText(messageSize.substring(0,20)+"...");
        }

        int timeStamp= Integer.parseInt(chatListView.getTimeCreatedAgo());
        int hour = timeStamp/60;
        if(timeStamp <=60){
            holder.messages_activity_time.setText(chatListView.getTimeCreatedAgo()+"m ago");
        }
        else if(hour<24) {

            holder.messages_activity_time.setText(hour+"h ago");
        }else{
            int days = hour/24;
            holder.messages_activity_time.setText(days+"d ago");

        }

        switch(chatListView.getCategory()){
            case "Art & Culture":
                holder.profilePicture.setImageResource(R.drawable.art_exposition);
                break;
            case "Nightlife":
                holder.profilePicture.setImageResource(R.drawable.music);
                break;
            case "Sports":
                holder.profilePicture.setImageResource(R.drawable.running);
                break;
            case "Professional & Networking":
                holder.profilePicture.setImageResource(R.drawable.coworking);
                break;
            case "Fun & Crazy":
                holder.profilePicture.setImageResource(R.drawable.naked_run);
                break;
            case "Games":
                holder.profilePicture.setImageResource(R.drawable.billard);
                break;
            case "Travel & Road-Trip":
                holder.profilePicture.setImageResource(R.drawable.backpack);
                break;
            case "Nature & Outdoors":
                holder.profilePicture.setImageResource(R.drawable.camping);
                break;
            case "Social Activities":
                holder.profilePicture.setImageResource(R.drawable.grab_drink);
                break;
            case "Help & Association":
                holder.profilePicture.setImageResource(R.drawable.coworking);
        }
        holder.relativeCLick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChatRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("activityID",chatListView.getactivityID());
                intent.putExtra("activityName",chatListView.getActivityName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView paid_event_created_text, messages_created_text_person_name, messages_created_text_message,messages_activity_time;
        ImageView profilePicture;
        RelativeLayout relativeCLick;
        public MyViewHolder(View view) {
            super(view);
            relativeCLick= (RelativeLayout)view.findViewById(R.id.relativeCLick);
            profilePicture = (ImageView)view.findViewById(R.id.paid_event_profile_picture);
            paid_event_created_text = (TextView) view.findViewById(R.id.paid_event_created_text);
            messages_created_text_person_name = (TextView) view.findViewById(R.id.messages_created_text_person_name);
            messages_created_text_message= (TextView) view.findViewById(R.id.messages_created_text_message);

            messages_activity_time = (TextView) view.findViewById(R.id.messages_activity_time);
        }
    }
}
