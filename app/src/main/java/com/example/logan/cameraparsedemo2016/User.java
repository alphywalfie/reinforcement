package com.example.logan.cameraparsedemo2016;

import io.realm.RealmObject;

/**
 * Created by Azi on 9 Jul 2016.
 */
public class User extends RealmObject {

    private String username;
    private String password;
    private String profile;
    private String aboutMe;
    private String whatDisappoints;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getWhatDisappoints() {
        return whatDisappoints;
    }

    public void setWhatDisappoints(String whatDisappoints) {
        this.whatDisappoints = whatDisappoints;
    }

/*    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String filename;*/

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