package com.example.logan.cameraparsedemo2016;

import io.realm.RealmObject;

/**
 * Created by Azi on 9 Jul 2016.
 */
public class User extends RealmObject {

    private String username;
    private String password;

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