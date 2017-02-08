package com.friendngo.friendngo.friendngo;

/**
 * Created by krishna on 2017-02-07.
 */

public class RequestModel {

    String name;
    String year;
    String homeCity;

    public RequestModel(String name, String year, String homeCity ) {
        this.name=name;
        this.year=year;
        this.homeCity=homeCity;

    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getHomeCity() {
        return homeCity;
    }


}

