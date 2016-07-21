package com.example.logan.cameraparsedemo2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HallOfShame extends AppCompatActivity {

    private RealmHallOfShameAdapter adapter;
    private OrderedRealmCollection<Disappointment> disappointments;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_shame);

        setTitle("Hall of Shame");

        RealmResults<Disappointment> result = realm.where(Disappointment.class).findAll();
        result = result.sort("likes", Sort.DESCENDING);

        ListView lv = (ListView) findViewById(R.id.listView3);
        adapter = new RealmHallOfShameAdapter(this, result);
        lv.setAdapter(adapter);
    }
}
