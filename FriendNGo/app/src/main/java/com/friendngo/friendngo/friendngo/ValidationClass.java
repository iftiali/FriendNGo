package com.friendngo.friendngo.friendngo;

import android.util.Log;

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
    public final static boolean isNullCheck(String username,String password){

        if(username.equals("") || password.equals("")){
            return false;
        }
        else {
            return true;
        }
    }
}
