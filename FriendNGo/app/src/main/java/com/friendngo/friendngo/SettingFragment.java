package com.friendngo.friendngo;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private String checkOnlineToast = null;
    private ImageView settings_profile_picture;
    private TextView settings_created_text;
    private TextView settings_created_point;
    private FrameLayout settings_frame_profile;
    private FrameLayout settings_frame_points ;
    private FrameLayout settings_frame_city;
    private FrameLayout settings_frame_group;
    private FrameLayout settings_frame_share;
    private FrameLayout settings_frame_contact;
    private FrameLayout settings_frame_ratting;
    private FrameLayout settings_frame_about;
    private FrameLayout settings_frame_notifications;
    public static boolean settingsCheck = false;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initXmlView(view);
        checkOnlineToast = getResources().getString(R.string.checkOnlineToast);
        getApiProfile();
        settings_frame_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCheck = true;
                Intent intent = new Intent(getApplicationContext(),MyProfileActivity.class);
                startActivity(intent);
            }
        });
        settings_frame_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PointsHistoryActivity.class);
                startActivity(intent);
            }
        });
        settings_frame_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCheck = true;
                Intent intent = new Intent(getApplicationContext(),MyCity.class);
                startActivity(intent);
            }
        });
        settings_frame_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCheck = true;
                Intent intent = new Intent(getApplicationContext(),SpecialGroup.class);
                startActivity(intent);
            }
        });
        settings_frame_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Notification settings Not Available in Beta", Toast.LENGTH_LONG).show();
            }
        });
        settings_frame_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Share app Not Available in Beta", Toast.LENGTH_LONG).show();
            }
        });
        settings_frame_ratting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rate the app Not Available in Beta", Toast.LENGTH_LONG).show();
            }
        });
        settings_frame_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "About us Not Available in Beta", Toast.LENGTH_LONG).show();
            }
        });
        settings_frame_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Contact us Not Available in Beta", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private void initXmlView(View view) {
        settings_profile_picture = (ImageView)view.findViewById(R.id.settings_profile_picture);
        settings_created_text = (TextView)view.findViewById(R.id.settings_created_text);
        settings_created_point=(TextView)view.findViewById(R.id.settings_created_point);
        settings_frame_profile = (FrameLayout)view.findViewById(R.id.settings_frame_profile);
        settings_frame_points = (FrameLayout)view.findViewById(R.id.settings_frame_points);
        settings_frame_city = (FrameLayout)view.findViewById(R.id.settings_frame_city);
        settings_frame_group = (FrameLayout)view.findViewById(R.id.settings_frame_group);
        settings_frame_notifications = (FrameLayout)view.findViewById(R.id.settings_frame_notifications);
        settings_frame_share = (FrameLayout)view.findViewById(R.id.settings_frame_share);
        settings_frame_ratting = (FrameLayout)view.findViewById(R.id.settings_frame_ratting);
        settings_frame_about = (FrameLayout)view.findViewById(R.id.settings_frame_about);
        settings_frame_contact = (FrameLayout)view.findViewById(R.id.settings_frame_contact);
    }
    private void getApiProfile() {
        if(ValidationClass.checkOnline(getApplicationContext())){
            AsyncHttpClient client = new AsyncHttpClient();
            if (SignIn.static_token != null) {
                client.addHeader("Authorization", "Token " + SignIn.static_token);
            }
            client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

                //GET user profile
                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());

                    try {
                        String firstNameString = response.getString("first_name");
                        settings_created_text.setText(firstNameString);
                        settings_created_point.setText(response.getString("points")+" Points");

                    } catch (JSONException e) {
                        Log.w("JSON EXCEPTION", e.getMessage());
                    }

                    String pictureURL="";
                    //GET Profile image from backend if not available from Facebook
                    if(FacebookLogin.facebook_profile_pic == null) {
                        //GET The image file at the pictureURL
                        AsyncHttpClient client = new AsyncHttpClient();
                        try {
                            pictureURL = response.getString("picture");
                        } catch (JSONException e) {
                            Log.w("GET PROFILE JSON FAIL", e.getMessage().toString());
                        }
                        client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, File response) {
                                Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
                                //Use the downloaded image as the profile picture
                                Uri uri = Uri.fromFile(response);
                                settings_profile_picture.setImageURI(uri);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                Log.w("GET IMAGE FAIL", "Could not retrieve image");
                            }
                        });
                    }else{

                        settings_profile_picture.setImageURI(FacebookLogin.facebook_profile_pic);

                    }
                }
            });



        }else{
            Toast.makeText(getApplicationContext(),checkOnlineToast,Toast.LENGTH_LONG).show();
        }
    }



}
