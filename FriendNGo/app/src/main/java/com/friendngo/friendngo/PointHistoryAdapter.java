package com.friendngo.friendngo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by krishna on 2017-04-07.
 */

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.MyViewHolder> {
    private List<pointHistoryModel> pointHistory;
    Context mContext;

    public PointHistoryAdapter(List<pointHistoryModel> pointHistory,Context mContext) {
        this.pointHistory= pointHistory;
        this.mContext=mContext;
    }
    @Override
    public PointHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points_history_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PointHistoryAdapter.MyViewHolder holder, int position) {
        final pointHistoryModel pointHistoryModel = pointHistory.get(position);
        holder.points_history_points.setText(pointHistoryModel.getPointsHistory());
        holder.points_history_created_text.setText(pointHistoryModel.getTextCreate());
        //holder.points_history_time.setText(pointHistoryModel.getTimePointHistory());
        int timeStamp= Integer.parseInt(pointHistoryModel.getTimePointHistory());
        if(timeStamp <=60){
            holder.points_history_time.setText(pointHistoryModel.getTimePointHistory()+"m ago");
        }else {
            int hour = timeStamp/60;
            holder.points_history_time.setText(hour+"h ago");
        }
    }

    @Override
    public int getItemCount() {
      return pointHistory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView points_history_points,points_history_created_text,points_history_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            points_history_points = (TextView)itemView.findViewById(R.id.points_history_points);
            points_history_created_text = (TextView)itemView.findViewById(R.id.points_history_created_text);
            points_history_time = (TextView)itemView.findViewById(R.id.points_history_time);
        }
    }
}
