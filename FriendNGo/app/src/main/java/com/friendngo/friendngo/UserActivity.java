package com.friendngo.friendngo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by scott on 2017-01-17.
 */

public class UserActivity {
    private String name;
    private String creator;
    private String creatorAge;
    private String creatorStatus;
    private int maxUsers;
    private Date activityTime;
    private Date activityEndTime;
    private String distance;
    private String points;
    private long activity_pk;
    private String category;
    private String homeNationality;
    private String homeCity;
    private String activityType;
    private double latitude;
    private double longitude;
    private double clatitude;
    private double clongitude;
    private String address;
    private String description;
    private long creator_PK;
    private Date endTime;
    private String profilePicURL;
    private Boolean isPaid;
    private String eventPictureURL;
    private String userStatus;
    List attendingList = new ArrayList<JSONObject>();
    private int request_state;

    public  UserActivity(){
    }

    public UserActivity(String home_city,
                        String home_nationality,
                        String name,
                        String  creator,
                        String creatorAge,
                        String creatorStatus,
                        int maxUsers,
                        Date activityTime,
                        Date activityEndTime,
                        String address,
                        String description,
                        String distance,
                        String points,
                        String category,
                        String type,
                        double latitude,
                        double longitude,
                        long creator_PK,
                        long activity_pk,
                        Date endTime,
                        String profilePicURL,
                        Boolean isPaid,
                        List attendingList,
                        int request_state,
                        String eventPictureURL,
                        String userStatus) {
        this.homeCity = home_city;
        this.homeNationality = home_nationality;
        this.name = name;
        this.creator = creator;
        this.creatorAge = creatorAge;
        this.creatorStatus = creatorStatus;
        this.maxUsers = maxUsers;
//        this.createdTime = createdTime;
        this.activityTime = activityTime;
        this.activityEndTime= activityEndTime;
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
        this.endTime = endTime;
        this.profilePicURL = profilePicURL;
        this.isPaid = isPaid;
        this.attendingList = attendingList;
        this.request_state = request_state;
        this.eventPictureURL = eventPictureURL;
        this.userStatus = userStatus;
    }

    public double getClatitude() {
        return clatitude;
    }

    public void setClatitude(double clatitude) {
        this.clatitude = clatitude;
    }

    public double getClongitude() {
        return clongitude;
    }

    public void setClongitude(double clongitude) {
        this.clongitude = clongitude;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public int getRequest_state() {
        return request_state;
    }

    public void setRequest_state(int request_state) {
        this.request_state = request_state;
    }

    public List getAttendingList() {
        return attendingList;
    }

    public void setAttendingList(List attendingList) {
        this.attendingList = attendingList;
    }

    public long getCreator_PK() {
        return creator_PK;
    }

    public void setCreator_PK(long creator_PK) {
        this.creator_PK = creator_PK;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
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
    public Date getActivityEndTime() {
    return activityEndTime;
    }
    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }
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

    public double getcLatitude() {
        return clatitude;
    }

    public void setcLatitude(double clatitude) {
        this.clatitude = clatitude;
    }

    public double getcLongitude() {
        return clongitude;
    }

    public void setcLongitude(double clongitude) {
        this.clongitude = clongitude;
    }
    public boolean getisPaid() {
        return isPaid;
    }

    public void setisPaid(boolean isPaid) {
        this.isPaid = isPaid; }

    public String getEventPictureUrl() {
        return eventPictureURL;
    }

    public void setEventPictureURL(String eventPictureURL) {
        this.eventPictureURL = eventPictureURL; }

    public String getuserStatus() {
        return userStatus;
    }
    public void setuserStatus(String userStatus) {
        this.userStatus = userStatus; }
}
