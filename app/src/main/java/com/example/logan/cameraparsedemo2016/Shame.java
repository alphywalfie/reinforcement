package com.example.logan.cameraparsedemo2016;

import io.realm.RealmObject;

/**
 * Created by Azi on 22 Jul 2016.
 */
public class Shame extends RealmObject {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
