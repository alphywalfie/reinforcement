package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserView extends AppCompatActivity {

    private File profilePhoto;
    private ProfileAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        TextView un = (TextView) findViewById(R.id.userProfileLabel);
        TextView am = (TextView) findViewById(R.id.userAboutLabel);
        TextView dm = (TextView) findViewById(R.id.userDisappointLabel);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        setTitle(user);
        String about = intent.getStringExtra("about");
        String disappoint = intent.getStringExtra("disappoints");
        SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
        un.setText(user);
        am.setText(about);
        dm.setText(disappoint);
        if(intent.getStringExtra("profile") != null)
        {
            String photoPath = intent.getStringExtra("profile");
            profilePhoto = new File(photoPath);
            ImageView pp = (ImageView) findViewById(R.id.userProfilePhoto);
            Picasso.with(this).load(profilePhoto).fit().into(pp);
        }

        ListView lv = (ListView) findViewById(R.id.listView2);
        RealmQuery<User> userToQuery = realm.where(User.class);
        userToQuery.equalTo("username", user);
        User result1 = userToQuery.findFirst();
        RealmQuery<Disappointment> query = realm.where(Disappointment.class);
        query.equalTo("user", result1.getUsername());
        RealmResults<Disappointment> userDisappointments = query.findAll();
        adapter = new ProfileAdapter(this, userDisappointments);
        lv.setAdapter(adapter);
    }
}
