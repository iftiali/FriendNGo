package com.friendngo.friendngo.friendngo;

import java.util.Date;

/**
 * Created by scott on 2017-01-17.
 */

public class UserActivity {

    private String name;
    private String creator;
    private int maxUsers;
//    private SimpleDateFormat createdTime;
    private Date activityTime;
    private String distance;
    private String points;
    private String category;
    private String activityType;
    private double latitude;
    private double longitude;
    private String address;
    public UserActivity(String name,
                        String  creator,
                        int maxUsers,
//                        SimpleDateFormat createdTime,
                        Date activityTime,
                        String address,
                        String distance,
                        String points,
                        String category, String type,
                        double latitude,
                        double longitude) {
        this.name = name;
        this.creator = creator;
        this.maxUsers = maxUsers;
//        this.createdTime = createdTime;
        this.activityTime = activityTime;
        this.address = address;
        this.distance =distance;
        this.points=points;
        this.category = category;
        this.activityType = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getDistance() {return distance;}
    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setPoints(String points) {
        this.points = points;
    }
    public String getPoints() {return points;}


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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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
}
