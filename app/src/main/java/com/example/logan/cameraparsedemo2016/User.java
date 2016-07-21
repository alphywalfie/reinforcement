package com.example.logan.cameraparsedemo2016;

import io.realm.RealmObject;

/**
 * Created by Azi on 9 Jul 2016.
 */
public class User extends RealmObject {

    private String id;
    private String username;
    private String password;
    private String filename;
    private String aboutMe;
    //private String[] whatDisappoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

//    public String[] getWhatDisappoints() {
//        return whatDisappoints;
//    }
//
//    public void setWhatDisappoints(String[] whatDisappoints) {
//        this.whatDisappoints = whatDisappoints;
//    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}