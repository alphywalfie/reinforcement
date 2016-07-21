package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
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

        RealmList<Disappointment> result2 = new RealmList<Disappointment>();
        if (result.size() > 5)
        {
            for (int i = 5; i < result.size(); i++)
            {
                final Disappointment d = result.get(i);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        d.deleteFromRealm();
                    }
                });
            }
        }

        ListView lv = (ListView) findViewById(R.id.listView3);
        adapter = new RealmHallOfShameAdapter(this, result);
        lv.setAdapter(adapter);
    }

    public void viewDisappointment(View v)
    {
        Disappointment d = (Disappointment) v.getTag();

        if (d != null)
        {
            Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.DisappointmentView.class);
            intent.putExtra("title", d.getTitle());
            intent.putExtra("user", d.getUser());
            intent.putExtra("caption", d.getCaption());
            intent.putExtra("latitude", d.getLatitude());
            intent.putExtra("longitude", d.getLongitude());
//        intent.putExtra("year", d.getYear());
//        intent.putExtra("month", d.getMonth());
//        intent.putExtra("date", d.getDate());
            if (d.getFilename() != null)
            {
                intent.putExtra("filename", d.getFilename());
            }
            else
            {
                intent.putExtra("filename", "");
            }
            intent.putExtra("id", d.getId());
            intent.putExtra("number", d.getLikes());
            startActivity(intent);
        }
    }

    public void home (View v)
    {
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.ListActivity.class);
        startActivity(intent);
    }
}
