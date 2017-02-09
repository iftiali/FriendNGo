package com.friendngo.friendngo.friendngo;

import java.util.Date;

/**
 * Created by scott on 2017-01-17.
 */

public class UserActivity {
    private String name;
    private String creator;
    private String creatorAge;
    private String creatorStatus;
    private int maxUsers;
//    private SimpleDateFormat createdTime;
    private Date activityTime;
    private String distance;
    private String points;
    private long activity_pk;
    private String category;
    private String homeNationality;
    private String homeCity;
    private String activityType;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private long creator_PK;
    public UserActivity(String home_city,
                        String home_nationality,
                        String name,
                        String  creator,
                        String creatorAge,
                        String creatorStatus,
                        int maxUsers,
//                        SimpleDateFormat createdTime,
                        Date activityTime,
                        String address,
                        String description,
                        String distance,
                        String points,
                        String category,
                        String type,
                        double latitude,
                        double longitude,
                        long creator_PK,
                        long activity_pk) {
        this.homeCity = home_city;
        this.homeNationality = home_nationality;
        this.name = name;
        this.creator = creator;
        this.creatorAge = creatorAge;
        this.creatorStatus = creatorStatus;
        this.maxUsers = maxUsers;
//        this.createdTime = createdTime;
        this.activityTime = activityTime;
        this.address = address;
        this.description = description;
        this.distance =distance;
        this.points=points;
        this.category = category;
        this.activityType = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator_PK = creator_PK;
        this.activity_pk = activity_pk;
    }


    public long getActivity_pk() {
        return activity_pk;
    }

    public void setActivity_pk(long activity_pk) {
        this.activity_pk = activity_pk;
    }
    public long getcreator_PK() {
        return creator_PK;
    }

    public void setcreator_PK(long creator_PK) {
        this.creator_PK = creator_PK;
    }
    public String getHomeNationality() {
        return homeNationality;
    }

    public void setHomeNationality(String homeNationality) {
        this.homeNationality = homeNationality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorStatus() {
        return creatorStatus;
    }

    public void setCreatorStatus(String creatorStatus) {
        this.creatorStatus = creatorStatus;
    }

    public String getCreatorAge() {
        return creatorAge;
    }

    public void setCreatorAge(String creatorAge) {
        this.creatorAge = creatorAge;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
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
