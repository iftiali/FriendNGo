package com.friendngo.friendngo.friendngo;

/**
 * Created by krishna on 2017-03-09.
 */

public class ChatListModel {
    private String category,activityName,personName,personLastMessage,timeCreatedAgo;

    public ChatListModel(String category, String activityName, String personName,String personLastMessage,String timeCreatedAgo) {
        this.category = category;
        this.activityName = activityName;
        this.personName = personName;
        this.personLastMessage = personLastMessage;
        this.timeCreatedAgo = timeCreatedAgo;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
    public String getPersonLastMessage() {
        return personLastMessage;
    }

    public void setPersonLastMessage(String personLastMessage) {
        this.personLastMessage = personLastMessage;
    }
    public String getTimeCreatedAgo() {
        return timeCreatedAgo;
    }

    public void settimeCreatedAgo(String timeCreatedAgo) {
        this.timeCreatedAgo = timeCreatedAgo;
    }
}
