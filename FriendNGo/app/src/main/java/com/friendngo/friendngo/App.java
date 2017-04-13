package com.friendngo.friendngo;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by krishna on 2017-02-07.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ProximaNovaSemibold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
