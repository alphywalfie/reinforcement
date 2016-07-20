package com.example.logan.cameraparsedemo2016;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Azi on 9 Jul 2016.
 */
public class MyApplication extends Application {
    public void onCreate()
    {
        super.onCreate();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
