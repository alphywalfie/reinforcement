package com.example.logan.cameraparsedemo2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class HallOfShame extends AppCompatActivity {

    private RealmHallOfShameAdapter adapter;
    private OrderedRealmCollection<Disappointment> disappointments;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_shame);

        setTitle("Wall of Shame");

        ListView lv = (ListView) findViewById(R.id.listView3);
        adapter = new RealmHallOfShameAdapter(this, realm.where(Disappointment.class).findAll());
        lv.setAdapter(adapter);
    }
}
