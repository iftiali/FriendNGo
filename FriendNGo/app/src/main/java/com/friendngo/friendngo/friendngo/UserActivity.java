package com.friendngo.friendngo.friendngo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by scott on 2017-01-17.
 */

public class UserActivity {

    private String name;
    private int creator;
    private int maxUsers;
//    private SimpleDateFormat createdTime;
    private Date activityTime;
    private String type;
    private double latitude;
    private double longitude;

    public UserActivity(String name,
                        int creator,
                        int maxUsers,
//                        SimpleDateFormat createdTime,
                        Date activityTime,
                        String type,
                        double latitude,
                        double longitude) {
        this.name = name;
        this.creator = creator;
        this.maxUsers = maxUsers;
//        this.createdTime = createdTime;
        this.activityTime = activityTime;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

//    public SimpleDateFormat getCreatedTime() {
//        return createdTime;
//    }
//
//    public void setCreatedTime(SimpleDateFormat createdTime) {
//        this.createdTime = createdTime;
//    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //TODO: Define getters and setters

    //TODO:

}
