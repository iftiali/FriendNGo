package com.friendngo.friendngo;


import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.util.List;

/**
 * Created by krishna on 2017-01-30.
 */

public class ValidationClass {

    public final static boolean isValidEmail(CharSequence target) {

        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isNullCheck(String username, String password) {

        if (username.equals("") || password.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public final static double get_longitude(String strAddress,Geocoder coder) {
        double locationValue =0;
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                locationValue =0;
                Log.w("Error ", "address in null");
            }
            Address location = address.get(0);

           locationValue = location.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationValue;
    }
    public final static double get_Latitude(String strAddress,Geocoder coder) {
        double locationValue =0;
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                Log.w("Error ", "address in null");
                locationValue =0;
            }
            Address location = address.get(0);
            locationValue = location.getLatitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  locationValue;
    }
}
