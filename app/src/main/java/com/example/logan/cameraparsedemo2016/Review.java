package com.example.logan.cameraparsedemo2016;

import java.util.UUID;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Azi on 30 Jun 2016.
 */
public class Review extends RealmObject {

    private String foodName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private float foodPrice;
    private String foodDescription;
    private String foodComment;
    private float foodRating;
    private String foodPhoto;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(float foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }
    public String getFoodComment() {
        return foodComment;
    }

    public void setFoodComment(String foodComment) {
        this.foodComment = foodComment;
    }

    public float getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(float foodRating) {
        this.foodRating = foodRating;
    }

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
    }
}
