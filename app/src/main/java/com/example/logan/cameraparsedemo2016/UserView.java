package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class UserView extends AppCompatActivity {

    private File profilePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        TextView un = (TextView) findViewById(R.id.userProfileLabel);
        TextView am = (TextView) findViewById(R.id.userAboutLabel);
        TextView dm = (TextView) findViewById(R.id.userDisappointLabel);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        String about = intent.getStringExtra("about");
        String disappoint = intent.getStringExtra("disappoints");
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
    }

    //to get the disappointments that this user owns, just launch a realm search that will get the user that owns the disappointments. use the username.
}
