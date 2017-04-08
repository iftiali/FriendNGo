package com.friendngo.friendngo;

/**
 * Created by krishna on 2017-04-07.
 */

public class pointHistoryModel {

    private String pointsHistory, textCreate, timePointHistory;

    public pointHistoryModel() {
    }
    public pointHistoryModel(String pointsHistory, String textCreate, String timePointHistory) {
        this.pointsHistory = pointsHistory;
        this.textCreate = textCreate;
        this.timePointHistory = timePointHistory;
    }
    public String getPointsHistory() {
        return pointsHistory;
    }

    public void setPointsHistory(String pointsHistory) {
        this.pointsHistory = pointsHistory;
    }

    public String getTextCreate() {
        return textCreate;
    }

    public void setTextCreate(String textCreate) {
        this.textCreate =textCreate;
    }

    public String getTimePointHistory() {
        return timePointHistory;
    }

    public void setTimePointHistory(String timePointHistory) {
        this.timePointHistory = timePointHistory;
    }
}
