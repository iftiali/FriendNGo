package com.friendngo.friendngo.friendngo;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scott on 2017-03-03.
 */

public class AttendingHorizontalRow extends RecyclerView.Adapter<AttendingHorizontalRow.ViewHolder> {

    private UserActivity userActivity;

    public AttendingHorizontalRow( UserActivity mDataset) {
        this.userActivity = mDataset;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView user_image;
        public TextView user_name_text;

        public ViewHolder(View itemView) {
            super(itemView);
            user_image = (CircularImageView)itemView.findViewById(R.id.attending_profile_image);
            user_name_text = (TextView) itemView.findViewById(R.id.attending_user_name);
        }
    }

    @Override
    public AttendingHorizontalRow.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attending_user_list_item,parent,false);

        // set the view's size, margins, paddings and layout parameters
        AttendingHorizontalRow.ViewHolder vh = new AttendingHorizontalRow.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AttendingHorizontalRow.ViewHolder holder, int position) {
            JSONObject attendingJSON = (JSONObject) userActivity.attendingList.get(position);

            String url = "";
            String name = "";
            try {
                url = attendingJSON.getString("picture");
                name = attendingJSON.getString("first_name");
            } catch (JSONException e){
                Log.w("JSONException",e.toString());
            }
            Uri myUri = Uri.parse(url);

            holder.user_image.setImageURI(myUri);
            holder.user_name_text.setText(name);


        //GET The image file at the pictureURL
//        AsyncHttpClient client = new AsyncHttpClient();
//
////        final ImageView profilePic = (CircularImageView) holder.findViewById(R.id.profilepicture);
//        client.get(MainActivity.base_host_url + mImageURLSet.get(position), new FileAsyncHttpResponseHandler(mContext) {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, File response) {
//                Log.w("GET IMAGE SUCCESS","Successfully Retrieved The Image");
//                //Use the downloaded image as the profile picture
//                Uri uri = Uri.fromFile(response);
//                profilePic.setImageURI(uri);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                Log.w("GET IMAGE FAIL","Could not retrieve image");
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return userActivity.attendingList.size();
    }
}
