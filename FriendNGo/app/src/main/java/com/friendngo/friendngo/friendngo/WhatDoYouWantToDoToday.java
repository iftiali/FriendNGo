package com.friendngo.friendngo.friendngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WhatDoYouWantToDoToday extends AppCompatActivity {

    private ImageView sports_category;
    private ImageView sports_category_check;
    private Button jogging_button;
    private ImageView jogging_button_check;
    private Button tennis_button;
    private ImageView tennis_button_check;
    private Button hockey_button;
    private ImageView hockey_button_check;

    private ImageView nightlife_category;
    private ImageView nightlife_category_check;
    private Button dancing_button;
    private ImageView dancing_button_check;
    private Button jazz_button;
    private ImageView jazz_button_check;
    private Button pubs_button;
    private ImageView pubs_button_check;

    private ImageView arts_category;
    private ImageView arts_category_check;
    private Button museum_button;
    private ImageView museum_button_check;
    private Button gallery_button;
    private ImageView gallery_button_check;
    private Button city_tour_button;
    private ImageView city_tour_button_check;

    private Button save_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_do_you_want_to_do_today);

        //Sets the top bar text
        getSupportActionBar().setTitle("What do you want to do today?");

        //Handler for sports category
        sports_category = (ImageView) findViewById(R.id.sports_image);
        sports_category_check = (ImageView) findViewById(R.id.sports_image_check);
        sports_category_check.setVisibility(View.INVISIBLE);
        sports_category.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(sports_category_check.getVisibility()==View.INVISIBLE){
                    sports_category_check.setVisibility(View.VISIBLE);
                }else{
                    sports_category_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Handler for sports buttons
        jogging_button = (Button) findViewById(R.id.jogging_button);
        jogging_button_check = (ImageView) findViewById(R.id.jogging_button_check);
        jogging_button_check.setVisibility(View.INVISIBLE);
        jogging_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(jogging_button_check.getVisibility()==View.INVISIBLE){
                    jogging_button_check.setVisibility(View.VISIBLE);
                }else{
                    jogging_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        tennis_button = (Button) findViewById(R.id.tennis_button);
        tennis_button_check = (ImageView) findViewById(R.id.tennis_button_check);
        tennis_button_check.setVisibility(View.INVISIBLE);
        tennis_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(tennis_button_check.getVisibility()==View.INVISIBLE){
                    tennis_button_check.setVisibility(View.VISIBLE);
                }else{
                    tennis_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        hockey_button = (Button) findViewById(R.id.hockey_button);
        hockey_button_check = (ImageView) findViewById(R.id.hockey_button_check);
        hockey_button_check.setVisibility(View.INVISIBLE);
        hockey_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(hockey_button_check.getVisibility()==View.INVISIBLE){
                    hockey_button_check.setVisibility(View.VISIBLE);
                }else{
                    hockey_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });


        //Handler for nightlife category
        nightlife_category = (ImageView) findViewById(R.id.nightlife_image);
        nightlife_category_check = (ImageView) findViewById(R.id.nightlife_image_check);
        nightlife_category_check.setVisibility(View.INVISIBLE);
        nightlife_category.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(nightlife_category_check.getVisibility()==View.INVISIBLE){
                    nightlife_category_check.setVisibility(View.VISIBLE);
                }else{
                    nightlife_category_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Handler for sports buttons
        dancing_button = (Button) findViewById(R.id.dancing_button);
        dancing_button_check = (ImageView) findViewById(R.id.dancing_button_check);
        dancing_button_check.setVisibility(View.INVISIBLE);
        dancing_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dancing_button_check.getVisibility()==View.INVISIBLE){
                    dancing_button_check.setVisibility(View.VISIBLE);
                }else{
                    dancing_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        jazz_button = (Button) findViewById(R.id.use_email_button);
        dancing_button_check = (ImageView) findViewById(R.id.jogging_button_check);
        dancing_button_check.setVisibility(View.INVISIBLE);
        dancing_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dancing_button_check.getVisibility()==View.INVISIBLE){
                    dancing_button_check.setVisibility(View.VISIBLE);
                }else{
                    dancing_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        pubs_button = (Button) findViewById(R.id.pubs_button);
        pubs_button_check = (ImageView) findViewById(R.id.pubs_button_check);
        pubs_button_check.setVisibility(View.INVISIBLE);
        pubs_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(pubs_button_check.getVisibility()==View.INVISIBLE){
                    pubs_button_check.setVisibility(View.VISIBLE);
                }else{
                    pubs_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });


        //Handler for arts category
        arts_category = (ImageView) findViewById(R.id.arts_image);
        arts_category_check = (ImageView) findViewById(R.id.arts_image_check);
        arts_category_check.setVisibility(View.INVISIBLE);
        arts_category.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(arts_category_check.getVisibility()==View.INVISIBLE){
                    arts_category_check.setVisibility(View.VISIBLE);
                }else{
                    arts_category_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Handler for arts buttons
        museum_button = (Button) findViewById(R.id.museum_button);
        museum_button_check = (ImageView) findViewById(R.id.museum_button_check);
        museum_button_check.setVisibility(View.INVISIBLE);
        museum_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(museum_button_check.getVisibility()==View.INVISIBLE){
                    museum_button_check.setVisibility(View.VISIBLE);
                }else{
                    museum_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        gallery_button = (Button) findViewById(R.id.gallery_button);
        gallery_button_check = (ImageView) findViewById(R.id.gallery_button_check);
        gallery_button_check.setVisibility(View.INVISIBLE);
        gallery_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(gallery_button_check.getVisibility()==View.INVISIBLE){
                    gallery_button_check.setVisibility(View.VISIBLE);
                }else{
                    gallery_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        city_tour_button = (Button) findViewById(R.id.city_tour_button);
        city_tour_button_check = (ImageView) findViewById(R.id.city_tour_button_check);
        city_tour_button_check.setVisibility(View.INVISIBLE);
        city_tour_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(city_tour_button_check.getVisibility()==View.INVISIBLE){
                    city_tour_button_check.setVisibility(View.VISIBLE);
                }else{
                    city_tour_button_check.setVisibility(View.INVISIBLE);
                }
            }
        });


        //Last but not least, the save button
        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Create POST request
                //TODO: Modify Backend to support
                WhatDoYouWantToDoToday.this.finish();
            }
        });


    }
}
