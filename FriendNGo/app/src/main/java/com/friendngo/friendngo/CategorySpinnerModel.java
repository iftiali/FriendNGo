package com.friendngo.friendngo;



public class CategorySpinnerModel {

    String text;
    Integer imageId;

    public CategorySpinnerModel(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}