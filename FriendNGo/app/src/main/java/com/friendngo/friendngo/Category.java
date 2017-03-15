package com.friendngo.friendngo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017-02-02.
 */

public class Category {
//test dev4
    String name;
   List activityTypeList = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getActivityTypeList() {
        return activityTypeList;
    }

    public void addActivityType(String activityType) {
        this.activityTypeList.add(activityType);
    }
}
