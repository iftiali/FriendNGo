package com.friendngo.friendngo.friendngo;

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
    public RequestModel(String profileImage,String name, int year, String homeCity,String nationality,String points ) {
        this.profileImage = profileImage;
        this.name=name;
        this.year=year;
        this.homeCity=homeCity;
        this.nationality=nationality;
        this.points=points;

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

