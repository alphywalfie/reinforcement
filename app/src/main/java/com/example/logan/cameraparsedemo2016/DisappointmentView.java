package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DisappointmentView extends AppCompatActivity {

    Realm realm = Realm.getDefaultInstance();
    Disappointment disappointmentToEdit;

    private RealmDisappointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disappointment_view);

        Intent intent2 = getIntent();
        TextView tv = (TextView) findViewById(R.id.titleText2);
        tv.setText(intent2.getStringExtra("title"));

        tv = (TextView) findViewById(R.id.userText);
        tv.setText(intent2.getStringExtra("user"));

        tv = (TextView) findViewById(R.id.captionText);
        tv.setText(intent2.getStringExtra("caption"));

        tv = (TextView) findViewById(R.id.locationText);
        tv.setText(intent2.getFloatExtra("latitude", 0)+","+intent2.getFloatExtra("longitude", 0));
//
//        tv = (TextView) findViewById(R.id.dateText);
//        tv.setText(intent.getStringExtra("month")+"/"+intent.getStringExtra("date")+"/"+intent.getStringExtra("year"));

        if (!(intent2.getStringExtra("filename").equals("")))
        {
            ImageView iv = (ImageView) findViewById(R.id.disappointmentImage);
            File disappointmentImage = new File(intent2.getStringExtra("filename"));
            Picasso.with(this).load(disappointmentImage).fit().into(iv);
        }

        tv = (TextView) findViewById(R.id.unlikeText);
        tv.setText(intent2.getIntExtra("number", 0)+"people have shamed this");
    }

    public void shame(View v)
    {
        Intent intent2 = getIntent();
        Disappointment d = realm.where(Disappointment.class)
                .equalTo("id", intent2.getStringExtra("id"))
                .findFirst();
        realm.beginTransaction();
        d.setLikes(d.getLikes()+1);
        realm.commitTransaction();

        TextView tv = (TextView) findViewById(R.id.unlikeText);
        tv.setText(d.getLikes()+"people have shamed this");
    }
}
