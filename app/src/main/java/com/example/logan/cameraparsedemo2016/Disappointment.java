package com.example.logan.cameraparsedemo2016;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by lance on 21/07/2016.
 */
public class Disappointment extends RealmObject {
    private String id;
    private String title;
    private String user;
    private String filename;
    private String caption;
    private Double latitude;
    private Double longitude;
    private int year;
    private int month;
    private int date;
<<<<<<< HEAD
    private int likes;
=======
    private RealmList<Shame> shames;

    public Disappointment() {
    }
>>>>>>> origin/master

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public RealmList<Shame> getShames() {
        return shames;
    }

    public void setShames(RealmList<Shame> shames) {
        this.shames = shames;
    }
}
