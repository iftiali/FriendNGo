package com.friendngo.friendngo;

/**
 * Created by krishna on 2017-02-07.
 */

public class RequestModel {
    String profileImage;
    String name;
    int year;
    String homeCity;
    String nationality;
    String points;
    int request_state;
    long sender_id;

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    int request_id;

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public RequestModel(long sender_id, String profileImage, String name, int request_state, int year, String homeCity, String nationality, String points, int request_id) {
        this.sender_id = sender_id;
        this.profileImage = profileImage;
        this.name=name;
        this.year=year;
        this.homeCity=homeCity;
        this.nationality=nationality;
        this.points=points;
        this.request_state=request_state;
        this.request_id = request_id;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getRequest_state() {
        return request_state;
    }

    public void setRequest_state(int request_state) {
        this.request_state = request_state;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getProfileImage(){return profileImage;}
    public void setProfileImage(String profileImage){this.profileImage = profileImage;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year){this.year = year;}

    public String getHomeCity() {
        return homeCity;
    }
    public void getHomeCity(String homeCity) {
        this.homeCity= homeCity;
    }

    public void setNationality(String Nationality){this.nationality = nationality;}
    public String getNationality(){return nationality;}

    public void setPoints(){this.points = points;}
    public String getPoints(){return points;}
}

